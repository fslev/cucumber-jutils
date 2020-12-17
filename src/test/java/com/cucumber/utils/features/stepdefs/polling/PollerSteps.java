package com.cucumber.utils.features.stepdefs.polling;

import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.jtest.utils.matcher.ObjectMatcher;

@ScenarioScoped
public class PollerSteps {

    private int min;
    private int max;

    @Given("^interval limits (\\d+) and (\\d+)$")
    public void maxLimit(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Then("poll until random generated number {int} is found")
    public void pollFor(int expected) {
        ObjectMatcher.match(null, expected, this::generateRandom, 30, null, null);
    }

    @Then("poll {}s until 0=0")
    public void pollForZeroSeconds(int pollTimeoutInSeconds) {
        ObjectMatcher.match(null, 0, () -> 0, pollTimeoutInSeconds, null, null);
    }

    public int generateRandom() {
        return (int) (min + Math.random() * (max - min));
    }
}
