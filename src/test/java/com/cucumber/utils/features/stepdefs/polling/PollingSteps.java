package com.cucumber.utils.features.stepdefs.polling;

import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.jtest.utils.matcher.ObjectMatcher;

import java.time.Duration;
import java.util.function.Supplier;

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
        pollUntilMatch(expected, this::generateRandom, Duration.ofSeconds(30));
    }

    @Then("poll {} until 0=0")
    public void pollForZeroSeconds(Duration pollingDuration) {
        pollUntilMatch(0, () -> 0, pollingDuration);
    }

    private static void pollUntilMatch(Object expected, Supplier<Object> actualSupplier, Duration pollingDuration) {
        long deadline = System.nanoTime() + pollingDuration.toNanos();
        RuntimeException lastFailure;
        do {
            try {
                ObjectMatcher.match(null, expected, actualSupplier.get());
                return;
            } catch (RuntimeException e) {
                lastFailure = e;
            }
        } while (System.nanoTime() < deadline);
        throw lastFailure;
    }

    public int generateRandom() {
        return (int) (min + Math.random() * (max - min));
    }
}
