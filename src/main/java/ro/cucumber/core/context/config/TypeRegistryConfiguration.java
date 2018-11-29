package ro.cucumber.core.context.config;

import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.cucumberexpressions.Transformer;
import io.cucumber.datatable.DataTable;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableTransformer;
import ro.cucumber.core.clients.http.Method;
import ro.cucumber.core.context.props.SymbolsParser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import static java.util.Locale.ENGLISH;

public class TypeRegistryConfiguration implements TypeRegistryConfigurer {

    private static final List<String> HTTP_METHOD_REGEXPS = Collections
            .singletonList(Pattern.compile("(GET|POST|PUT|DELETE|OPTIONS|HEAD|TRACE)").pattern());

    private static final List<String> CSTRING_REGEXPS =
            Collections.singletonList(Pattern.compile("\\s*.+").pattern());

    private static final String CUSTOM_STRING = "cstring";

    @Override
    public Locale locale() {
        return ENGLISH;
    }

    @Override
    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        typeRegistry.defineParameterType(
                new ParameterType<>("httpMethod", HTTP_METHOD_REGEXPS, Method.class, (String s) -> {
                    switch (s) {
                        case "GET":
                            return Method.GET;
                        case "POST":
                            return Method.POST;
                        case "PUT":
                            return Method.PUT;
                        case "DELETE":
                            return Method.DELETE;
                        case "HEAD":
                            return Method.HEAD;
                        case "OPTIONS":
                            return Method.OPTIONS;
                        case "TRACE":
                            return Method.TRACE;
                        default:
                            return null;
                    }
                }));

        // special string type for parsing scenario and global placeholders
        typeRegistry.defineParameterType(new ParameterType<>(CUSTOM_STRING, CSTRING_REGEXPS,
                Object.class, new SymbolsTransformer()));

        // DataTable cell (0,0) is assigned to a String
        // Needed for doc strings
        typeRegistry.defineDataTableType(new DataTableType(String.class,
                (DataTable dataTable) -> (new SymbolsParser(dataTable.cell(0, 0).trim())).parse()
                        .toString()));

        // Custom data table type
        typeRegistry.defineDataTableType(
                new DataTableType(CustomDataTable.class, new CustomDataTableTransformer()));
    }

    private static class SymbolsTransformer implements Transformer<Object> {
        @Override
        public Object transform(String s) {
            if (s == null) {
                return null;
            }
            Object result = new SymbolsParser(s.trim()).parse();
            return result;
        }
    }

    private static class CustomDataTableTransformer implements TableTransformer<CustomDataTable> {
        @Override
        public CustomDataTable transform(DataTable dataTable) {
            List list = new ArrayList<>();
            List<Map<String, String>> mapsWithDataList = dataTable.asMaps();
            if (!mapsWithDataList.isEmpty()) {
                mapsWithDataList.forEach((Map<String, String> mapData) -> {
                    Map<String, String> map = new HashMap<>();
                    mapData.forEach((k, v) -> map.put(k, new SymbolsParser(v).parse().toString()));
                    list.add(map);
                });
            } else {
                List<String> dataList = dataTable.asList();
                dataList.forEach((String el) -> list.add(new SymbolsParser(el).parse().toString()));
            }
            return new CustomDataTable(list);
        }
    }
}
