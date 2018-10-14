package ro.cucumber.core.features.stepdefs.symbols;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import ro.cucumber.core.context.compare.CompareCucumbers;

@ScenarioScoped
public class PollerSteps {
    int min;
    int max;

    @Given("^interval limits (\\d+) and (\\d+)$")
    public void maxLimit(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Then("poll until random generated number (\\d+) is found")
    public void pollFor(int expected) {
        CompareCucumbers.compareWithPolling(expected, () -> generateRandom());
    }

    public int generateRandom() {
        return (int) (min + Math.random() * (max - min));
    }


}
