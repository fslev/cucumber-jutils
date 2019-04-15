package com.cucumber.utils.context.stepdefs;

import com.cucumber.utils.context.utils.Cucumbers;
import com.google.inject.Inject;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;

import java.util.List;

@ScenarioScoped
public class CompareSteps {

    @Inject
    private Cucumbers cucumbers;

    @Then("COMPARE {} with \"{}\"")
    public void compareWithString(Object expected, Object actual) {
        cucumbers.compare(expected, actual);
    }

    @Then("COMPARE {} with content from path \"{}\"")
    public void compareWithContentFromFilepath(Object expected, String filePath) {
        cucumbers.compare(expected, cucumbers.read(filePath));
    }

    @Then("COMPARE {} with")
    public void compareWithDocString(Object expected, String actual) {
        cucumbers.compare(expected, actual);
    }

    @Then("COMPARE {} with table")
    public void compareWithDataTable(Object expected, List actual) {
        cucumbers.compare(expected, actual);
    }
}
