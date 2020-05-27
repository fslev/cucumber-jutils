package com.cucumber.utils.context.props.internal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SpelParserTest {

    @Test
    public void emptySourceTest() {
        assertNull(SpelParser.parse(null));
        assertEquals(SpelParser.parse(""), "");
    }

    @Test
    public void testSimpleParse() {
        String s = "#{T(java.net.IDN).toASCII('testá.com')}";
        assertEquals("xn--test-8na.com", SpelParser.parse(s));
    }


    @Test
    public void invalidSpelExpression() {
        String s = "#{(java.net.IDN).toASCII('testá.com')}";
        assertEquals("(java.net.IDN).toASCII('testá.com')", SpelParser.parse(s));
    }

}
