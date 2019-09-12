package com.cucumber.utils.context.stepdefs;

import com.cucumber.utils.context.utils.Cucumbers;
import com.cucumber.utils.context.utils.ScenarioLogger;
import com.google.inject.Inject;
import cucumber.runtime.java.guice.ScenarioScoped;
import io.cucumber.java.en.Then;

import java.util.List;

@ScenarioScoped
public class CompareSteps {

    @Inject
    private Cucumbers cucumbers;
    @Inject
    private ScenarioLogger logger;

    @Then("COMPARE {} with \"{}\"")
    public void compareWithString(Object expected, Object actual) {
        logger.log("Compare %s against %s", expected, actual);
        cucumbers.compare(expected, actual);
    }

    @Then("Negative COMPARE {} with \"{}\"")
    public void compareNegativeWithString(Object expected, Object actual) {
        logger.log("Negative compare %s against %s", expected, actual);
        try {
            cucumbers.compare(expected, actual);
        } catch (AssertionError e) {
            logger.log("Assertion Error caught. Negative compare passes %s", e.getMessage());
            return;
        }
        throw new AssertionError("Compared objects match");
    }

    @Then("COMPARE {} with content from path \"{}\"")
    public void compareWithContentFromFilepath(Object expected, String filePath) {
        String actual = cucumbers.read(filePath);
        logger.log("Compare %s against %s", expected, actual);
        cucumbers.compare(expected, actual);
    }

    @Then("COMPARE {} with")
    public void compareWithDocString(Object expected, String actual) {
        logger.log("Compare %s against %s", expected, actual);
        cucumbers.compare(expected, actual);
    }

    @Then("Negative COMPARE {} with")
    public void compareNegativeWithDocString(Object expected, String actual) {
        logger.log("Negative compare %s against %s", expected, actual);
        try {
            cucumbers.compare(expected, actual);
        } catch (AssertionError e) {
            logger.log("Assertion Error caught. Negative compare passes %s", e.getMessage());
            return;
        }
        throw new AssertionError("Compared objects match");
    }

    @Then("COMPARE {} with table")
    public void compareWithDataTable(Object expected, List actual) {
        logger.log("Compare %s against %s", expected, actual);
        cucumbers.compare(expected, actual);
    }
}