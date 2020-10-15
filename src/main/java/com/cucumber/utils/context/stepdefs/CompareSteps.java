package com.cucumber.utils.context.stepdefs;

import com.cucumber.utils.context.Cucumbers;
import com.cucumber.utils.context.ScenarioUtils;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Then;
import io.jtest.utils.matcher.condition.MatchCondition;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertNull;

@ScenarioScoped
public class CompareSteps {

    @Inject
    private Cucumbers cucumbers;
    @Inject
    private ScenarioUtils logger;

    @Then("COMPARE {} with \"{}\"")
    public void compare(Object expected, Object actual) {
        logger.log("    Compare:\n{}\n    Against:\n{}", expected, actual);
        cucumbers.compare(expected, actual);
    }

    @Then("COMPARE {} with \"{}\" using matchConditions={}")
    public void compare(Object expected, Object actual, Set<MatchCondition> matchConditions) {
        logger.log("    Compare:\n{}\n    Against:\n{}\n    with match conditions: {}", expected, actual, matchConditions);
        cucumbers.compare(expected, actual, matchConditions.toArray(new MatchCondition[0]));
    }

    @Then("COMPARE {} with NULL")
    public void compareWithNull(Object expected) {
        logger.log("    Compare:\n{}\n    Against:\n{}", expected, null);
        assertNull(expected);
    }

    @Then("Negative COMPARE {} with \"{}\"")
    public void compareNegativeWithString(Object expected, Object actual) {
        logger.log("    Negative Compare:\n{}\n    Against:\n{}", expected, actual);
        try {
            cucumbers.compare(expected, actual);
        } catch (AssertionError e) {
            logger.log("Assertion Error caught. Negative compare passes {}", e.getMessage());
            return;
        }
        throw new AssertionError("Compared objects match");
    }

    @Then("COMPARE {} with content from path \"{}\"")
    public void compareWithContentFromFilepath(Object expected, String filePath) {
        compare(expected, cucumbers.read(filePath));
    }

    @Then("COMPARE {} with")
    public void compareWithDocString(Object expected, StringBuilder actual) {
        compare(expected, actual.toString());
    }

    @Then("Negative COMPARE {} with")
    public void compareNegativeWithDocString(Object expected, StringBuilder actual) {
        compareNegativeWithString(expected, actual.toString());
    }

    @Then("COMPARE {} with table")
    public void compareWithDataTable(Object expected, List<Map<String, Object>> actual) {
        logger.log("    Compare:\n{}\n    Against:\n{}", expected, actual);
        cucumbers.compare(expected, actual);
    }
}