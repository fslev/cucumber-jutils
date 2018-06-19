package stepdefs;

import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class AbstractExample {

    String s;

    @When("Ulalla (.*)")
    public void doSmth(String s) {
        this.s = s;
    }
}
