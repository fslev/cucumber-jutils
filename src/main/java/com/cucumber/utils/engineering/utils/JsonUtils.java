package com.cucumber.utils.engineering.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import org.apache.commons.lang3.tuple.Triple;
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
        JsonNode js = mapper.readTree("{\"status\":\"20*0\",\"fiel*d\":{\"autoRenew\":false,\"an|therField\":\"andot^erkey\",\"jsonArrayKey\":[\"^dbsjdb\",\"ccc.das\",\"c.aaa\"]}}");
        Map<String, Map<String, String>> finalMap = new HashMap<>();
        List<Triple<String, String, String>> tripleList = new ArrayList<>();
        addKeys("", js, tripleList, new ArrayList<>());
        tripleList.forEach(System.out::println);
        checkRegexExists(tripleList);

    }

    private void addKeys(String currentPath, JsonNode jsonNode, List<Triple<String, String, String>> tripleList, List<Integer> suffix) {
        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
            String pathPrefix = currentPath.isEmpty() ? "" : currentPath + "->";
            while (iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                if (entry.getValue().isObject()) {
                    tripleList.add(Triple.of(entry.getKey(), "", currentPath));
                }
                if (entry.getValue().isArray()) {
                    tripleList.add(Triple.of(entry.getKey(), "", currentPath));
                }
                addKeys(pathPrefix + entry.getKey(), entry.getValue(), tripleList, suffix);
            }

        } else if (jsonNode.isArray()) {
            System.out.println(jsonNode + "System.out.println -> jsonnode");
            System.out.println(currentPath + "currentPath");
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                suffix.add(i + 1);
                addKeys(currentPath, arrayNode.get(i), tripleList, suffix);
            }

        } else if (jsonNode.isValueNode()) {
            System.out.println("currentPath " + currentPath);
            if (currentPath.contains("->")) {
                StringBuilder currentPathBuilder = new StringBuilder(currentPath);
                for (Integer integer : suffix) {
                    currentPathBuilder.append("->").append(integer);
                }
                currentPath = currentPathBuilder.toString();
            }
            ValueNode valueNode = (ValueNode) jsonNode;
            tripleList.add(Triple.of(currentPath.split("->")[currentPath.split("->").length - 1],
                    valueNode.asText(), currentPath));
        }
    }

    public void checkRegexExists(List<Triple<String, String, String>> list) {

        list.forEach(triple -> {
            List<String> regexFromKeys = RegexUtils.getRegexCharsFromString(triple.getLeft());
            List<String> regexFromValue = RegexUtils.getRegexCharsFromString(triple.getMiddle());

            if (!regexFromKeys.isEmpty())
                log.warn("Regex " + regexFromKeys + " was found for key " + triple.getLeft()
                        + " at key-path: " +
                        (triple.getRight().equals("") ? triple.getLeft() : triple.getRight()));
            if (!regexFromValue.isEmpty())
                log.warn("Regex " + regexFromValue + " was found for value " + triple.getMiddle() + " at key-path: " + triple.getRight());
        });
    }


    @Test
    public void test() throws JsonProcessingException {
        jsonParser();
    }


}
