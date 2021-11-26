package com.cucumber.utils.context.stepdefs;

import com.cucumber.utils.context.ScenarioUtils;
import com.cucumber.utils.context.ScenarioVarsUtils;
import com.cucumber.utils.context.vars.ScenarioVars;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Then;
import io.jtest.utils.matcher.ObjectMatcher;
import io.jtest.utils.matcher.condition.MatchCondition;
import ro.skyah.util.MessageUtil;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNull;

@ScenarioScoped
public class MatchSteps {

    @Inject
    private ScenarioVars scenarioVars;
    @Inject
    private ScenarioUtils logger;

    @Then("Match {} with \"{}\"")
    public void match(Object expected, Object actual) {
        logger.log("MATCH:\n\n{}\n\nagainst:\n\n{}", expected, actual != null ? MessageUtil.cropL(actual.toString()) : null);
        scenarioVars.putAll(ObjectMatcher.match(null, expected, actual));
    }

    @Then("Match {} with \"{}\" using matchConditions={}")
    public void match(Object expected, Object actual, MatchCondition[] matchConditions) {
        logger.log("MATCH:\n\n{}\n\nagainst:\n\n{}\n    with match conditions: {}", expected,
                actual != null ? MessageUtil.cropL(actual.toString()) : null, matchConditions);
        scenarioVars.putAll(ObjectMatcher.match(null, expected, actual, matchConditions));
    }

    @Then("Match {} with NULL")
    public void matchWithNull(Object expected) {
        logger.log("MATCH:\n\n{}\n\nagainst:\n\n{}", expected, null);
        assertNull(expected);
    }

    @Then("Negative match {} with \"{}\"")
    public void matchNegativeWithString(Object expected, Object actual) {
        logger.log("Negative match:\n\n{}\n\nagainst:\n\n{}", expected, actual != null ? MessageUtil.cropL(actual.toString()) : null);
        try {
            scenarioVars.putAll(ObjectMatcher.match(null, expected, actual));
        } catch (AssertionError e) {
            logger.log("Assertion Error caught. Negative match passes {}", e.getMessage());
            return;
        }
        throw new AssertionError("Objects match");
    }

    @Then("Match {} with content from path \"{}\"")
    public void matchWithContentFromFilepath(Object expected, String filePath) {
        match(expected, ScenarioVarsUtils.parse(filePath, scenarioVars));
    }

    @Then("Match {} with")
    public void matchWithDocString(Object expected, StringBuilder actual) {
        match(expected, actual.toString());
    }

    @Then("Negative match {} with")
    public void matchNegativeWithDocString(Object expected, StringBuilder actual) {
        matchNegativeWithString(expected, actual.toString());
    }

    @Then("Match {} with table")
    public void matchWithDataTable(Object expected, List<Map<String, Object>> actual) {
        logger.log("MATCH:\n\n{}\n\nagainst:\n\n{}", expected, actual);
        scenarioVars.putAll(ObjectMatcher.match(null, expected, actual));
    }
}