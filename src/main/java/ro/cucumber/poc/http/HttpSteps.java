package ro.cucumber.poc.http;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import io.cucumber.datatable.DataTable;
import java.util.List;
import java.util.Map;
import com.google.inject.Inject;

@ScenarioScoped
public class HttpSteps {

    private Scenario scenario;
    @Inject
    private HttpClient client;

    @Before
    public void initScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    @Given("HTTP REST service at address {string}")
    public void setAddress(String address) {
        client.setAddress(address);
    }

    @And("HTTP path {string}")
    public void setPath(String path) {
        client.setPath(path);
    }

    @And("^HTTP headers$")
    public void setHeaders(DataTable table) {
        List<Map<String, String>> list = table.asMaps();
        if (!list.isEmpty()) {
            for (Map.Entry<String, String> e : list.get(list.size() - 1).entrySet()) {
                client.addHeader(e.getKey(), e.getValue());
            }
        }
    }

    @And("^HTTP query params$")
    public void setQueryParams(DataTable table) {
        List<Map<String, String>> list = table.asMaps();
        if (!list.isEmpty()) {
            for (Map.Entry<String, String> e : list.get(list.size() - 1).entrySet()) {
                client.addQueryParam(e.getKey(), e.getValue());
            }
        }
    }

    @And("HTTP method {verb}")
    public void setMethod(HttpVerb verb) {
        client.setMethod(verb);
    }

    @And("HTTP entity {string}")
    public void setEntity(String entity) {
        client.setEntity(entity);
    }

    @And("HTTP proxy host {string} port {int} and scheme {string}")
    public void useProxy(String host, int port, String scheme) {
        client.useProxy(host, port, scheme);
    }

    @And("HTTP timeout {int}")
    public void setTimeout(int timeout) {
        client.setTimeout(timeout);
    }

    @When("^HTTP execute$")
    public void execute() {
        client.execute();
    }

    @Then("^HTTP compare response body with$")
    public void compareResponseBodyWith(String expected) {
        scenario.write(expected);
    }

    @And("HTTP compare response status code with {int}")
    public void compareResponseStatusCodeWith(int expected) {}
}
