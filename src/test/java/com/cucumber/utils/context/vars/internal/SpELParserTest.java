package com.cucumber.utils.context.vars.internal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SpELParserTest {

    @Test
    public void emptySourceTest() {
        assertEquals(SpELParser.parseQuietly(""), "");
    }

    @Test
    public void testSimpleParse() {
        String s = "#{T(java.net.IDN).toASCII('testá.com')}";
        assertEquals("xn--test-8na.com", SpELParser.parseQuietly(s));
    }

    @Test
    public void testEscapedExpressions() {
        String s = "two plus three is #{2+3} and one two plus two is #{2+2}";
        assertEquals("two plus three is 5 and one two plus two is 4", SpELParser.parseQuietly(s));

        // one escaped expression prefix
        s = "two plus three is \\#{2+3} and one two plus two is #{2+2}";
        assertEquals("two plus three is \\5 and one two plus two is 4", SpELParser.parseQuietly(s));

        // two escaped expression prefixes
        s = "two plus three is \\#{2+3} and one two plus two is \\#{2+2}";
        assertEquals("two plus three is \\5 and one two plus two is \\4", SpELParser.parseQuietly(s));
    }

    @Test
    public void invalidSpelExpression() {
        String s = "#{(java.net.IDN).toASCII('testá.com')}";
        assertEquals("#{(java.net.IDN).toASCII('testá.com')}", SpELParser.parseQuietly(s));
    }

    @Test
    public void multipleInvalidSpelExpressions() {
        String s = "test #{1+2} and #{invalid";
        assertEquals("test #{1+2} and #{invalid", SpELParser.parseQuietly(s));

        s = "test #{1+2} and #{'#{'}invalid";
        assertEquals("test 3 and #{invalid", SpELParser.parseQuietly(s));
    }

    @Test
    public void invalidSpelContent() {
        String s = "T(invalid.net.IDN).toASCII('testá.com')";
        assertEquals("T(invalid.net.IDN).toASCII('testá.com')", SpELParser.parseQuietly(s));
    }

    @Test
    public void spELGeneratesNull() {
        assertNull(SpELParser.parseQuietly("#{T(com.cucumber.utils.context.vars.internal.SpELParserTest).returnsNull()}"));
    }

    @Test
    public void testSpelParsingOfMultipleExpressions() {
        String s = "#{T(java.net.IDN).toASCII('testá.com')}#{T(java.net.IDN).toASCII('testá.com')}";
        assertEquals("xn--test-8na.comxn--test-8na.com", SpELParser.parseQuietly(s));
    }

    @Test
    public void testSpelParsingOfMultipleExpressionsWithOneInvalidDelimited() {
        String s = "#{T(java.net.IDN).toASCII('testá.com')}#{'#{'}T(java.net.IDN).toASCII('testá.com')";
        assertEquals("xn--test-8na.com#{T(java.net.IDN).toASCII('testá.com')", SpELParser.parseQuietly(s));
    }

    @Test
    public void testSpelParsingOfMultipleExpressionsWithOneInvalid() {
        String s = "#{T(java.net.IDN).toASCII('testá.com')}#{'#{'}T(jav.net.IDN).toASCII('testá.com')}";
        assertEquals("xn--test-8na.com#{T(jav.net.IDN).toASCII('testá.com')}", SpELParser.parseQuietly(s));
    }

    @Test
    public void testSpelParsingOfMultipleExpressionsWithOneLiteral() {
        String s = "#{T(java.net.IDN).toASCII('testá.com')}#{'#{T(java.net.IDN).toASCII(''testá.com'')}'}";
        assertEquals("xn--test-8na.com#{T(java.net.IDN).toASCII('testá.com')}", SpELParser.parseQuietly(s));
    }

    @Test
    public void testSpelParsingOfExpressionContainingBackslash() {
        String s = "#{('a\\Bc'+'d\\Ef').toLowerCase()}#{('g\\hi').toLowerCase()}";
        assertEquals("a\\bcd\\efg\\hi", SpELParser.parseQuietly(s));
    }

    @Test
    public void testSpelParsingOfExpressionContainingEscapedBraces() {
        String s = "#{'abcD\\}EF'.toLowerCase()} and #{'abcD\\}EF'.toLowerCase()}";
        assertEquals("abcd\\}ef and abcd\\}ef", SpELParser.parseQuietly(s));
    }

    public static Object returnsNull() {
        return null;
    }
}
