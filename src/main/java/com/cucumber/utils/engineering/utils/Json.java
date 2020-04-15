package com.cucumber.utils.engineering.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

public class Json {


    static <R> void walkAndProcessJson(JsonNode jsonNode, Function<String, R> processFunction, String parentPath, Map<String, R> results) throws JsonProcessingException {
        Map.Entry<String, R> result = null;
        if (jsonNode.isObject()) {
            result = processValue(parentPath, jsonNode, processFunction);
            addNewResultToMap(results, result);
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
            String pathPrefix = parentPath.isEmpty() ? "" : parentPath + "/";
            while (iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                result = processKey(pathPrefix + entry.getKey(), entry.getKey(), processFunction);
                addNewResultToMap(results, result);
                walkAndProcessJson(entry.getValue(), processFunction, pathPrefix + entry.getKey(), results);
            }

        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                walkAndProcessJson(jsonNode.get(i), processFunction, parentPath + "[" + i + "]", results);
            }
        } else if (jsonNode.getNodeType().equals(JsonNodeType.STRING)) {
            result = processValue(parentPath, jsonNode, processFunction);
            addNewResultToMap(results, result);
        }
    }

    private static <R> void addNewResultToMap(Map<String, R> results, Map.Entry<String, R> result) {
        if (result != null && result.getValue() != null) {
            results.put(result.getKey(), result.getValue());
        }
    }

    private static <R> Map.Entry<String, R> processValue(String path, JsonNode node, Function<String, R> processFct) {
        return new AbstractMap.SimpleEntry<>(path + "/{value}", processFct.apply(node.toString()));
    }

    private static <R> Map.Entry<String, R> processKey(String path, String key, Function<String, R> processFct) {
        return new AbstractMap.SimpleEntry<>(path + "/{key}", processFct.apply(key));
    }
}
