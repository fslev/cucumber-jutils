package com.cucumber.utils.context.steps.http;

import com.cucumber.utils.context.ScenarioUtils;
import com.cucumber.utils.context.vars.ScenarioVars;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import io.jtest.utils.clients.http.HttpClient;
import io.jtest.utils.clients.http.Method;
import io.jtest.utils.matcher.ObjectMatcher;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.util.Map;

@ScenarioScoped
public class HttpSteps {

    @Inject
    private ScenarioVars scenarioVars;
    @Inject
    private ScenarioUtils scenarioUtils;

    @Given("[http-util] Make request with url={}, method={}, queryParams={}, headers={}, entity={} and check response={}")
    public void executeAndMatch(String url, Method method, Map<String, String> queryParams, Map<String, String> headers,
                                String entity, String expected) throws IOException {
        scenarioUtils.log("{} {}\nQuery params: {}\nHeaders: {}\nEntity: {}", method, url, queryParams, headers, entity);
        HttpClient.Builder builder = new HttpClient.Builder().address(url).method(method).queryParams(queryParams).headers(headers);
        if (entity != null) {
            builder.entity(entity);
        }
        try (CloseableHttpResponse response = builder.build().execute()) {
            scenarioVars.putAll(ObjectMatcher.matchHttpResponse(null, expected, response));
        }
    }
}