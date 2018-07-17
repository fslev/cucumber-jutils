package tests.ro.cucumber.core;

import org.junit.Test;
import ro.cucumber.core.matchers.MatcherWithAssignableSymbols;
import ro.cucumber.core.matchers.JsonMatcher;
import ro.cucumber.core.matchers.MatcherException;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonMatcherTests {

    @Test(expected = MatcherException.class)
    public void compareMalformedJson() throws MatcherException {
        String expected = "{\"!b\":val1\",\"a\":\"val2\"}";
        String actual = "{\"a\":\"val2\",\"c\":\"val1\"}";
        new JsonMatcher(expected, actual);
    }

    @Test
    public void compareSimpleJson() throws MatcherException {
        String expected = "{\"!b\":\"val1\",\"a\":\"val2\"}";
        String actual = "{\"a\":\"val2\",\"c\":\"val1\"}";
        MatcherWithAssignableSymbols matcher = new JsonMatcher(expected, actual);
        assertTrue(matcher.getAssignSymbols().isEmpty());
        matcher.matches();
        assertTrue(matcher.getAssignSymbols().isEmpty());
    }

    @Test
    public void compareSimpleJsonWithAssignSymbols() throws MatcherException {
        String expected = "{\"!b\":\"~[sym1]\",\"a\":\"~[sym2]\",\"c\":\"~[sym3]\"}";
        String actual = "{\"a\":\"val2\",\"d\":\"val3\",\"c\":\"val1\"}";
        MatcherWithAssignableSymbols matcher = new JsonMatcher(expected, actual);
        assertTrue(matcher.getAssignSymbols().isEmpty());
        matcher.matches();
        Map<String, String> symbols = matcher.getAssignSymbols();
        assertEquals("val2", symbols.get("sym2"));
        assertEquals("val1", symbols.get("sym3"));
        assertEquals(2, symbols.size());
    }
}
