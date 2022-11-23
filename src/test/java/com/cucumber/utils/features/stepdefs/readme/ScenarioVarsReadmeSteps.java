package com.cucumber.utils.features.stepdefs.readme;

import com.cucumber.utils.context.ScenarioVarsUtils;
import com.cucumber.utils.context.vars.ScenarioVars;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ScenarioScoped
public class ScenarioVarsReadmeSteps {

    @Inject
    private ScenarioVars scenarioVars;

    @Given("Some random step which sets some variables")
    public void setVariables() {
        scenarioVars.put("animal", "Cheetah");
        Map<String, Object> vars = new HashMap<>();
        vars.put("figure", "triangle");
        vars.put("number", 10);
        scenarioVars.putAll(vars);
    }

    @Given("Read scenario variables from file")
    public void setVariablesFromFile() {
        ScenarioVarsUtils.loadScenarioVarsFromFile("features/readme/vars/config.properties", scenarioVars);
        assertEquals("Africa", scenarioVars.get("location"));
    }
}
