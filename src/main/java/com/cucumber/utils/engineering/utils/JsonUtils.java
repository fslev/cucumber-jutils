package com.cucumber.utils.engineering.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class JsonUtils {

    private static Logger log = LogManager.getLogger();

    public static JsonNode toJson(String content) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
        return mapper.readTree(content);
    }

    public static String prettyPrint(String content) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(toJson(content));
        } catch (IOException e) {
            log.debug("Cannot prettify JSON: {}", e.getMessage());
            return content;
        }
    }

    public void jsonParser() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode js = mapper.readTree("{\"status\": \"20*0\",\"Carm*en\": {\"autoRenew\": false,\"an|therField\": \"andot^erkey\"}}");
        Map<String, String> finalMap = new HashMap<>();
        addKeys("", js, finalMap, new ArrayList<>());
        finalMap.entrySet()
                .forEach(System.out::println);
        checkRegexExists(finalMap);

    }

    private void addKeys(String currentPath, JsonNode jsonNode, Map<String, String> mapToAdd, List<Integer> suffix) {
        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
            String pathPrefix = currentPath.isEmpty() ? "" : currentPath + "-";
            while (iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                if (entry.getValue().isObject()) {
                    mapToAdd.put(entry.getKey(), "");
                }
                addKeys(pathPrefix + entry.getKey(), entry.getValue(), mapToAdd, suffix);
            }

        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;

            for (int i = 0; i < arrayNode.size(); i++) {
                suffix.add(i + 1);
                addKeys(currentPath, arrayNode.get(i), mapToAdd, suffix);

                if (i + 1 < arrayNode.size()) {
                    suffix.remove(arrayNode.size() - 1);
                }
            }

        } else if (jsonNode.isValueNode()) {
            if (currentPath.contains("-")) {
                for (int i = 0; i < suffix.size(); i++) {
                    currentPath += "-" + suffix.get(i);
                }
            }
            ValueNode valueNode = (ValueNode) jsonNode;
            mapToAdd.put(currentPath, valueNode.asText());
        }
    }

    public void checkRegexExists(Map<String, String> finalMap) {

        finalMap.forEach((key, value) -> {
            List<String> regexFromKeys = RegexUtils.getRegexCharsFromString(key);
            List<String> regexFromValue = RegexUtils.getRegexCharsFromString(value);

            if (!regexFromKeys.isEmpty())
                log.warn("Regex " + regexFromKeys + " was found for key " + key + "at key-path: " + key);
            if (!regexFromValue.isEmpty())
                log.warn("Regex " + regexFromValue + " was found for value " + value + " at key-path: " + key);
        });
    }


    @Test
    public void test() throws JsonProcessingException {
        jsonParser();
    }


}
