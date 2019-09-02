package com.cucumber.utils.context.stepdefs;

import com.cucumber.utils.context.utils.Cucumbers;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Then;
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

    @Then("Negative COMPARE {} with \"{}\"")
    public void compareNegativeWithString(Object expected, Object actual) {
        log.info("Negative compare {} against {}", expected, actual);
        try {
            cucumbers.compare(expected, actual);
        } catch (AssertionError e) {
            log.info("Assertion Error caught. Negative compare passes {}", e.getMessage());
            return;
        }
        throw new AssertionError("Compared objects match");
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

    @Then("Negative COMPARE {} with")
    public void compareNegativeWithDocString(Object expected, String actual) {
        log.info("Negative compare {} against {}", expected, actual);
        try {
            cucumbers.compare(expected, actual);
        } catch (AssertionError e) {
            log.info("Assertion Error caught. Negative compare passes {}", e.getMessage());
            return;
        }
        throw new AssertionError("Compared objects match");
    }

    @Then("COMPARE {} with table")
    public void compareWithDataTable(Object expected, List actual) {
        log.info("Compare {} against {}", expected, actual);
        cucumbers.compare(expected, actual);
    }
}
