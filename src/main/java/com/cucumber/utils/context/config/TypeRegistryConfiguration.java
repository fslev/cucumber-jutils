package com.cucumber.utils.context.config;

import com.cucumber.utils.clients.http.Method;
import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.context.props.ScenarioPropsParser;
import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.cucumberexpressions.Transformer;
import io.cucumber.datatable.DataTable;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableTransformer;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Locale.ENGLISH;

public class TypeRegistryConfiguration implements TypeRegistryConfigurer {

    private static final List<String> CSTRING_REGEXPS =
            Collections.singletonList(Pattern.compile(".*").pattern());
    private static final String CUSTOM_STRING = "cstring";

    @Override
    public Locale locale() {
        return ENGLISH;
    }

    @Override
    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        typeRegistry.defineParameterType(ParameterType.fromEnum(Method.class));
        typeRegistry.defineParameterType(new ParameterType<>("cstring", CSTRING_REGEXPS,
                Object.class, new SymbolsTransformer()));

        // Custom data table type
        typeRegistry.defineDataTableType(
                new DataTableType(List.class, new PlaceholdersDataTableTransformer()));
        // Needed especially for doc strings
        typeRegistry.defineDataTableType(new DataTableType(String.class,
                (DataTable dataTable) -> {
                    ScenarioProps scenarioProps = InjectorByThreadSource.getInjector(Thread.currentThread().getId()).getInstance(ScenarioProps.class);
                    return new ScenarioPropsParser(scenarioProps, dataTable.cell(0, 0).trim()).result().toString();
                }));

    }

    private class SymbolsTransformer implements Transformer<Object> {
        @Override
        public Object transform(String s) {
            ScenarioProps scenarioProps = InjectorByThreadSource.getInjector(Thread.currentThread().getId()).getInstance(ScenarioProps.class);
            if (s == null) {
                return null;
            }
            return new ScenarioPropsParser(scenarioProps, s.trim()).result();
        }
    }

    private class PlaceholdersDataTableTransformer implements TableTransformer {
        @Override
        public List transform(DataTable dataTable) {
            ScenarioProps scenarioProps = InjectorByThreadSource.getInjector(Thread.currentThread().getId()).getInstance(ScenarioProps.class);
            List<Map<String, String>> list = new ArrayList<>();
            dataTable.asMaps().forEach(map -> {
                list.add(map.entrySet().stream().collect(Collectors.toMap(
                        e -> new ScenarioPropsParser(scenarioProps, e.getKey()).result().toString(),
                        e -> new ScenarioPropsParser(scenarioProps, e.getValue()).result().toString())));
            });
            return !list.isEmpty() ? list : dataTable.asList().stream()
                    .map(el -> new ScenarioPropsParser(scenarioProps, el).result().toString())
                    .collect(Collectors.toList());
        }
    }
}
