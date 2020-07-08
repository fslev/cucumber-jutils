package com.cucumber.utils.context.config;

import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.context.props.ScenarioPropsParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@ScenarioScoped
public class ParameterTypesConfig {

    private static final String EMPTY_STRING = "[_blank]";
    private static final String NULL_STRING = "[_null]";

    @Inject
    private ScenarioProps scenarioProps;
    private final ObjectMapper objectMapper = new ObjectMapper();

    {
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
    }

    @DefaultParameterTransformer
    @DefaultDataTableEntryTransformer(headersToProperties = true, replaceWithEmptyString = EMPTY_STRING)
    @DefaultDataTableCellTransformer(replaceWithEmptyString = EMPTY_STRING)
    public Object defaultTransformer(Object fromValue, Type toValueType) {
        if (fromValue == null || fromValue.equals(NULL_STRING)) {
            return null;
        }
        Object parsedValue = ScenarioPropsParser.parse(fromValue.toString(), scenarioProps);
        if (parsedValue == null || toValueType.equals(Object.class)) {
            return parsedValue;
        }
        try {
            return objectMapper.readValue(parsedValue.toString(), objectMapper.constructType(toValueType));
            // if json string cannot be converted to object then proceed to simple value conversion
        } catch (IOException e) {
            return objectMapper.convertValue(parsedValue, objectMapper.constructType(toValueType));
        }
    }

    @DocStringType
    public StringBuilder convertDocString(String docString) {
        return new StringBuilder(ScenarioPropsParser.parse(docString, scenarioProps).toString());
    }

    @DataTableType(replaceWithEmptyString = EMPTY_STRING)
    public Map<String, Object> convertDataTable(Map<String, String> tableEntry) {
        Map<String, Object> transformedMap = new HashMap<>();
        for (Map.Entry<String, String> e : tableEntry.entrySet()) {
            transformedMap.put(
                    e.getKey() != null ? ScenarioPropsParser.parse(e.getKey(), scenarioProps).toString() : null,
                    e.getValue() != null ? ScenarioPropsParser.parse(e.getValue(), scenarioProps) : null);
        }
        return transformedMap;
    }
}