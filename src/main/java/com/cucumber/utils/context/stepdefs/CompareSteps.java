package com.cucumber.utils.context.stepdefs;

import com.cucumber.utils.context.utils.Cucumbers;
import com.google.inject.Inject;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@ScenarioScoped
public class CompareSteps {

    private Logger log = LogManager.getLogger();

    @Inject
    private Cucumbers cucumbers;

    @Then("COMPARE {} with \"{}\"")
    public void compareWithString(Object expected, Object actual) {
        log.info("Compare {} against {}", expected, actual);
        cucumbers.compare(expected, actual);
    }

    @Then("COMPARE {} with content from path \"{}\"")
    public void compareWithContentFromFilepath(Object expected, String filePath) {
        String actual = cucumbers.read(filePath);
        log.info("Compare {} against {}", expected, actual);
        cucumbers.compare(expected, actual);
    }

    @Then("COMPARE {} with")
    public void compareWithDocString(Object expected, String actual) {
        log.info("Compare {} against {}", expected, actual);
        cucumbers.compare(expected, actual);
    }

    @Then("COMPARE {} with table")
    public void compareWithDataTable(Object expected, List actual) {
        log.info("Compare {} against {}", expected, actual);
        cucumbers.compare(expected, actual);
    }
}
