package com.cucumber.utils.engineering.utils;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Map;
import java.util.function.Function;

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
