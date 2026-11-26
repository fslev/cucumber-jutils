package com.cucumber.utils.context.steps;

import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateTimeStepsTest {

    /**
     * Cucumber's TeamCity formatter (io.cucumber:teamcity-formatter, used by IntelliJ for Cucumber 7+) gives the
     * IDE its "Click to see difference" link only when the trimmed message matches this pattern (find).
     */
    private static final Pattern CUCUMBER_TEAMCITY_EXPECTED_BUT_WAS =
            Pattern.compile("expected: (.*)(?:\r\n|\r|\n) but was: (.*)$", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

    @Test
    public void chronoUnitsMismatchGivesBothValuesToIdeDiffPatterns() {
        AssertionError e = assertThrows(AssertionError.class,
                () -> DateTimeSteps.assertChronoUnitsBetween(2, 3, ChronoUnit.DAYS));
        String message = e.getMessage();
        assertTrue(message.startsWith("Days differ"), message);
        Matcher matcher = CUCUMBER_TEAMCITY_EXPECTED_BUT_WAS.matcher(message.trim());
        assertTrue(matcher.find(), message);
        assertEquals("2", matcher.group(1));
        assertEquals("3", matcher.group(2));
    }

    @Test
    public void chronoUnitsMatch() {
        assertDoesNotThrow(() -> DateTimeSteps.assertChronoUnitsBetween(2, 2, ChronoUnit.DAYS));
    }
}
