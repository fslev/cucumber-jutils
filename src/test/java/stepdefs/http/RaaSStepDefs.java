package stepdefs.http;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import ro.cucumber.poc.http.HttpVerb;
import com.google.inject.Inject;

public class RaaSStepDefs {

    private Scenario scenario;


    @Before
    public void initScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    @Given("Raas service")
    public void setRaasService() {}

    @Then("make call")
    public void execute() {}
}
