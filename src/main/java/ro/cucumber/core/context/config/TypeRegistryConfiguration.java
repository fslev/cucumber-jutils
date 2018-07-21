package ro.cucumber.core.context.config;

import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.cucumberexpressions.Transformer;
import io.cucumber.datatable.DataTable;
import io.cucumber.datatable.DataTableType;
import ro.cucumber.core.clients.http.HttpVerb;
import ro.cucumber.core.context.props.SymbolParser;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import static java.util.Locale.ENGLISH;

public class TypeRegistryConfiguration implements TypeRegistryConfigurer {

    private static final List<String> HTTP_VERB_REGEXPS = Collections
            .singletonList(Pattern.compile("(GET|POST|PUT|DELETE|OPTIONS|HEAD|TRACE)").pattern());

    private static final List<String> CSTRING_REGEXPS = Collections.singletonList(
            Pattern.compile(".+")
                    .pattern());

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

        typeRegistry.defineParameterType(new ParameterType<>(CUSTOM_STRING,
                CSTRING_REGEXPS, String.class, new CustomStringTransformer()));

        // DataTable cell (0,0) is assigned to a String
        // Works also for doc strings
        typeRegistry.defineDataTableType(new DataTableType(String.class, (DataTable dataTable) -> (
                dataTable.cell(0, 0)))
        );
    }

    private static class CustomStringTransformer implements Transformer<String> {

        @Override
        public String transform(String s) {
            if (s == null) {
                return null;
            }
            return SymbolParser.parse(s);
        }
    }
}
