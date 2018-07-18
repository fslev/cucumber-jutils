package tests.ro.cucumber.core;

import ro.cucumber.core.matchers.JsonMatcher;
import ro.cucumber.core.matchers.MatcherException;
import ro.cucumber.core.matchers.MatcherWithAssignableSymbols;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

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

    @Test
    public void compareJsonArray() throws MatcherException {
        String expected = "{\"b\":\"val1\",\"a\":[1,2,3,4]}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        MatcherWithAssignableSymbols matcher = new JsonMatcher(expected, actual);
        assertTrue(matcher.getAssignSymbols().isEmpty());
        matcher.matches();
        assertTrue(matcher.getAssignSymbols().isEmpty());
    }

    @Test
    public void compareJsonArrayWithAssignSymbols() throws MatcherException {
        String expected = "{\"b\":\"val1\",\"a\":[2,\"~[sym1]\",4,\"~[sym2]\"]}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        MatcherWithAssignableSymbols matcher = new JsonMatcher(expected, actual);
        assertTrue(matcher.getAssignSymbols().isEmpty());
        matcher.matches();
        Map<String, String> symbols = matcher.getAssignSymbols();
        assertEquals("5", symbols.get("sym1"));
        assertEquals("3", symbols.get("sym2"));
        assertEquals(2, symbols.size());
    }

    @Test(expected = AssertionError.class)
    public void compareJsonArrayWithAssignSymbols_negative() throws MatcherException {
        String expected = "{\"b\":\"val1\",\"a\":[\"~[sym1]\",2,3,5]}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        MatcherWithAssignableSymbols matcher = new JsonMatcher(expected, actual);
        assertTrue(matcher.getAssignSymbols().isEmpty());
        matcher.matches();
    }

    @Test
    public void compareJsonWithAssignSymbolsAndDoNotFind() throws MatcherException {
        String expected = "{\"b\":\"!t~[sym1]1\"}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        MatcherWithAssignableSymbols matcher = new JsonMatcher(expected, actual);
        assertTrue(matcher.getAssignSymbols().isEmpty());
        matcher.matches();
        assertTrue(matcher.getAssignSymbols().isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void compareJsonWithAssignSymbolsAndDoNotFind_negative() throws MatcherException {
        String expected = "{\"b\":\"!v~[sym1]1\"}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        MatcherWithAssignableSymbols matcher = new JsonMatcher(expected, actual);
        matcher.matches();
    }
}
