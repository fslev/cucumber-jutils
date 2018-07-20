package ro.cucumber.core.context.config;

import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.cucumberexpressions.Transformer;
import ro.cucumber.core.clients.http.HttpVerb;
import ro.cucumber.core.context.GlobalProps;
import ro.cucumber.core.context.ScenarioProps;
import ro.cucumber.core.engineering.symbols.GlobalSymbolReplaceParser;
import ro.cucumber.core.engineering.symbols.ScenarioSymbolReplaceParser;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import static java.util.Locale.ENGLISH;

public class TypeRegistryConfiguration implements TypeRegistryConfigurer {

    private static final List<String> HTTP_VERB_REGEXPS = Collections
            .singletonList(Pattern.compile("(GET|POST|PUT|DELETE|OPTIONS|HEAD|TRACE)").pattern());

    private static final List<String> STRING_REGEXPS = Collections.singletonList(
            Pattern.compile("\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\"|'([^'\\\\]*(\\\\.[^'\\\\]*)*)'")
                    .pattern());

    private static final String STRING_WITH_SYMBOLS_PARAM_NAME = "symString";

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

        typeRegistry.defineParameterType(new ParameterType<>(STRING_WITH_SYMBOLS_PARAM_NAME,
                STRING_REGEXPS, String.class, new StringWithPropertiesTransformer()));
    }

    private static class StringWithPropertiesTransformer implements Transformer<String> {

        @Override
        public String transform(String s) {
            if (s == null) {
                return null;
            }
            s = s.replaceAll("\\\\\"", "\"").replaceAll("\\\\'", "'");
            return getParsedStringWithScenarioValues(getParsedStringWithGlobalValues(s));
        }

        private String getParsedStringWithGlobalValues(String s) {
            GlobalSymbolReplaceParser parser = new GlobalSymbolReplaceParser(s);
            Set<String> symbols = parser.searchForSymbols();
            Map<String, String> values = new HashMap();
            symbols.forEach((String name) -> {
                String val = GlobalProps.get(name);
                if (val != null) {
                    values.put(name, val);
                }
            });
            return parser.parse(values);
        }

        private String getParsedStringWithScenarioValues(String s) {
            ScenarioSymbolReplaceParser parser = new ScenarioSymbolReplaceParser(s);
            Set<String> symbols = parser.searchForSymbols();
            Map<String, String> values = new HashMap();
            symbols.forEach((String name) -> {
                Object val = getScenarioProps().get(name);
                if (val != null) {
                    values.put(name, val.toString());
                }
            });
            return parser.parse(values);
        }

        private ScenarioProps getScenarioProps() {
            return CustomInjectorSource.getContextInjector().getInstance(ScenarioProps.class);
        }
    }
}
