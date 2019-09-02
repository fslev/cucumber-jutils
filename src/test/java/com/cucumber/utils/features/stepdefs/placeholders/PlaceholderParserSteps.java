package com.cucumber.utils.features.stepdefs.placeholders;

import com.cucumber.utils.context.props.ScenarioProps;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import static org.junit.Assert.assertEquals;

@ScenarioScoped
public class PlaceholderParserSteps {

    private String str;

    @Inject
    private ScenarioProps scenarioProps;

    @Given("The string with global placeholders \"{}\"")
    public void stringWithGlobalSymbols(String str) {
        this.str = str;
    }

    @Given("The string with scenario placeholders \"{}\"")
    public void stringWithScenarioSymbols(String str) {
        this.str = str;
    }

    @Then("Check filled string equals \"{}\"")
    public void check(String str) {
        assertEquals(str, this.str);
    }
}
