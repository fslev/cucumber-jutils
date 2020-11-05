package com.cucumber.utils.context.stepdefs.http;

import com.cucumber.utils.context.Cucumbers;
import com.cucumber.utils.context.ScenarioUtils;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import io.jtest.utils.clients.http.HttpClient;
import io.jtest.utils.clients.http.Method;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.util.Map;

@ScenarioScoped
public class HttpSteps {

    @Inject
    private Cucumbers cucumbers;
    @Inject
    private ScenarioUtils scenarioUtils;

    @Given("HTTP Invoke REST API with url={}, method={}, queryParams={}, headers={}, entity={} and check response={}")
    public void executeAndCompare(String url, Method method, Map<String, String> queryParams, Map<String, String> headers,
                                  String entity, String expected) throws IOException {
        scenarioUtils.log("{} {}\nQuery params: {}\nHeaders: {}\nEntity: {}", method, url, queryParams, headers, entity);
        HttpClient.Builder builder = new HttpClient.Builder().address(url).method(method).setQueryParams(queryParams).setHeaders(headers);
        if (entity != null) {
            builder.entity(entity);
        }
        try (CloseableHttpResponse response = builder.build().execute()) {
            cucumbers.compareHttpResponse(null, expected, response);
        }
    }
}