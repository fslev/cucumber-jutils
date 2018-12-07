package ro.cucumber.core.basicstepdefs.http;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import io.cucumber.datatable.DataTable;
import ro.cucumber.core.clients.http.HttpClient;
import ro.cucumber.core.clients.http.Method;
import ro.cucumber.core.context.compare.Cucumbers;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;

@ScenarioScoped
public class HttpClientSteps {
    private HttpClient.Builder builder = new HttpClient.Builder();
    private HttpResponse response;

    @Given("HTTP REST service at address {string}")
    public void setAddress(String address) {
        builder.address(address);
    }

    @And("HTTP path {string}")
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

    @And("HTTP method {httpMethod}")
    public void setMethod(Method method) {
        builder.method(method);
    }

    @And("HTTP entity {string}")
    public void setEntity(String entity) {
        builder.entity(entity);
    }

    @And("HTTP proxy host {string} port {int} and scheme {string}")
    public void useProxy(String host, int port, String scheme) {
        builder.useProxy(host, port, scheme);
    }

    @And("HTTP timeout {int}")
    public void setTimeout(int timeout) {
        builder.timeout(timeout);
    }

    @When("^HTTP execute$")
    public void execute() {
        this.response = builder.build().execute();
    }

    @Then("^HTTP compare response body with$")
    public void compareResponseBodyWith(String expected) {
        Cucumbers.compare(expected, response);
    }

    @And("HTTP compare response status code with {int}")
    public void compareResponseStatusCodeWith(int expected) {
    }
}
