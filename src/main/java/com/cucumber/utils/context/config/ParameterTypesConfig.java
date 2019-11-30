package com.cucumber.utils.context.config;

import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.context.props.ScenarioPropsParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import io.cucumber.datatable.DataTable;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        Object parsedValue = new ScenarioPropsParser(scenarioProps, fromValue.toString()).result();
        try {
            return objectMapper.readValue(parsedValue.toString(), objectMapper.constructType(toValueType));
            // if json string cannot be converted to object then proceed to simple value conversion
        } catch (IOException e) {
            return objectMapper.convertValue(parsedValue, objectMapper.constructType(toValueType));
        }
    }

    @DocStringType
    public StringBuilder convertDocString(String docString) {
        return new StringBuilder(new ScenarioPropsParser(scenarioProps, docString).result().toString());
    }

    @DataTableType
    public List convertDataTable(DataTable dataTable) {
        List<Map<String, String>> list = new ArrayList<>();
        dataTable.asMaps().forEach(map ->
                list.add(map.entrySet().stream().collect(Collectors.toMap(
                        e -> new ScenarioPropsParser(scenarioProps, e.getKey()).result().toString(),
                        e -> new ScenarioPropsParser(scenarioProps, e.getValue()).result().toString()))));
        return list;
    }
}
