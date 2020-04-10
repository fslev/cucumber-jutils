package com.cucumber.utils.engineering.utils;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Function;

class Xml {

    static <R> void walkAndProcessNode(Node node, Function<String, R> processFct, String parentPath, Map<String, R> results) {
        String currentPath = getNodePath(node, parentPath);
        Map.Entry<String, R> result = processNode(currentPath, node, processFct);
        if (result != null && result.getValue() != null) {
            results.put(result.getKey(), result.getValue());
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

    /**
     * @param path
     * @param node
     * @param processFct
     * @param <R>
     * @return a simple mapping between processed node result and its corresponding path
     */
    private static <R> Map.Entry<String, R> processNode(String path, Node node, Function<String, R> processFct) {
        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
                return new AbstractMap.SimpleEntry<>(path, processFct.apply(node.getNodeName()));
            case Node.ATTRIBUTE_NODE:
                return new AbstractMap.SimpleEntry<>(path + "{attr:" + node.getNodeName() + "}", processFct.apply(node.getNodeValue()));
            case Node.TEXT_NODE:
                return new AbstractMap.SimpleEntry<>(path + "/{val}", processFct.apply(node.getNodeValue()));
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
