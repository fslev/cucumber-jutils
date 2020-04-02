package com.cucumber.utils.engineering.utils;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class RegexUtilsTest {

    @Test
    public void testStringContainsSpecialRegexCharacters() {
        String s = "[0-9]";
        assertEquals(Collections.singletonList("["), RegexUtils.getRegexCharsFromString(s));
    }

    @Test
    public void testNullStringContainsSpecialRegexCharacters() {
        String s = null;
        assertEquals(Collections.emptyList(), RegexUtils.getRegexCharsFromString(s));
    }
}
