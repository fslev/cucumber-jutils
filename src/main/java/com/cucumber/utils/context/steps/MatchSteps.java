package com.cucumber.utils.context.steps;

import com.cucumber.utils.context.ScenarioUtils;
import com.cucumber.utils.context.ScenarioVarsUtils;
import com.cucumber.utils.context.vars.ScenarioVars;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Then;
import io.json.compare.util.MessageUtil;
import io.jtest.utils.matcher.ObjectMatcher;
import io.jtest.utils.matcher.condition.MatchCondition;

import java.util.List;
import java.util.Map;

@ScenarioScoped
public class MatchSteps {

    private static final String MATCH_LOG = """
            MATCH:

            {}

            against:

            {}""";

    private static final String MATCH_WITH_CONDITIONS_LOG = """
            MATCH:

            {}

            against:

            {}
                with match conditions: {}""";

    private static final String NEGATIVE_MATCH_LOG = """
            Negative match:

            {}

            against:

            {}""";

    @Inject
    private ScenarioVars scenarioVars;
    @Inject
    private ScenarioUtils logger;

    @Then("[util] Match {} with {}")
    public void match(Object expected, Object actual) {
        logger.log(MATCH_LOG, expected, cropped(actual));
        scenarioVars.putAll(ObjectMatcher.match(null, expected, actual));
    }

    @Then("[util] Match {} against")
    public void matchWithDocString(Object expected, StringBuilder actual) {
        match(expected, actual.toString());
    }

    @Then("[util] Match {} against {} using matchConditions={}")
    public void match(Object expected, Object actual, MatchCondition[] matchConditions) {
        logger.log(MATCH_WITH_CONDITIONS_LOG, expected, cropped(actual), matchConditions);
        scenarioVars.putAll(ObjectMatcher.match(null, expected, actual, matchConditions));
    }

    @Then("[util] Match {} against file \"{}\"")
    public void matchWithContentFromFilepath(Object expected, String filePath) {
        match(expected, ScenarioVarsUtils.parse(filePath, scenarioVars));
    }

    @Then("[util] Match {} against table")
    public void matchWithDataTable(Object expected, List<Map<String, Object>> actual) {
        logger.log(MATCH_LOG, expected, actual);
        scenarioVars.putAll(ObjectMatcher.match(null, expected, actual));
    }

    @Then("[util] Match {} against NULL")
    public void matchWithNull(Object expected) {
        logger.log(MATCH_LOG, expected, null);
        if (expected != null) {
            throw new AssertionError("Expected null but was: " + expected);
        }
    }

    @Then("[util] Negative match {} with {}")
    public void matchNegativeWithString(Object expected, Object actual) {
        logger.log(NEGATIVE_MATCH_LOG, expected, cropped(actual));
        try {
            scenarioVars.putAll(ObjectMatcher.match(null, expected, actual));
        } catch (AssertionError e) {
            logger.log("Assertion Error caught. Negative match passes {}", e.getMessage());
            return;
        }
        throw new AssertionError("Objects match");
    }

    @Then("[util] Negative match {} against")
    public void matchNegativeWithDocString(Object expected, StringBuilder actual) {
        matchNegativeWithString(expected, actual.toString());
    }

    private static String cropped(Object value) {
        return value != null ? MessageUtil.cropL(value.toString()) : null;
    }
}
