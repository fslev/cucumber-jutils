package com.cucumber.utils.features.stepdefs.compare;

import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.context.utils.Cucumbers;
import com.cucumber.utils.context.utils.ScenarioUtils;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;

@ScenarioScoped
public class CompareSteps {


    @Inject
    private ScenarioProps scenarioProps;
    @Inject
    private Cucumbers cucumbers;
    @Inject
    private ScenarioUtils scenarioUtils;

    @Given("Negative compare {} against {} via jsonNonExtensibleObject={}, jsonNonExtensibleArray={}, jsonArrayStrictOrder={} and message={}")
    public void compareJsonsNegative(String expectedJson, String actualJson, boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder, String message) {
        try {
            compareJsons(expectedJson, actualJson, jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder, message);
            throw new AssertionError("Are equal");
        } catch (AssertionError e) {
            scenarioUtils.log("PASSED: Not equal");
        }
    }

    @Given("Compare {} against {} via jsonNonExtensibleObject={}, jsonNonExtensibleArray={}, jsonArrayStrictOrder={} and message={}")
    public void compareJsons(String expectedJson, String actualJson, boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder, String message) {
        scenarioUtils.log("Compare JSON {} with {}", expectedJson, actualJson);
        cucumbers.compare(message, expectedJson, actualJson, jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder);
    }

    @Given("Negative compare {} against {} via xmlChildListLength={}, xmlChildListSequence={}, xmlElementNumAttributes={} and message={}")
    public void compareXmlsNegative(String expectedJson, String actualJson, boolean xmlChildListLength, boolean xmlChildListSequence, boolean xmlElementNumAttributes, String message) {
        try {
            compareXmls(expectedJson, actualJson, xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes, message);
            throw new AssertionError("Are equal");
        } catch (AssertionError e) {
            scenarioUtils.log("PASSED: Not equal");
        }
    }

    @Given("Compare {} against {} via xmlChildListLength={}, xmlChildListSequence={}, xmlElementNumAttributes={} and message={}")
    public void compareXmls(String expectedJson, String actualJson, boolean xmlChildListLength, boolean xmlChildListSequence, boolean xmlElementNumAttributes, String message) {
        scenarioUtils.log("Compare JSON {} with {}", expectedJson, actualJson);
        cucumbers.compare(message, expectedJson, actualJson, false, false, false, xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes);
    }

}
