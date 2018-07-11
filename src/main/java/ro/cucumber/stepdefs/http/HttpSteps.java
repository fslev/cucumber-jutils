package ro.cucumber.stepdefs.http;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import io.cucumber.datatable.DataTable;
import ro.cucumber.core.http.HttpClient;
import ro.cucumber.core.http.HttpVerb;
import java.util.List;
import java.util.Map;
import com.google.inject.Inject;

@ScenarioScoped
public class HttpSteps {

    private Scenario scenario;
    @Inject
    private HttpClient.Builder builder;

    @Before
    public void initScenario(Scenario scenario) {
        this.scenario = scenario;
    }

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

    @And("HTTP method {verb}")
    public void setMethod(HttpVerb verb) {
        builder.method(verb);
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
        builder.build().execute();
    }

    @Then("^HTTP compare response body with$")
    public void compareResponseBodyWith(String expected) {
        scenario.write(expected);
    }

    @And("HTTP compare response status code with {int}")
    public void compareResponseStatusCodeWith(int expected) {}
}
