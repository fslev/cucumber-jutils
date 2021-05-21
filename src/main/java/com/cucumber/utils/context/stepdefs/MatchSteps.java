package com.cucumber.utils.context.stepdefs;

import com.cucumber.utils.context.ScenarioPropsUtils;
import com.cucumber.utils.context.ScenarioUtils;
import com.cucumber.utils.context.props.ScenarioProps;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Then;
import io.jtest.utils.matcher.ObjectMatcher;
import io.jtest.utils.matcher.condition.MatchCondition;
import ro.skyah.util.MessageUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertNull;

@ScenarioScoped
public class MatchSteps {

    @Inject
    private ScenarioProps scenarioProps;
    @Inject
    private ScenarioUtils logger;

    @Then("Match {} with \"{}\"")
    public void match(Object expected, Object actual) {
        logger.log("    MATCH:\n\n{}\n\n    against:\n\n{}", expected, actual != null ? MessageUtil.cropXXL(actual.toString()) : null);
        scenarioProps.putAll(ObjectMatcher.match(null, expected, actual));
    }

    @Then("Match {} with \"{}\" using matchConditions={}")
    public void match(Object expected, Object actual, Set<MatchCondition> matchConditions) {
        logger.log("    MATCH:\n\n{}\n\n    against:\n\n{}\n    with match conditions: {}", expected,
                actual != null ? MessageUtil.cropXXL(actual.toString()) : null, matchConditions);
        scenarioProps.putAll(ObjectMatcher.match(null, expected, actual, matchConditions.toArray(new MatchCondition[0])));
    }

    @Then("Match {} with NULL")
    public void matchWithNull(Object expected) {
        logger.log("    MATCH:\n\n{}\n\n    against:\n\n{}", expected, null);
        assertNull(expected);
    }

    @Then("Negative match {} with \"{}\"")
    public void matchNegativeWithString(Object expected, Object actual) {
        logger.log("    Negative match:\n\n{}\n\n    against:\n\n{}", expected, actual != null ? MessageUtil.cropXXL(actual.toString()) : null);
        try {
            scenarioProps.putAll(ObjectMatcher.match(null, expected, actual));
        } catch (AssertionError e) {
            logger.log("Assertion Error caught. Negative match passes {}", e.getMessage());
            return;
        }
        throw new AssertionError("Objects match");
    }

    @Then("Match {} with content from path \"{}\"")
    public void matchWithContentFromFilepath(Object expected, String filePath) {
        match(expected, ScenarioPropsUtils.parse(filePath, scenarioProps));
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
        logger.log("    MATCH:\n\n{}\n\n    against:\n\n{}", expected, actual);
        scenarioProps.putAll(ObjectMatcher.match(null, expected, actual));
    }
}