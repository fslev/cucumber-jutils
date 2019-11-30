package com.cucumber.utils.context.config;

import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.context.props.ScenarioPropsParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.DefaultDataTableCellTransformer;
import io.cucumber.java.DefaultDataTableEntryTransformer;
import io.cucumber.java.DefaultParameterTransformer;
import io.cucumber.java.DocStringType;

import java.io.IOException;
import java.lang.reflect.Type;

@ScenarioScoped
public class ParameterTypesConfig {

    @Inject
    private ScenarioProps scenarioProps;

    private final ObjectMapper objectMapper = new ObjectMapper();

    {
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
    }


    @DefaultParameterTransformer
    @DefaultDataTableEntryTransformer(headersToProperties = true)
    @DefaultDataTableCellTransformer
    public Object defaultTransformer(Object fromValue, Type toValueType) {
        System.out.println(fromValue.toString());
        Object parsedValue = new ScenarioPropsParser(scenarioProps, fromValue.toString()).result();
        System.out.println(scenarioProps);
        System.out.println(parsedValue);
        try {
            return objectMapper.readValue(parsedValue.toString(), objectMapper.constructType(toValueType));
            // if json string cannot be converted to object then proceed to simple value conversion
        } catch (IOException e) {
            return objectMapper.convertValue(parsedValue, objectMapper.constructType(toValueType));
        }
    }

    @DocStringType
    public StringBuilder convertDocString(String docString) throws IOException {
        return new StringBuilder(new ScenarioPropsParser(scenarioProps, docString).result().toString());
    }
}
