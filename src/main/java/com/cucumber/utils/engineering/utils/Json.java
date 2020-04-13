package com.cucumber.utils.engineering.utils;

import com.cucumber.utils.engineering.compare.JsonCompare;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

public class Json {
    static ObjectMapper mapper = new ObjectMapper();

    static <R> void walkAndProcessJson(JsonNode jsonNode, Function<String, R> processFunction, String parentPath, Map<String, R> results) throws JsonProcessingException {
        Map.Entry<String, R> result = null;
        if (jsonNode.isObject()) {
            result = processNode(parentPath, jsonNode, processFunction);
            if (result != null && result.getValue() != null) {
                results.put(result.getKey(), result.getValue());
            }
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
            String pathPrefix = parentPath.isEmpty() ? "" : parentPath + "/";
            while (iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                walkAndProcessJson(entry.getValue(), processFunction, pathPrefix + entry.getKey(), results);
            }

        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                walkAndProcessJson(jsonNode.get(i), processFunction, parentPath + "[" + i + "]", results);
            }
        } else if (jsonNode.isValueNode()) {
            result = processNode(parentPath, jsonNode, processFunction);
        }
        System.out.println("result + " + result);
    }


    private static <R> Map.Entry<String, R> processNode(String path, JsonNode node, Function<String, R> processFct) {
        switch (node.getNodeType()) {
            case OBJECT: {
                Iterator<Map.Entry<String, JsonNode>> iter = node.fields();
                Map.Entry<String, JsonNode> entry = iter.next();
                String key = entry.getKey();
                return new AbstractMap.SimpleEntry<>(path + "/{key:" + node + "}",
                        processFct.apply(node.toString()));
            }
            case STRING: {
                return new AbstractMap.SimpleEntry<>(path + "/{value}", processFct.apply(node.toString()));
            }
        }
        return null;
    }

    @Test
    public <R> void test() throws JsonProcessingException {
        //String js = "{\"status\":\"20.0\",\"arr?ay\":[\"fistElement*\", \"secondElement^\"]}";
        String js = "{\"a*1\":\"a.2\",\"b1|\":\"b2?\"}";
        JsonCompare compare = new JsonCompare();
        compare.checkJsonContainsSpecialRegexCharsAndWarn(js);

    }
}
