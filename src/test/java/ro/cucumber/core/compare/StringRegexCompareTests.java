package ro.cucumber.core.compare;

import ro.cucumber.core.engineering.compare.StringRegexCompare;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class StringRegexCompareTests {

    @Test
    public void compareSimpleString() {
        String expected = "value";
        String actual = "value";
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        Map<String, String> symbols = matcher.compare();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareSimpleInt() {
        int expected = 1;
        int actual = 1;
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        Map<String, String> symbols = matcher.compare();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareSimpleIntWithAssignSymbols() {
        String expected = "~[sym1]";
        int actual = 10;
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        Map<String, String> symbols = matcher.compare();
        assertEquals("10", symbols.get("sym1"));
        assertEquals(1, symbols.size());
    }

    @Test(expected = AssertionError.class)
    public void compareSimpleInt_negative() {
        int expected = 1;
        int actual = 2;
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        matcher.compare();
    }

    @Test
    public void compareSimpleInteger() {
        Integer expected = 1;
        int actual = 1;
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        Map<String, String> symbols = matcher.compare();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareSimpleBoolean() {
        boolean expected = true;
        Boolean actual = true;
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        Map<String, String> symbols = matcher.compare();
        assertTrue(symbols.isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void compareSimpleBoolean_negative() {
        boolean expected = true;
        Boolean actual = false;
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        matcher.compare();
    }

    @Test(expected = AssertionError.class)
    public void compareSimpleString_negative() {
        String expected = "val";
        String actual = "value";
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        matcher.compare();
    }

    @Test
    public void compareInvalidRegex() {
        String expected = "va(lue";
        String actual = "va(lue";
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        Map<String, String> symbols = matcher.compare();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareRegex() {
        String expected = "va.*ue";
        String actual = "va(lue";
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        Map<String, String> symbols = matcher.compare();
        assertTrue(symbols.isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void compareRegex_negative() {
        String expected = "va.*ue";
        String actual = "va(le";
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        matcher.compare();
    }

    @Test
    public void compareSimpleStringWithAssignSymbols() {
        String expected = "The ~[sym1] is running through the ~[sym2]";
        String actual = "The rabbit is running through the forest";
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        Map<String, String> symbols = matcher.compare();
        assertEquals("rabbit", symbols.get("sym1"));
        assertEquals("forest", symbols.get("sym2"));
        assertEquals(2, symbols.size());
    }

    @Test
    public void compareRegexWithAssignSymbols() {
        String expected = ".* Rabbit ~[sym1] in the .*";
        String actual = "The Rabbit is running in the forest";
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        Map<String, String> symbols = matcher.compare();
        assertEquals("is running", symbols.get("sym1"));
        assertEquals(1, symbols.size());
    }

    @Test(expected = AssertionError.class)
    public void compareRegexWithAssignSymbols_negative() {
        String expected = ".* Fox ~[sym1] in the .*";
        String actual = "The Rabbit is running in the forest";
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        matcher.compare();
    }

    @Test(expected = AssertionError.class)
    public void checkMessageFromFailedCompare() {
        String expected = "wa";
        int actual = 1;
        try {
            new StringRegexCompare("Some mess", expected, actual).compare();
        } catch (AssertionError e) {
            assertTrue(e.getMessage().contains("Some mess") && e.getMessage().contains("Expected:"));
            throw e;
        }
    }
}
