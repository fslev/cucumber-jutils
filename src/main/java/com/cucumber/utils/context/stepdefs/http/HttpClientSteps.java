package com.cucumber.utils.context.stepdefs.http;

import com.cucumber.utils.clients.http.HttpClient;
import com.cucumber.utils.clients.http.Method;
import com.cucumber.utils.context.utils.Cucumbers;
import com.google.inject.Inject;
import io.cucumber.datatable.DataTable;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.HttpResponse;

import java.util.List;
import java.util.Map;

@ScenarioScoped
public class HttpClientSteps {

    @Inject
    private Cucumbers cucumbers;
    private HttpClient.Builder builder = new HttpClient.Builder();
    private HttpResponse response;

    @Given("HTTP REST service at address \"{}\"")
    public void setAddress(String address) {
        builder.address(address);
    }

    @And("HTTP path \"{}\"")
    public void setPath(String path) {
        builder.path(path);
    }

    @And("^HTTP headers$")
    public void setHeaders(DataTable table) {
        List<Map<String, String>> list = table.asMaps();
        if (!list.isEmpty()) {
            for (Map.Entry<String, String> e : list.get(list.size() - 1).entrySet()) {
                builder.addHeader(e.getKey(), e.getValue());
            }
        }
    }

    @And("^HTTP query params$")
    public void setQueryParams(DataTable table) {
        List<Map<String, String>> list = table.asMaps();
        if (!list.isEmpty()) {
            for (Map.Entry<String, String> e : list.get(list.size() - 1).entrySet()) {
                builder.addQueryParam(e.getKey(), e.getValue());
            }
        }
    }

    @And("HTTP method {}")
    public void setMethod(Method method) {
        builder.method(method);
    }

    @And("HTTP entity \"{}\"")
    public void setEntity(String entity) {
        builder.entity(entity);
    }

    @And("HTTP proxy host \"{}\" port \"{}\" and scheme \"{}\"")
    public void useProxy(String host, int port, String scheme) {
        builder.useProxy(host, port, scheme);
    }

    @And("HTTP timeout \"{}\"")
    public void setTimeout(int timeout) {
        builder.timeout(timeout);
    }

    @When("^HTTP execute$")
    public void execute() {
        this.response = builder.build().execute();
    }

    @Then("^HTTP compare response body with$")
    public void compareResponseBodyWith(StringBuilder expected) {
        cucumbers.compareHttpResponse(null, expected.toString(), response);
    }
}
