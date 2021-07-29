package com.cucumber.utils.features.stepdefs.match;

import com.cucumber.utils.context.stepdefs.MatchSteps;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;

import static org.junit.Assert.assertThrows;

@ScenarioScoped
public class NegativeMatchSteps {

    @Inject
    MatchSteps matchSteps;

    @Given("Test negative match")
    public void negativeMatch() {
        matchSteps.matchNegativeWithString("a", "b");
        matchSteps.matchNegativeWithDocString("a", new StringBuilder("b"));
        assertThrows(AssertionError.class, () -> matchSteps.matchNegativeWithString("a", "a"));
        assertThrows(AssertionError.class, () -> matchSteps.matchNegativeWithDocString("a", new StringBuilder("a")));
    }
}
