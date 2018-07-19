package tests.ro.cucumber.core;

import org.junit.Test;
import ro.cucumber.core.matchers.JsonMatcher;
import ro.cucumber.core.matchers.exceptions.MatcherException;

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
    public void compareJsonWithAssignSymbolsAndInvalidRegex() throws MatcherException {
        String expected = "{\"b\":\"(~[sym1]\"}";
        String actual = "{\"a\":\"val2\",\"b\":\"(val1\"}";
        JsonMatcher matcher = new JsonMatcher(expected, actual);
        Map<String, String> symbols = matcher.match();
        assertEquals("val1", symbols.get("sym1"));
        assertEquals(1, symbols.size());
    }

    @Test
    public void compareSimpleJson() throws MatcherException {
        String expected = "{\"!b\":\"val1\",\"a\":\"val2\"}";
        String actual = "{\"a\":\"val2\",\"c\":\"val1\"}";
        JsonMatcher matcher = new JsonMatcher(expected, actual);
        Map<String, String> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareSimpleJsonWithAssignSymbols() throws MatcherException {
        String expected = "{\"!b\":\"~[sym1]\",\"a\":\"~[sym2]\",\"c\":\"~[sym3]\"}";
        String actual = "{\"a\":\"val2\",\"d\":\"val3\",\"c\":\"val1\"}";
        JsonMatcher matcher = new JsonMatcher(expected, actual);
        Map<String, String> symbols = matcher.match();
        assertEquals("val2", symbols.get("sym2"));
        assertEquals("val1", symbols.get("sym3"));
        assertEquals(2, symbols.size());
    }

    @Test
    public void compareJsonArray() throws MatcherException {
        String expected = "{\"b\":\"val1\",\"a\":[1,2,3,4]}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        JsonMatcher matcher = new JsonMatcher(expected, actual);
        Map<String, String> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareJsonArrayWithAssignSymbols() throws MatcherException {
        String expected = "{\"b\":\"val1\",\"a\":[2,\"~[sym1]\",4,\"~[sym2]\"]}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        JsonMatcher matcher = new JsonMatcher(expected, actual);
        Map<String, String> symbols = matcher.match();
        assertEquals("5", symbols.get("sym1"));
        assertEquals("3", symbols.get("sym2"));
        assertEquals(2, symbols.size());
    }

    @Test(expected = AssertionError.class)
    public void compareJsonArrayWithAssignSymbols_negative() throws MatcherException {
        String expected = "{\"b\":\"val1\",\"a\":[\"~[sym1]\",2,3,5]}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        JsonMatcher matcher = new JsonMatcher(expected, actual);
        Map<String, String> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
        matcher.match();
    }

    @Test
    public void compareJsonWithAssignSymbolsAndDoNotFind() throws MatcherException {
        String expected = "{\"b\":\"!t~[sym1]1\"}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        JsonMatcher matcher = new JsonMatcher(expected, actual);
        Map<String, String> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void compareJsonWithAssignSymbolsAndDoNotFind_negative() throws MatcherException {
        String expected = "{\"b\":\"!v~[sym1]1\"}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        JsonMatcher matcher = new JsonMatcher(expected, actual);
        matcher.match();
    }

    @Test
    public void compareBigJsonWithAssignSymbols() throws MatcherException {
        String expected = "[\n" + "  {\n" + "    \"_id\": \"5b4fa3f8c2741fde34e4d5c8\",\n"
                + "    \"index\": 0,\n" + "    \"latitude\": -73.952152,\n"
                + "    \"longitude\": \"~[longitude]\",\n" + "    \"tags\": [\n"
                + "      \"irure\",\n" + "      \"et\",\n" + "      \"ex\",\n"
                + "      \"fugiat\",\n" + "      \"aute\",\n" + "      \"laboris\",\n"
                + "      \"sit\"\n" + "    ],\n" + "    \"friends\": [\n" + "      {\n"
                + "        \"id\": \"~[friendId]\",\n" + "        \"name\": \"Meagan Martinez\"\n"
                + "      },\n" + "      {\n" + "        \"id\": 2,\n"
                + "        \"name\": \"Sloan Yang\"\n" + "      }\n" + "    ],\n"
                + "    \"greeting\": \"Hello, Holly Hawkins! You have 1 unread messages.\",\n"
                + "    \"favoriteFruit\": \"banana\"\n" + "  }\n" + "]";
        String actual = "[\n" + "  {\n" + "    \"_id\": \"5b4fa3f8c2741fde34e4d5c8\",\n"
                + "    \"index\": 0,\n"
                + "    \"guid\": \"6bf5b919-ec09-444b-b9ec-fff820b9c591\",\n"
                + "    \"isActive\": false,\n" + "    \"balance\": \"$3,756.68\",\n"
                + "    \"picture\": \"http://placehold.it/32x32\",\n" + "    \"age\": 27,\n"
                + "    \"eyeColor\": \"brown\",\n" + "    \"name\": \"Holly Hawkins\",\n"
                + "    \"gender\": \"female\",\n" + "    \"company\": \"FUELTON\",\n"
                + "    \"email\": \"hollyhawkins@fuelton.com\",\n"
                + "    \"phone\": \"+1 (997) 554-3416\",\n"
                + "    \"address\": \"825 Powers Street, Noxen, Iowa, 7981\",\n"
                + "    \"about\": \"Ullamco sunt ex reprehenderit velit tempor nulla exercitation laborum consectetur ullamco veniam. Veniam est aliqua deserunt excepteur. Veniam fugiat laboris esse dolor deserunt. Reprehenderit sit velit anim laborum fugiat veniam occaecat exercitation occaecat commodo in quis sunt. Tempor mollit excepteur nulla voluptate aliqua sunt velit pariatur deserunt.\\r\\n\",\n"
                + "    \"registered\": \"2015-05-28T07:50:30 -03:00\",\n"
                + "    \"latitude\": -73.952152,\n" + "    \"longitude\": -90.447286,\n"
                + "    \"tags\": [\n" + "      \"irure\",\n" + "      \"et\",\n" + "      \"ex\",\n"
                + "      \"fugiat\",\n" + "      \"aute\",\n" + "      \"laboris\",\n"
                + "      \"sit\"\n" + "    ],\n" + "    \"friends\": [\n" + "      {\n"
                + "        \"id\": 0,\n" + "        \"name\": \"Jodie Gaines\"\n" + "      },\n"
                + "      {\n" + "        \"id\": 1,\n" + "        \"name\": \"Meagan Martinez\"\n"
                + "      },\n" + "      {\n" + "        \"id\": 2,\n"
                + "        \"name\": \"Sloan Yang\"\n" + "      }\n" + "    ],\n"
                + "    \"greeting\": \"Hello, Holly Hawkins! You have 1 unread messages.\",\n"
                + "    \"favoriteFruit\": \"banana\"\n" + "  }\n" + "]";
        JsonMatcher matcher = new JsonMatcher(expected, actual);
        Map<String, String> symbols = matcher.match();
        assertEquals("1", symbols.get("friendId"));
        assertEquals("-90.447286", symbols.get("longitude"));
        assertEquals(2, symbols.size());
    }
}
