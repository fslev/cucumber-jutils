package com.cucumber.utils.features.stepdefs.readme;

import com.cucumber.utils.context.vars.ScenarioVars;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ScenarioScoped
public class ScenarioVarsAnotherReadmeSteps {

    @Inject
    private ScenarioVars scenarioVars;

    @Given("Some random step which reads the variables")
    public void readVariables() {
        assertEquals("Cheetah", scenarioVars.getAsString("animal"));
        assertEquals("triangle", scenarioVars.getAsString("figure"));
        assertEquals(10, scenarioVars.get("number"));
    }

    @Given("Some random step which reads variables set inside Gherkin")
    public void readVariablesSetViaGherkin() {
        assertEquals("Mars", scenarioVars.getAsString("planet"));
    }
}
