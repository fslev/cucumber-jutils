package com.cucumber.utils.engineering.match;

import com.cucumber.utils.engineering.match.condition.MatchCondition;
import com.cucumber.utils.exceptions.InvalidTypeException;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonMatcherTests {

    @Test(expected = InvalidTypeException.class)
    public void compareMalformedJson() throws InvalidTypeException {
        String expected = "{\"!b\":val1\",\"a\":\"val2\"}";
        String actual = "{\"a\":\"val2\",\"c\":\"val1\"}";
        new JsonMatcher(null, expected, actual, null);
    }

    @Test(expected = InvalidTypeException.class)
    public void compareJsonWithNull() throws InvalidTypeException {
        String expected = "{\"b\":\"val1\",\"a\":\"val2\"}";
        String actual = null;
        new JsonMatcher(null, expected, actual, null).match();
    }

    @Test(expected = InvalidTypeException.class)
    public void compareJsonWithString() throws InvalidTypeException {
        String expected = "{\"a\":\"lorem ipsum\"}";
        String actual = "string";
        new JsonMatcher(null, expected, actual, null);
    }

    @Test
    public void compareJsonWithAssignSymbolsAndInvalidRegex() throws InvalidTypeException {
        String expected = "{\"b\":\"(~[sym1]\"}";
        String actual = "{\"a\":\"val2\",\"b\":\"(val1\"}";
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertEquals("val1", symbols.get("sym1"));
        assertEquals(1, symbols.size());
    }

    @Test
    public void compareSimpleJson() throws InvalidTypeException {
        String expected = "{\"!b\":\"val1\",\"a\":\"val2\"}";
        String actual = "{\"a\":\"val2\",\"c\":\"val1\"}";
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareJsonWithAssignSymbolsOnFields_in_depth() throws InvalidTypeException {
        String expected = "{\"a\":{\"abc-~[sym1]\":{\"o\":\"2\"},\"abc-~[sym2]\":{\"o\":\"0\"}}}";
        String actual = "{\"a\":{\"abc-X\":{\"o\":\"1\"},\"abc-Y\":{\"o\":\"0\"},\"abc-X\":{\"o\":\"2\"}}}";
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertEquals("X", symbols.get("sym1"));
        assertEquals("Y", symbols.get("sym2"));
        assertEquals(2, symbols.size());
    }

    @Test(expected = AssertionError.class)
    public void compareJsonWithAssignSymbolsOnFields_in_depth_negative() throws InvalidTypeException {
        String expected = "{\"a\":{\"abc-~[sym1]\":{\"o\":\"2\"},\"abc-~[sym2]\":{\"o\":\"U\"}}}";
        String actual = "{\"a\":{\"abc-X\":{\"o\":\"1\"},\"abc-Y\":{\"o\":\"0\"},\"abc-X\":{\"o\":\"2\"}}}";
        new JsonMatcher(null, expected, actual, null).match();
    }

    @Test
    public void compareSimpleJson_checkNoExtraFieldsExist() throws InvalidTypeException {
        String expected = "{\"a\":\"val2\",\"c\":\"val1\",\"!.*\":\".*\"}";
        String actual = "{\"a\":\"val2\",\"c\":\"val1\"}";
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void compareSimpleJson_checkNoExtraFieldsExistNegative() throws InvalidTypeException {
        String expected = "{\"a\":\"val2\",\"c\":\"val1\",\"!.*\":\".*\"}";
        String actual = "{\"a\":\"val2\",\"c\":\"val1\",\"d\":\"val1\"}";
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        matcher.match();
    }

    @Test
    public void compareSimpleJsonWithAssignSymbols() throws InvalidTypeException {
        String expected = "{\"!b\":\"~[sym1]\",\"a\":\"~[sym2]\",\"c\":\"~[sym3]\"}";
        String actual = "{\"a\":\"val2\",\"d\":\"val3\",\"c\":\"val1\"}";
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertEquals("val2", symbols.get("sym2"));
        assertEquals("val1", symbols.get("sym3"));
        assertEquals(2, symbols.size());
    }

    @Test
    public void compareJsonArray() throws InvalidTypeException {
        String expected = "{\"b\":\"val1\",\"a\":[1,2,3,4]}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareJsonArray_checkNoExtraElementsExist() throws InvalidTypeException {
        String expected = "{\"b\":\"val1\",\"a\":[1,2,3,4,\"!.*\"]}";
        String actual = "{\"a\":[4,3,2,1],\"b\":\"val1\"}";
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void compareJsonArray_checkNoExtraElementsExist_negative() throws InvalidTypeException {
        String expected = "{\"b\":\"val1\",\"a\":[1,2,3,4,\"!.*\"]}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        matcher.match();
    }

    @Test
    public void compareJsonArrayWithAssignSymbols() throws InvalidTypeException {
        String expected = "{\"b\":\"val1\",\"a\":[2,\"~[sym1]\",4,\"~[sym2]\"]}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertEquals("5", symbols.get("sym1"));
        assertEquals("3", symbols.get("sym2"));
        assertEquals(2, symbols.size());
    }

    @Test(expected = AssertionError.class)
    public void compareJsonArrayWithAssignSymbols_negative() throws InvalidTypeException {
        String expected = "{\"b\":\"val1\",\"a\":[\"~[sym1]\",2,3,5]}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
        matcher.match();
    }

    @Test
    public void compareJsonWithAssignSymbolsAndDoNotFind() throws InvalidTypeException {
        String expected = "{\"b\":\"!t~[sym1]1\"}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void compareJsonWithAssignSymbolsAndDoNotFind_negative() throws InvalidTypeException {
        String expected = "{\"b\":\"!v~[sym1]1\"}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        matcher.match();
    }

    @Test
    public void compareBigJsonWithAssignSymbols() throws InvalidTypeException {
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
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertEquals("1", symbols.get("friendId"));
        assertEquals("-90.447286", symbols.get("longitude"));
        assertEquals(2, symbols.size());
    }

    @Test(expected = AssertionError.class)
    public void checkMessageFromSimpleJsonCompare() throws InvalidTypeException {
        String expected = "{\"a\":\"val2\",\"c\":\"val1\",\"!.*\":\".*\"}";
        String actual = "{\"a\":\"val2\",\"c\":\"val1\",\"d\":\"val1\"}";
        JsonMatcher matcher = new JsonMatcher("some msg", expected, actual, null);
        try {
            matcher.match();
        } catch (AssertionError e) {
            assertTrue(e.getMessage().contains("some msg") && e.getMessage().contains("Expected:"));
            throw e;
        }
    }

    @Test
    public void testPlaceholderFillFromJsonCompareWithRegexSymbols() throws InvalidTypeException {
        String expected = "{\n" +
                "  \"/processes/running/ote_company/test-create-1551172176725.com/emailverification.*\": {\n" +
                "    \"businessKey\": \"tes.*reate-~[businessKey]\",\n" +
                "    \"type\": \"EMAIL_VERIFICATION\"\n" +
                "  }\n" +
                "}";
        String actual = "{\n" +
                "  \"/processes/running/ote_company/test-create-1551172176725.com/emailverification/3edb8eeb-b4e2-4b57-a6af-927fc1807b8e\": {\n" +
                "    \"businessKey\": \"test-create-1551172176725.com|email-verif|1d55b4f3-6ec1-4d89-ba58-2ba2a3eaa80e\",\n" +
                "    \"type\": \"EMAIL_VERIFICATION\"\n" +
                "  }\n" +
                "}";
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertEquals("Actual symbol result: " + symbols.get("businessKey"), "1551172176725.com|email-verif|1d55b4f3-6ec1-4d89-ba58-2ba2a3eaa80e", symbols.get("businessKey"));
        assertEquals(1, symbols.size());
    }

    @Test
    public void doNotMatchJsons() throws InvalidTypeException {
        String expected = "{\"a\":1}";
        String actual = "{\"a\":2}";
        new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.DO_NOT_MATCH))).match();
    }

    @Test(expected = AssertionError.class)
    public void doNotMatchJsons_negative() throws InvalidTypeException {
        String expected = "{\"a\":1}";
        String actual = "{\"a\":1, \"b\":2}";
        new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.DO_NOT_MATCH))).match();
    }

    @Test
    public void doNotMatchJsons_with_assign_symbols() throws InvalidTypeException {
        String expected = "{\"a\":\"~[val]\"}";
        String actual = "{\"b\":2}";
        Map<String, Object> props = new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.DO_NOT_MATCH))).match();
        assertEquals(0, props.size());
    }

    @Test
    public void doNotMatchNonExtensibleJsons() throws InvalidTypeException {
        String expected = "{\"a\":1}";
        String actual = "{\"a\":1, \"b\":2}";
        new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_NON_EXTENSIBLE_OBJECT, MatchCondition.DO_NOT_MATCH))).match();
    }

    @Test(expected = AssertionError.class)
    public void doNotMatchNonExtensibleJsons_negative() throws InvalidTypeException {
        String expected = "{\"b\":2, \"a\":1}";
        String actual = "{\"a\":1, \"b\":2}";
        new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_NON_EXTENSIBLE_OBJECT, MatchCondition.DO_NOT_MATCH))).match();
    }

    @Test
    public void matchNonExtensibleJsonArrays() throws InvalidTypeException {
        String expected = "{\"a\":{\"b\":[1, true]}}";
        String actual = "{\"a\":{\"c\":0, \"b\":[1, true]}}";
        new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_NON_EXTENSIBLE_ARRAY))).match();
    }

    @Test(expected = AssertionError.class)
    public void matchNonExtensibleJsonArrays_negative() throws InvalidTypeException {
        String expected = "{\"a\":{\"b\":[1]}}";
        String actual = "{\"a\":{\"c\":0, \"b\":[1, true]}}";
        new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_NON_EXTENSIBLE_ARRAY))).match();
    }

    @Test
    public void doNotMatchNonExtensibleJsonArrays() throws InvalidTypeException {
        String expected = "{\"a\":{\"b\":[1]}}";
        String actual = "{\"a\":{\"c\":0, \"b\":[1, true]}}";
        new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_NON_EXTENSIBLE_ARRAY, MatchCondition.DO_NOT_MATCH))).match();
    }

    @Test(expected = AssertionError.class)
    public void doNotMatchNonExtensibleJsonArrays_negative() throws InvalidTypeException {
        String expected = "{\"a\":{\"b\":[1, true]}}";
        String actual = "{\"a\":{\"c\":0, \"b\":[1, true]}}";
        new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_NON_EXTENSIBLE_ARRAY, MatchCondition.DO_NOT_MATCH))).match();
    }

    @Test
    public void matchJsonArraysStrictOrder() throws InvalidTypeException {
        String expected = "{\"a\":{\"b\":[1, true]}}";
        String actual = "{\"a\":{\"c\":0, \"b\":[1, true, false]}}";
        new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_STRICT_ORDER_ARRAY))).match();
    }

    @Test(expected = AssertionError.class)
    public void matchJsonArraysStrictOrder_negative() throws InvalidTypeException {
        String expected = "{\"a\":{\"b\":[1, false]}}";
        String actual = "{\"a\":{\"c\":0, \"b\":[1, true, false]}}";
        new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_STRICT_ORDER_ARRAY))).match();
    }

    @Test
    public void doNotMatchJsonArraysStrictOrder() throws InvalidTypeException {
        String expected = "{\"a\":{\"b\":[1, false]}}";
        String actual = "{\"a\":{\"c\":0, \"b\":[1, true, false]}}";
        new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_STRICT_ORDER_ARRAY,
                MatchCondition.DO_NOT_MATCH))).match();
    }

    @Test
    public void matchJsonNonExtensibleAndArraysStrictOrder() throws InvalidTypeException {
        String expected = "{\"a\":{\"d\":null, \"b\":[1, true]}, \"c\":0}";
        String actual = "{\"c\":0, \"a\":{\"b\":[1, true], \"d\":null}}";
        new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_STRICT_ORDER_ARRAY,
                MatchCondition.JSON_NON_EXTENSIBLE_OBJECT, MatchCondition.JSON_NON_EXTENSIBLE_ARRAY))).match();
    }

    @Test(expected = AssertionError.class)
    public void matchJsonNonExtensibleAndArraysStrictOrder_negative() throws InvalidTypeException {
        String expected = "{\"a\":{\"d\":null, \"b\":[1, true]}, \"c\":0}";
        String actual = "{\"c\":0, \"a\":{\"b\":[true, 1], \"d\":null}}";
        new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_STRICT_ORDER_ARRAY,
                MatchCondition.JSON_NON_EXTENSIBLE_OBJECT, MatchCondition.JSON_NON_EXTENSIBLE_ARRAY))).match();
    }

    @Test
    public void doNotMatchJsonNonExtensibleAndArraysStrictOrder() throws InvalidTypeException {
        String expected = "{\"a\":{\"d\":null, \"b\":[1, true]}, \"c\":0}";
        String actual = "{\"c\":0, \"a\":{\"b\":[1, true], \"d\":null, \"e\":null}}";
        new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_STRICT_ORDER_ARRAY,
                MatchCondition.JSON_NON_EXTENSIBLE_OBJECT, MatchCondition.JSON_NON_EXTENSIBLE_ARRAY, MatchCondition.DO_NOT_MATCH))).match();
    }

    @Test(expected = AssertionError.class)
    public void doNotMatchJsonNonExtensibleAndArraysStrictOrder_negative() throws InvalidTypeException {
        String expected = "{\"a\":{\"d\":null, \"b\":[1, true]}, \"c\":0}";
        String actual = "{\"c\":0, \"a\":{\"b\":[1, true], \"d\":null}}";
        new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_STRICT_ORDER_ARRAY,
                MatchCondition.JSON_NON_EXTENSIBLE_OBJECT, MatchCondition.JSON_NON_EXTENSIBLE_ARRAY, MatchCondition.DO_NOT_MATCH))).match();
    }
}
