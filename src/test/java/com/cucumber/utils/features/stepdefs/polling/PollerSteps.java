package com.cucumber.utils.features.stepdefs.polling;

import com.cucumber.utils.context.compare.Cucumbers;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class PollerSteps {
    int min;
    int max;

    @Given("^interval limits (\\d+) and (\\d+)$")
    public void maxLimit(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Then("poll until random generated number {int} is found")
    public void pollFor(int expected) {
        Cucumbers.pollAndCompare(expected, () -> generateRandom());
    }

    public int generateRandom() {
        return (int) (min + Math.random() * (max - min));
    }


}
