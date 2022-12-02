package com.cucumber.utils.context.config;

import com.cucumber.utils.context.vars.ScenarioVars;
import com.cucumber.utils.context.vars.ScenarioVarsParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.DataTableType;
import io.cucumber.java.DefaultDataTableCellTransformer;
import io.cucumber.java.DefaultDataTableEntryTransformer;
import io.cucumber.java.DefaultParameterTransformer;
import io.cucumber.java.DocStringType;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@ScenarioScoped
public class ParameterTypesConfig {

    private static final String EMPTY_STRING = "[_blank]";
    private static final String NULL_STRING = "[_null]";

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, DeserializationFeature.FAIL_ON_TRAILING_TOKENS);

    @Inject
    private ScenarioVars scenarioVars;

    @DefaultParameterTransformer
    @DefaultDataTableEntryTransformer(headersToProperties = true, replaceWithEmptyString = EMPTY_STRING)
    @DefaultDataTableCellTransformer(replaceWithEmptyString = EMPTY_STRING)
    public Object defaultTransformer(Object fromValue, Type toValueType) {
        if (fromValue == null || fromValue.equals(NULL_STRING)) {
            return null;
        }
        Object parsedValue = ScenarioVarsParser.parse(fromValue.toString(), scenarioVars);
        if (parsedValue != null && !toValueType.equals(Object.class) && !toValueType.equals(parsedValue.getClass())) {
            if (parsedValue instanceof String) {
                try {
                    return MAPPER.readValue(parsedValue.toString(), MAPPER.constructType(toValueType));
                } catch (IOException ignored) {
                }
            }
            return MAPPER.convertValue(parsedValue, MAPPER.constructType(toValueType));
        }
        return parsedValue;
    }

    @DocStringType
    public StringBuilder convertDocString(String docString) {
        return new StringBuilder(ScenarioVarsParser.parse(docString, scenarioVars).toString());
    }

    @DataTableType(replaceWithEmptyString = EMPTY_STRING)
    public Map<String, Object> convertDataTable(Map<String, String> tableEntry) {
        Map<String, Object> transformedMap = new HashMap<>();
        for (Map.Entry<String, String> e : tableEntry.entrySet()) {
            transformedMap.put(
                    e.getKey() != null ? ScenarioVarsParser.parse(e.getKey(), scenarioVars).toString() : null,
                    e.getValue() != null ? ScenarioVarsParser.parse(e.getValue(), scenarioVars) : null);
        }
        return transformedMap;
    }
}