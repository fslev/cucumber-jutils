package com.cucumber.utils.context.steps;

import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class DateTimeStepsTest {

    /**
     * The patterns Cucumber's TeamCity formatter (io.cucumber:teamcity-formatter 0.3.1, used by IntelliJ for
     * Cucumber 7+) tries in this order on the trimmed message to give the IDE its "Click to see difference" link.
     */
    private static final Pattern[] CUCUMBER_TEAMCITY_COMPARE_PATTERNS = {
            Pattern.compile("expected: (.*)(?:\r\n|\r|\n) {5}but: was (.*)$", Pattern.DOTALL | Pattern.CASE_INSENSITIVE),
            Pattern.compile("expected: (.*)(?:\r\n|\r|\n) but was: (.*)$", Pattern.DOTALL | Pattern.CASE_INSENSITIVE),
            Pattern.compile("expected: <(.*)> but was: <(.*)>$", Pattern.DOTALL | Pattern.CASE_INSENSITIVE),
            Pattern.compile("expected:\\s?<(.*)> but was:\\s?<(.*)>$", Pattern.DOTALL | Pattern.CASE_INSENSITIVE),
            Pattern.compile("expected \\[(.*)] but found \\[(.*)]$", Pattern.DOTALL | Pattern.CASE_INSENSITIVE)};

    @Test
    public void chronoUnitsMismatchGivesBothValuesToIdeDiffPatterns() {
        AssertionError e = assertThrows(AssertionError.class,
                () -> DateTimeSteps.assertChronoUnitsBetween(2, 3, ChronoUnit.DAYS));
        String message = e.getMessage();
        assertEquals("Days differ ==> expected: <2> but was: <3>", message);
        for (Pattern pattern : CUCUMBER_TEAMCITY_COMPARE_PATTERNS) {
            Matcher matcher = pattern.matcher(message.trim());
            if (matcher.find()) {
                assertEquals("2", matcher.group(1));
                assertEquals("3", matcher.group(2));
                return;
            }
        }
        fail("No IDE diff pattern matches: " + message);
    }

    @Test
    public void chronoUnitsMatch() {
        assertDoesNotThrow(() -> DateTimeSteps.assertChronoUnitsBetween(2, 2, ChronoUnit.DAYS));
    }
}
