package stepdefs.http;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import ro.cucumber.poc.http.HttpClient;
import com.google.inject.Inject;

public class RaaSStepDefs extends Base {

    private Scenario scenario;

    @Inject
    protected HttpClient client;

    @Before
    public void initScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    @Given("RaaS service")
    public void setRaasService() {
        // System.out.println("Aici: " + greeter.da());
        client.setAddress("http://euronews.com");
    }

    @Then("RaaS make call {zest}")
    public void execute(String test) {
        // client.execute();
    }
}
