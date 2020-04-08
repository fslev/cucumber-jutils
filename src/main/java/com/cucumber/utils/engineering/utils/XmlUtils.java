package com.cucumber.utils.engineering.utils;

import org.w3c.dom.*;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xmlunit.builder.Input;
import org.xmlunit.builder.Transform;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class XmlUtils {

    public static final String STYLE_XSL = "<?xml version=\"1.0\"?>"
            + "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">"
            + "<xsl:output method=\"xml\" encoding=\"UTF-8\" indent=\"no\" />"
            + "<xsl:strip-space elements=\"*\"/><xsl:template compare=\"@*|node()\"><xsl:copy>"
            + "<xsl:apply-templates select=\"@*|node()\"></xsl:apply-templates></xsl:copy></xsl:template>"
            + "<xsl:template compare=\"*/text()\">"
            + "    <xsl:value-of select=\".\" disable-output-escaping=\"yes\"/>\n"
            + "</xsl:template></xsl:stylesheet>";

    public static String toSingleLineString(String xml) {
        Source source = Input.fromString(xml).build();
        return Transform.source(source).withStylesheet(Input.fromString(STYLE_XSL).build()).build()
                .toString();
    }

    /**
     * @param xml
     * @return check is valid XML
     */
    public static boolean isValid(String xml) {
        try {
            toDocument(xml);
        } catch (SAXException | IOException e) {
            return false;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * @param xml
     * @return Document representation of String XML
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static Document toDocument(String xml) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        builder.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException exception) {
            }

            @Override
            public void error(SAXParseException exception) {
            }

            @Override
            public void fatalError(SAXParseException exception) {
            }
        });
        return builder.parse(new InputSource(new StringReader(xml)));
    }

    /**
     * Walks the XML, applies a function on each XML node element and attribute
     * and returns resulted values mapped to their corresponding XML paths
     *
     * @param xml
     * @param processFunction Applied for each XML node element and attribute
     * @param <R>             Process function return type
     * @return A map between resulted node values and their corresponding XML paths
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static <R> Map<String, R> walkXmlAndProcessNodes(String xml, Function<String, R> processFunction) throws IOException, SAXException, ParserConfigurationException {
        Map<String, R> resultsMap = new HashMap<>();
        Element element = toDocument(xml).getDocumentElement();
        Xml.walkAndProcessNode(element, processFunction, "", resultsMap);
        return resultsMap;
    }
}

class Xml {

    static <R> void walkAndProcessNode(Node node, Function<String, R> processFct, String parentPath, Map<String, R> results) {
        R result = processNode(node, processFct);
        String currentPath = getNodePath(node, parentPath);
        if (result != null) {
            results.put(currentPath, result);
        }
        if (node.hasAttributes()) {
            NamedNodeMap attributes = node.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                walkAndProcessNode(attributes.item(i), processFct, currentPath, results);
            }
        }
        if (node.hasChildNodes()) {
            NodeList nodeList = node.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node item = nodeList.item(i);
                walkAndProcessNode(item, processFct, currentPath, results);
            }
        }
    }

    private static <R> R processNode(Node node, Function<String, R> processFct) {
        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
                return processFct.apply(node.getNodeName());
            case Node.ATTRIBUTE_NODE:
            case Node.TEXT_NODE:
                return processFct.apply(node.getNodeValue());
        }
        return null;
    }

    private static String getNodePath(Node node, String parentPath) {
        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
                if (isListElement(node)) {
                    return parentPath + "/" + node.getNodeName() + "[" + findNodePositionAmongListElements(node) + "]";
                } else {
                    return parentPath + "/" + node.getNodeName();
                }
            case Node.ATTRIBUTE_NODE:
                return parentPath + "{" + node.getNodeName() + "}";
            default:
                return parentPath;
        }
    }

    private static boolean isListElement(Node node) {
        Node prevNode = node.getPreviousSibling();
        Node nextNode = node.getNextSibling();
        while (prevNode != null) {
            if (prevNode.getNodeType() == Node.ELEMENT_NODE && prevNode.getNodeName().equals(node.getNodeName())) {
                return true;
            }
            prevNode = prevNode.getPreviousSibling();
        }
        while (nextNode != null) {
            if (nextNode.getNodeType() == Node.ELEMENT_NODE && nextNode.getNodeName().equals(node.getNodeName())) {
                return true;
            }
            nextNode = nextNode.getNextSibling();
        }
        return false;
    }

    private static int findNodePositionAmongListElements(Node node) {
        int i = 1;
        Node prevNode = node.getPreviousSibling();
        while (prevNode != null) {
            if (prevNode.getNodeType() == Node.ELEMENT_NODE && prevNode.getNodeName().equals(node.getNodeName())) {
                i++;
            }
            prevNode = prevNode.getPreviousSibling();
        }
        return i;
    }
}