//package com.cucumber.utils.context.config;
//
//import com.cucumber.utils.clients.http.Method;
//import com.cucumber.utils.context.props.ScenarioProps;
//import com.cucumber.utils.context.props.ScenarioPropsParser;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.cucumber.core.api.TypeRegistry;
//import io.cucumber.core.api.TypeRegistryConfigurer;
//import io.cucumber.cucumberexpressions.ParameterByTypeTransformer;
//import io.cucumber.cucumberexpressions.ParameterType;
//import io.cucumber.datatable.DataTable;
//import io.cucumber.datatable.DataTableType;
//import io.cucumber.datatable.TableTransformer;
//
//import java.io.IOException;
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import static java.util.Locale.ENGLISH;
//
//public class TypeRegistryConfiguration implements TypeRegistryConfigurer {
//
//    @Override
//    public Locale locale() {
//        return ENGLISH;
//    }
//
//    @Override
//    public void configureTypeRegistry(TypeRegistry typeRegistry) {
//        typeRegistry.defineParameterType(ParameterType.fromEnum(Method.class));
//        // Custom data table type
//        typeRegistry.defineDataTableType(
//                new DataTableType(List.class, new ScenarioPropsDataTableTransformer()));
//        ScenarioPropsParameterTransformer scenarioPropsTransformer = new ScenarioPropsParameterTransformer();
//        typeRegistry.setDefaultParameterTransformer(scenarioPropsTransformer);
//        // Needed especially for doc strings
//        typeRegistry.defineDataTableType(new DataTableType(String.class,
//                (DataTable dataTable) -> {
//                    ScenarioProps scenarioProps = InjectorByThreadSource.getInjector(Thread.currentThread().getId()).getInstance(ScenarioProps.class);
//                    return new ScenarioPropsParser(scenarioProps, dataTable.cell(0, 0).trim()).result().toString();
//                }));
//    }
//
//
//    private class ScenarioPropsParameterTransformer implements ParameterByTypeTransformer {
//        private ObjectMapper objectMapper = new ObjectMapper()
//                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
//                .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
//
//        @Override
//        public Object transform(String s, Type type) {
//            ScenarioProps scenarioProps = InjectorByThreadSource.getInjector(Thread.currentThread().getId()).getInstance(ScenarioProps.class);
//            Object parsedValue = new ScenarioPropsParser(scenarioProps, s.trim()).result();
//            try {
//                return objectMapper.readValue(parsedValue.toString(), objectMapper.constructType(type));
//                // if json string cannot be converted to object then proceed to simple value conversion
//            } catch (IOException e) {
//                return objectMapper.convertValue(parsedValue, objectMapper.constructType(type));
//            }
//        }
//    }
//
//    private class ScenarioPropsDataTableTransformer implements TableTransformer {
//        @Override
//        public List transform(DataTable dataTable) {
//            ScenarioProps scenarioProps = InjectorByThreadSource.getInjector(Thread.currentThread().getId()).getInstance(ScenarioProps.class);
//            List<Map<String, String>> list = new ArrayList<>();
//            dataTable.asMaps().forEach(map ->
//                    list.add(map.entrySet().stream().collect(Collectors.toMap(
//                            e -> new ScenarioPropsParser(scenarioProps, e.getKey()).result().toString(),
//                            e -> new ScenarioPropsParser(scenarioProps, e.getValue()).result().toString()))));
//            return list;
//        }
//    }
//}