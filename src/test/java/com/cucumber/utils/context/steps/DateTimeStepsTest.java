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
     * IntelliJ's Cucumber runner reads only the message text to offer its "Click to see difference" link,
     * using this pattern (case-insensitive, DOTALL, find).
     */
    private static final Pattern INTELLIJ_EXPECTED_BUT_WAS =
            Pattern.compile("expected: (.*)\\s+but was: (.*)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

    @Test
    public void chronoUnitsMismatchGivesBothValuesToIdeDiffPatterns() {
        AssertionError e = assertThrows(AssertionError.class,
                () -> DateTimeSteps.assertChronoUnitsBetween(2, 3, ChronoUnit.DAYS));
        String message = e.getMessage();
        assertTrue(message.startsWith("Days differ"), message);
        Matcher matcher = INTELLIJ_EXPECTED_BUT_WAS.matcher(message);
        assertTrue(matcher.find(), message);
        assertEquals("2" + System.lineSeparator(), matcher.group(1));
        assertEquals("3" + System.lineSeparator(), matcher.group(2));
    }

    @Test
    public void chronoUnitsMatch() {
        assertDoesNotThrow(() -> DateTimeSteps.assertChronoUnitsBetween(2, 2, ChronoUnit.DAYS));
    }
}
