package com.cucumber.utils.features.stepdefs.json;

import com.cucumber.utils.context.ScenarioUtils;
import com.cucumber.utils.context.vars.ScenarioVars;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import io.jtest.utils.matcher.ObjectMatcher;

@ScenarioScoped
public class JsonConvertSteps {

    private String str;

    @Inject
    private ScenarioVars scenarioVars;
    @Inject
    private ScenarioUtils scenarioUtils;

    @Given("Compare JSON {} with {}")
    public void compareJsons(JsonNode expectedJson, String actualJson) {
        scenarioUtils.log("Compare JSON {} with {}", expectedJson, actualJson);
        scenarioVars.putAll(ObjectMatcher.match(null, expectedJson, actualJson));
    }

}
