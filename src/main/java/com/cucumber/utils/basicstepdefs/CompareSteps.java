package com.cucumber.utils.basicstepdefs;

import com.cucumber.utils.context.compare.Cucumbers;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;

import java.util.List;

@ScenarioScoped
public class CompareSteps {
    @Then("COMPARE {cstring} with \"{cstring}\"")
    public void compareWithString(Object expected, Object actual) {
        Cucumbers.compare(expected, actual);
    }

    @Then("COMPARE {cstring} with content from path \"{cstring}\"")
    public void compareWithContentFromFilepath(Object expected, String filePath) {
        Cucumbers.compare(expected, Cucumbers.read(filePath));
    }

    @Then("COMPARE {cstring} with")
    public void compareWithDocString(Object expected, String actual) {
        Cucumbers.compare(expected, actual);
    }

    @Then("COMPARE {cstring} with table")
    public void compareWithDataTable(Object expected, List actual) {
        Cucumbers.compare(expected, actual);
    }
}
