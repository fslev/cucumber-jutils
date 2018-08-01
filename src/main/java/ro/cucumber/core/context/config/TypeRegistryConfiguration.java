package ro.cucumber.core.context.config;

import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.cucumberexpressions.Transformer;
import io.cucumber.datatable.DataTable;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableTransformer;
import ro.cucumber.core.clients.http.HttpVerb;
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

    private static final List<String> HTTP_VERB_REGEXPS = Collections
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
                new ParameterType<>("httpVerb", HTTP_VERB_REGEXPS, HttpVerb.class, (String s) -> {
                    switch (s) {
                        case "GET":
                            return HttpVerb.GET;
                        case "POST":
                            return HttpVerb.POST;
                        case "PUT":
                            return HttpVerb.PUT;
                        case "DELETE":
                            return HttpVerb.DELETE;
                        case "HEAD":
                            return HttpVerb.HEAD;
                        case "OPTIONS":
                            return HttpVerb.OPTIONS;
                        case "TRACE":
                            return HttpVerb.TRACE;
                        default:
                            return null;
                    }
                }));

        // special string type for parsing scenario and global symbols
        typeRegistry.defineParameterType(new ParameterType<>(CUSTOM_STRING, CSTRING_REGEXPS,
                Object.class, new SymbolsTransformer()));

        // DataTable cell (0,0) is assigned to a String
        // Needed for doc strings
        typeRegistry.defineDataTableType(new DataTableType(String.class,
                (DataTable dataTable) -> (dataTable.cell(0, 0).trim())));

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
