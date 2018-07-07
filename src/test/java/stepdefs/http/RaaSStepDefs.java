package stepdefs.http;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import ro.cucumber.poc.http.HttpClient;
import com.google.inject.Inject;

public class RaaSStepDefs {

    private Scenario scenario;
    @Inject
    HttpClient client;

    @Before
    public void initScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    @Given("RaaS service")
    public void setRaasService() {
        client.setAddress("http://euronews.com");
    }

    @Then("RaaS make call")
    public void execute() {
        client.execute();
    }
}
