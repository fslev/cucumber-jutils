package tests.ro.cucumber.core;

import ro.cucumber.core.matchers.StringRegexMatcher;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class StringRegexMatcherTests {

    @Test
    public void compareSimpleString() {
        String expected = "value";
        String actual = "value";
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        matcher.matches();
        Map<String, String> assignSymbols = matcher.getAssignSymbols();
        assertTrue(assignSymbols.isEmpty());
    }

    @Test
    public void compareSimpleInt() {
        int expected = 1;
        int actual = 1;
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        matcher.matches();
        Map<String, String> assignSymbols = matcher.getAssignSymbols();
        assertTrue(assignSymbols.isEmpty());
    }

    @Test
    public void compareSimpleIntWithAssignSymbols() {
        String expected = "~[sym1]";
        int actual = 10;
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        matcher.matches();
        Map<String, String> assignSymbols = matcher.getAssignSymbols();
        assertEquals("10", assignSymbols.get("sym1"));
        assertEquals(1, assignSymbols.size());
    }

    @Test(expected = AssertionError.class)
    public void compareSimpleInt_negative() {
        int expected = 1;
        int actual = 2;
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        matcher.matches();
    }

    @Test
    public void compareSimpleInteger() {
        Integer expected = 1;
        int actual = 1;
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        matcher.matches();
        Map<String, String> assignSymbols = matcher.getAssignSymbols();
        assertTrue(assignSymbols.isEmpty());
    }

    @Test
    public void compareSimpleBoolean() {
        boolean expected = true;
        Boolean actual = true;
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        matcher.matches();
        Map<String, String> assignSymbols = matcher.getAssignSymbols();
        assertTrue(assignSymbols.isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void compareSimpleBoolean_negative() {
        boolean expected = true;
        Boolean actual = false;
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        matcher.matches();
    }

    @Test(expected = AssertionError.class)
    public void compareSimpleString_negative() {
        String expected = "val";
        String actual = "value";
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        matcher.matches();
    }

    @Test
    public void compareInvalidRegex() {
        String expected = "va(lue";
        String actual = "va(lue";
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        matcher.matches();
        Map<String, String> assignSymbols = matcher.getAssignSymbols();
        assertTrue(assignSymbols.isEmpty());
    }

    @Test
    public void compareRegex() {
        String expected = "va.*ue";
        String actual = "va(lue";
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        matcher.matches();
        Map<String, String> assignSymbols = matcher.getAssignSymbols();
        assertTrue(assignSymbols.isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void compareRegex_negative() {
        String expected = "va.*ue";
        String actual = "va(le";
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        matcher.matches();
    }

    @Test
    public void compareSimpleStringWithAssignSymbols() {
        String expected = "The ~[sym1] is running through the ~[sym2]";
        String actual = "The rabbit is running through the forest";
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        matcher.matches();
        Map<String, String> assignSymbols = matcher.getAssignSymbols();
        assertEquals("rabbit", assignSymbols.get("sym1"));
        assertEquals("forest", assignSymbols.get("sym2"));
        assertEquals(2, assignSymbols.size());
    }
}
