package ro.cucumber.core.context.config;

import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.cucumberexpressions.Transformer;
import io.cucumber.datatable.DataTable;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableTransformer;
import ro.cucumber.core.clients.http.HttpVerb;
import ro.cucumber.core.context.props.PlaceholderFill;

import java.util.*;
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

        typeRegistry.defineParameterType(new ParameterType<>(CUSTOM_STRING, CSTRING_REGEXPS,
                Object.class, new FillPlaceholdersTransformer()));

        // DataTable cell (0,0) is assigned to a String
        // Works also for doc strings
        typeRegistry.defineDataTableType(new DataTableType(String.class,
                (DataTable dataTable) -> (dataTable.cell(0, 0).trim())));

        typeRegistry.defineDataTableType(new DataTableType(CustomDataTable.class,
                (TableTransformer<CustomDataTable>) dataTable -> {
                    List<Map<String, String>> list = new ArrayList<>();
                    List<Map<String, String>> listData = dataTable.asMaps();
                    listData.forEach((Map<String, String> mapData) -> {
                        Map<String, String> map = new HashMap<>();
                        mapData.forEach((k, v) -> map.put(k, new PlaceholderFill(v).getResult().toString()));
                        list.add(map);
                    });
                    return new CustomDataTable(list);
                }));
    }

    private static class FillPlaceholdersTransformer implements Transformer<Object> {

        @Override
        public Object transform(String s) {
            if (s == null) {
                return null;
            }
            Object result = new PlaceholderFill(s.trim()).getResult();
            return result;
        }
    }
}
