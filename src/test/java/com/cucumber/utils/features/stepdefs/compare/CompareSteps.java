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

    @Given("Negative compare {} against {} via jsonNonExtensibleObject={}, jsonNonExtensibleArray={}, jsonArrayStrictOrder={}, xmlChildListLength={}, xmlChildListSequence={}, xmlElementNumAttributes={} and message={}")
    public void compareNegative(String expectedJson, String actualJson, boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder, boolean xmlChildListLength, boolean xmlChildListSequence, boolean xmlElementNumAttributes, String message) {
        try {
            compare(expectedJson, actualJson, jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder, xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes, message);
            throw new AssertionError("Are equal");
        } catch (AssertionError e) {
            scenarioUtils.log("PASSED: Not equal\n{}", e);
        }
    }

    @Given("Compare {} against {} via jsonNonExtensibleObject={}, jsonNonExtensibleArray={}, jsonArrayStrictOrder={}, xmlChildListLength={}, xmlChildListSequence={}, xmlElementNumAttributes={} and message={}")
    public void compare(String expectedJson, String actualJson, boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder, boolean xmlChildListLength, boolean xmlChildListSequence, boolean xmlElementNumAttributes, String message) {
        scenarioUtils.log("Compare\n{}\nwith\n{}", expectedJson, actualJson);
        cucumbers.compare(message, expectedJson, actualJson, jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder, xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes);
    }
}
