package com.cucumber.utils.features.stepdefs.polling;

import com.cucumber.utils.context.utils.Cucumbers;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

@ScenarioScoped
public class PollerSteps {

    @Inject
    private Cucumbers cucumbers;
    private int min;
    private int max;

    @Given("^interval limits (\\d+) and (\\d+)$")
    public void maxLimit(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Then("poll until random generated number {int} is found")
    public void pollFor(int expected) {
        cucumbers.pollAndCompare(expected, 30, this::generateRandom);
    }

    @Then("poll {}s until 0=0")
    public void pollForZeroSeconds(int pollTimeoutInSeconds) {
        cucumbers.pollAndCompare(0, pollTimeoutInSeconds, () -> 0);
    }

    public int generateRandom() {
        return (int) (min + Math.random() * (max - min));
    }
}
