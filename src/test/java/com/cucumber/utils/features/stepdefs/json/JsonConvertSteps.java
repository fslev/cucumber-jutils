package com.cucumber.utils.features.stepdefs.json;

import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.context.utils.Cucumbers;
import com.cucumber.utils.context.utils.ScenarioUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;

@ScenarioScoped
public class JsonConvertSteps {

    private String str;

    @Inject
    private ScenarioProps scenarioProps;
    @Inject
    private Cucumbers cucumbers;
    @Inject
    private ScenarioUtils scenarioUtils;

    @Given("Compare JSON {} with {}")
    public void compareJsons(JsonNode expectedJson, String actualJson) {
        scenarioUtils.log("Compare JSON {} with {}", expectedJson, actualJson);
        cucumbers.compare(expectedJson, actualJson);
    }

}
