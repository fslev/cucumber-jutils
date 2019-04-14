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

    @Then("COMPARE {cstring} with \"{cstring}\"")
    public void compareWithString(Object expected, Object actual) {
        cucumbers.compare(expected, actual);
    }

    @Then("COMPARE {cstring} with content from path \"{cstring}\"")
    public void compareWithContentFromFilepath(Object expected, String filePath) {
        cucumbers.compare(expected, cucumbers.read(filePath));
    }

    @Then("COMPARE {cstring} with")
    public void compareWithDocString(Object expected, String actual) {
        cucumbers.compare(expected, actual);
    }

    @Then("COMPARE {cstring} with table")
    public void compareWithDataTable(Object expected, List actual) {
        cucumbers.compare(expected, actual);
    }
}
