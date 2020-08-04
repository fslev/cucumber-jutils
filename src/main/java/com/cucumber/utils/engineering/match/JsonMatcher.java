package com.cucumber.utils.engineering.match;

import com.cucumber.utils.engineering.match.comparators.json.CustomJsonComparator;
import com.cucumber.utils.engineering.match.condition.MatchCondition;
import com.cucumber.utils.engineering.utils.RegexUtils;
import com.cucumber.utils.exceptions.InvalidTypeException;
import com.cucumber.utils.helper.JsonUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import ro.skyah.comparator.CompareMode;
import ro.skyah.comparator.JSONCompare;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.fail;

public class JsonMatcher extends AbstractMatcher<JsonNode> {
    private final CustomJsonComparator comparator;

    public JsonMatcher(String message, Object expected, Object actual, Set<MatchCondition> matchConditions) throws InvalidTypeException {
        super(message, expected, actual, matchConditions);
        this.comparator = new CustomJsonComparator(matchConditions);
    }

    @Override
    protected JsonNode convert(Object value) throws InvalidTypeException {
        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
        try {
            JsonNode jsonNode = value instanceof String ? mapper.readTree((String) value) : mapper.convertValue(value, JsonNode.class);
            if (!jsonNode.getNodeType().equals(JsonNodeType.OBJECT) && !jsonNode.getNodeType().equals(JsonNodeType.ARRAY)) {
                throw new InvalidTypeException("Malformed JSON");
            }
            return jsonNode;
        } catch (Exception e) {
            throw new InvalidTypeException("Invalid JSON NODE");
        }

    }

    @Override
    public Map<String, Object> match() {
        if (matchConditions.contains(MatchCondition.DO_NOT_MATCH)) {
            matchConditions.remove(MatchCondition.DO_NOT_MATCH);
            try {
                positiveMatch();
            } catch (AssertionError e) {
                return new HashMap<>();
            }
            fail(negativeMatchMessage);
        }
        return positiveMatch();
    }

    private Map<String, Object> positiveMatch() {
        try {
            JSONCompare.assertEquals(message, expected, actual, comparator, jsonCompareModes());
        } catch (AssertionError e) {
            if (!comparator.getFieldProperties().isEmpty()) {
                while (true) {
                    comparator.getDepletedFieldPropertyList().add(new HashMap<>(comparator.getFieldProperties()));
                    comparator.getFieldProperties().clear();
                    try {
                        JSONCompare.assertEquals(message, expected, actual, comparator, jsonCompareModes());
                    } catch (AssertionError e1) {
                        if (!comparator.getFieldProperties().isEmpty()) {
                            continue;
                        }
                        throw e1;
                    }
                    break;
                }
            } else {
                debugIfJsonContainsUnintentionalRegexChars(expected.toString());
                throw e;
            }
        }
        return comparator.getValueProperties();
    }

    private CompareMode[] jsonCompareModes() {
        Set<CompareMode> jsonCompareModes = new HashSet<>();
        for (MatchCondition condition : matchConditions) {
            if (MatchCondition.JSON_NON_EXTENSIBLE_OBJECT.equals(condition)) {
                jsonCompareModes.add(CompareMode.JSON_OBJECT_NON_EXTENSIBLE);
            } else if (MatchCondition.JSON_NON_EXTENSIBLE_ARRAY.equals(condition)) {
                jsonCompareModes.add(CompareMode.JSON_ARRAY_NON_EXTENSIBLE);
            } else if (MatchCondition.JSON_STRICT_ORDER_ARRAY.equals(condition)) {
                jsonCompareModes.add(CompareMode.JSON_ARRAY_STRICT_ORDER);
            }
        }
        return jsonCompareModes.toArray(new CompareMode[0]);
    }

    private static void debugIfJsonContainsUnintentionalRegexChars(String json) {
        if (!LOG.isDebugEnabled()) {
            return;
        }
        try {
            Map<String, List<String>> specialRegexChars = JsonUtils.walkJsonAndProcessNodes(json, nodeValue -> {
                List<String> regexChars = RegexUtils.getRegexCharsFromString(nodeValue);
                return regexChars.isEmpty() ? null : regexChars;
            });
            if (!specialRegexChars.isEmpty()) {
                String prettyResult = specialRegexChars.entrySet().stream().map(e -> e.getKey() + " contains: " + e.getValue().toString())
                        .collect(Collectors.joining("\n"));
                LOG.debug(" \n\n Comparison mechanism failed while comparing JSONs." +
                                " \n One reason for this, might be that Json may have unintentional regex special characters. " +
                                "\n If so, try to quote them by using \\Q and \\E or simply \\" +
                                "\n Found the following list of special regex characters inside expected:\n\n{}\n\nExpected:\n{}\n",
                        prettyResult, json);
            }
        } catch (Exception e) {
            LOG.debug("Cannot extract special regex characters from json", e);
        }
    }
}
