package stepdefs.http;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import io.cucumber.datatable.DataTable;
import ro.cucumber.poc.http.HttpBaseStepDefs;
import ro.cucumber.poc.http.HttpVerb;
import static org.junit.Assert.assertEquals;

@ScenarioScoped
public class HttpSteps extends HttpBaseStepDefs {

    private Scenario scenario;

    @Before
    public void initScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    @Given("HTTP REST service at address {string}")
    public void setAddress(String address) {
        super.init();
        super.setAddress(address);
    }

    @And("HTTP path {string}")
    public void setPath(String path) {
        super.setPath(path);
    }

    @And("^HTTP headers$")
    public void setHeaders(DataTable headers) {
        super.setHeaders(headers);
    }

    @And("^HTTP query params$")
    public void setQueryParams(DataTable params) {
        super.setQueryParams(params);
    }

    @And("HTTP method {verb}")
    public void setMethod(HttpVerb verb) {
        super.setMethod(verb);
    }

    @And("HTTP entity {string}")
    public void setEntity(String entity) {
        super.setEntity(entity);
    }

    @And("HTTP proxy host {string} port {int} and scheme {string}")
    public void useProxy(String host, int port, String scheme) {
        super.useProxy(host, port, scheme);
    }

    @And("HTTP timeout {int}")
    public void setTimeout(int timeout) {
        super.setTimeout(timeout);
    }

    @When("^HTTP execute$")
    public void execute() {
        super.execute();
    }

    @Then("^HTTP compare response body with$")
    public void compareResponseBodyWith(String expected) {
        System.out.println("waa");
        // execute();
    }

    @And("HTTP compare response status code with {int}")
    public void compareResponseStatusCodeWith(int expected) {
        System.out.println("waasss");
        assertEquals(expected, response.getStatusLine().getStatusCode());
    }
}
