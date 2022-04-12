package com.cucumber.utils.features.stepdefs.polling;

import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.jtest.utils.matcher.ObjectMatcher;

import java.time.Duration;

@ScenarioScoped
public class PollingSteps {

    private int min;
    private int max;

    @Given("^interval limits (\\d+) and (\\d+)$")
    public void maxLimit(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Then("poll until random generated number {int} is found")
    public void pollFor(int expected) {
        ObjectMatcher.match(null, expected, this::generateRandom, Duration.ofSeconds(30), null, null);
    }

    @Then("poll {} until 0=0")
    public void pollForZeroSeconds(Duration pollingDuration) {
        ObjectMatcher.match(null, 0, () -> 0, pollingDuration, null, null);
    }

    public int generateRandom() {
        return (int) (min + Math.random() * (max - min));
    }
}
