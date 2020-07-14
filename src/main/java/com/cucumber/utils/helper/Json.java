package com.cucumber.utils.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Function;

public class Json {


    static <R> void walkAndProcessJson(JsonNode jsonNode, Function<String, R> processFunction, String parentPath, Map<String, R> results) {
        String pathPrefix = parentPath.isEmpty() ? "" : parentPath + "/";
        if (jsonNode.isObject()) {
            jsonNode.fields().forEachRemaining(field -> {
                String currentPath = pathPrefix + field.getKey();
                addNewResultToMap(results, processKey(currentPath, field.getKey(), processFunction));
                walkAndProcessJson(field.getValue(), processFunction, currentPath, results);
            });
        } else if (jsonNode.isArray()) {
            for (int i = 0; i < jsonNode.size(); i++) {
                walkAndProcessJson(jsonNode.get(i), processFunction, parentPath + "[" + (i + 1) + "]", results);
            }
        } else if (jsonNode.getNodeType().equals(JsonNodeType.STRING)) {
            addNewResultToMap(results, processValue(parentPath, jsonNode, processFunction));
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
