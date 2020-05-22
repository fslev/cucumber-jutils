package com.cucumber.utils.compare;

import com.cucumber.utils.engineering.compare.StringRegexCompare;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class StringRegexCompareTests {

    @Test
    public void compareSimpleString() {
        String expected = "result";
        String actual = "result";
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        Map<String, Object> symbols = matcher.compare();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareSimpleInt() {
        int expected = 1;
        int actual = 1;
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        Map<String, Object> symbols = matcher.compare();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareSimpleIntWithAssignSymbols() {
        String expected = "~[sym1]";
        int actual = 10;
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        Map<String, Object> symbols = matcher.compare();
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
        Map<String, Object> symbols = matcher.compare();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareSimpleBoolean() {
        boolean expected = true;
        Boolean actual = true;
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        Map<String, Object> symbols = matcher.compare();
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
        String actual = "result";
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        matcher.compare();
    }

    @Test
    public void compareInvalidRegex() {
        String expected = "va(lue";
        String actual = "va(lue";
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        Map<String, Object> symbols = matcher.compare();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareRegex() {
        String expected = "va.*ue";
        String actual = "va(lue";
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        Map<String, Object> symbols = matcher.compare();
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
        Map<String, Object> symbols = matcher.compare();
        assertEquals("rabbit", symbols.get("sym1"));
        assertEquals("forest", symbols.get("sym2"));
        assertEquals(2, symbols.size());
    }

    @Test
    public void compareRegexWithAssignSymbols() {
        String expected = ".* Rabbit ~[sym1] in the .*";
        String actual = "The Rabbit is running in the forest";
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        Map<String, Object> symbols = matcher.compare();
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

    @Test
    public void compareStringWithAssignSymbolsAgainstStringWithRegexCharacters() {
        String expected = "This is regex ~[regex]";
        String actual = "This is regex a|b|c|d";
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        Map<String, Object> symbols = matcher.compare();
        assertEquals("a|b|c|d", symbols.get("regex"));
        assertEquals(1, symbols.size());
    }

    @Test
    public void compareStringWithAssignSymbolsAndRegexAgainstStringWithRegexCharacters() {
        String expected = ".* is regex ~[regex]";
        String actual = "This is regex a|b|c|d";
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        Map<String, Object> symbols = matcher.compare();
        assertEquals("a|b|c|d", symbols.get("regex"));
        assertEquals(1, symbols.size());
    }

    @Test
    public void compareStringWithAssignSymbolsAndSeparateRegexAgainstStringWithRegexCharacters() {
        String expected = ".* is regex ~[regex] \\Q[0-9]*\\E";
        String actual = "This is regex a|b|c|d [0-9]*";
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        Map<String, Object> symbols = matcher.compare();
        assertEquals("a|b|c|d", symbols.get("regex"));
        assertEquals(1, symbols.size());
    }

    @Test
    public void compareStringWithAssignSymbolsAndSeparateRegexAgainstStringWithRegexCharacters_negaive() {
        String expected = ".* is regex ~[regex] [0-9]*";
        String actual = "This is regex a|b|c|d [0-9]*";
        try {
            new StringRegexCompare(expected, actual).compare();
        } catch (AssertionError e) {
            assertEquals("\nEXPECTED:\n" +
                    ".* is regex \\Qa|b|c|d\\E [0-9]*\n" +
                    "BUT GOT:\n" +
                    "This is regex a|b|c|d [0-9]*\n", e.getMessage());
            return;
        }
        fail("Values should not match ! But they do...");
    }

    @Test
    public void compareStringWithAssignSymbolsAndRegexAgainstStringWithRegexCharacters_negative() {
        String expected = ".* is regex ~[regex]lorem";
        String actual = "This is regex a|b|c|d";
        try {
            new StringRegexCompare(expected, actual).compare();
        } catch (AssertionError e) {
            assertEquals("\n" +
                    "EXPECTED:\n" +
                    ".* is regex ~[regex]lorem\n" +
                    "BUT GOT:\n" +
                    "This is regex a|b|c|d\n", e.getMessage());
            return;
        }
        fail("Values should not match ! But they do...");
    }

    @Test
    public void compareStringWithAssignSymbolsAndInvalidRegexAgainstStringWithRegexCharacters() {
        String expected = "[ is regex ~[regex]";
        String actual = "[ is regex a|b|c|d";
        StringRegexCompare matcher = new StringRegexCompare(expected, actual);
        Map<String, Object> symbols = matcher.compare();
        assertEquals("a|b|c|d", symbols.get("regex"));
        assertEquals(1, symbols.size());
    }

}
