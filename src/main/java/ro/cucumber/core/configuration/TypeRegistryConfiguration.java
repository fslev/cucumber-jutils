package ro.cucumber.core.configuration;

import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.cucumberexpressions.Transformer;
import ro.cucumber.core.clients.http.HttpClient;
import ro.cucumber.core.clients.http.HttpVerb;

import java.util.Locale;

import static java.util.Locale.ENGLISH;

public class TypeRegistryConfiguration implements TypeRegistryConfigurer {

    @Override
    public Locale locale() {
        return ENGLISH;
    }

    @Override
    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        typeRegistry.defineParameterType(new ParameterType<>("verb",
                "(GET|POST|PUT|DELETE|OPTIONS|HEAD|CONNECT|TRACE)", HttpVerb.class, (String s) -> {
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

        typeRegistry.defineParameterType(
                new ParameterType<>("zest", ".*", String.class, new Transformer<String>() {
                    @Override
                    public String transform(String s) throws Throwable {
                        System.out.println("TypeREG: " + CustomInjectorSource.getContextInjector()
                                .getInstance(HttpClient.class));
                        return s;
                    }
                }));
    }
}
