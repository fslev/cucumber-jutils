package com.cucumber.utils.engineering.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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