package tests.ro.cucumber.core.matchers;

import org.junit.Test;
import ro.cucumber.core.matchers.StringRegexMatcher;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StringRegexMatcherTests {

    @Test
    public void compareSimpleString() {
        String expected = "value";
        String actual = "value";
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        Map<String, String> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareSimpleInt() {
        int expected = 1;
        int actual = 1;
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        Map<String, String> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareSimpleIntWithAssignSymbols() {
        String expected = "~[sym1]";
        int actual = 10;
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        Map<String, String> symbols = matcher.match();
        assertEquals("10", symbols.get("sym1"));
        assertEquals(1, symbols.size());
    }

    @Test(expected = AssertionError.class)
    public void compareSimpleInt_negative() {
        int expected = 1;
        int actual = 2;
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        matcher.match();
    }

    @Test
    public void compareSimpleInteger() {
        Integer expected = 1;
        int actual = 1;
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        Map<String, String> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareSimpleBoolean() {
        boolean expected = true;
        Boolean actual = true;
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        Map<String, String> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void compareSimpleBoolean_negative() {
        boolean expected = true;
        Boolean actual = false;
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        matcher.match();
    }

    @Test(expected = AssertionError.class)
    public void compareSimpleString_negative() {
        String expected = "val";
        String actual = "value";
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        matcher.match();
    }

    @Test
    public void compareInvalidRegex() {
        String expected = "va(lue";
        String actual = "va(lue";
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        Map<String, String> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareRegex() {
        String expected = "va.*ue";
        String actual = "va(lue";
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        Map<String, String> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void compareRegex_negative() {
        String expected = "va.*ue";
        String actual = "va(le";
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        matcher.match();
    }

    @Test
    public void compareSimpleStringWithAssignSymbols() {
        String expected = "The ~[sym1] is running through the ~[sym2]";
        String actual = "The rabbit is running through the forest";
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        Map<String, String> symbols = matcher.match();
        assertEquals("rabbit", symbols.get("sym1"));
        assertEquals("forest", symbols.get("sym2"));
        assertEquals(2, symbols.size());
    }

    @Test
    public void compareRegexWithAssignSymbols() {
        String expected = ".* Rabbit ~[sym1] in the .*";
        String actual = "The Rabbit is running in the forest";
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        Map<String, String> symbols = matcher.match();
        assertEquals("is running", symbols.get("sym1"));
        assertEquals(1, symbols.size());
    }

    @Test(expected = AssertionError.class)
    public void compareRegexWithAssignSymbols_negative() {
        String expected = ".* Fox ~[sym1] in the .*";
        String actual = "The Rabbit is running in the forest";
        StringRegexMatcher matcher = new StringRegexMatcher(expected, actual);
        matcher.match();
    }
}
