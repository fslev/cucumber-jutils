package com.cucumber.utils.compare;

import com.cucumber.utils.engineering.compare.JsonCompare;
import com.cucumber.utils.engineering.compare.exceptions.CompareException;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonCompareTests {

    @Test(expected = CompareException.class)
    public void compareMalformedJson() throws CompareException {
        String expected = "{\"!b\":val1\",\"a\":\"val2\"}";
        String actual = "{\"a\":\"val2\",\"c\":\"val1\"}";
        new JsonCompare(expected, actual);
    }

    @Test
    public void compareJsonWithAssignSymbolsAndInvalidRegex() throws CompareException {
        String expected = "{\"b\":\"(~[sym1]\"}";
        String actual = "{\"a\":\"val2\",\"b\":\"(val1\"}";
        JsonCompare matcher = new JsonCompare(expected, actual);
        Map<String, String> symbols = matcher.compare();
        assertEquals("val1", symbols.get("sym1"));
        assertEquals(1, symbols.size());
    }

    @Test
    public void compareSimpleJson() throws CompareException {
        String expected = "{\"!b\":\"val1\",\"a\":\"val2\"}";
        String actual = "{\"a\":\"val2\",\"c\":\"val1\"}";
        JsonCompare matcher = new JsonCompare(expected, actual);
        Map<String, String> symbols = matcher.compare();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareSimpleJson_checkNoExtraFieldsExist() throws CompareException {
        String expected = "{\"a\":\"val2\",\"c\":\"val1\",\"!.*\":\".*\"}";
        String actual = "{\"a\":\"val2\",\"c\":\"val1\"}";
        JsonCompare matcher = new JsonCompare(expected, actual);
        Map<String, String> symbols = matcher.compare();
        assertTrue(symbols.isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void compareSimpleJson_checkNoExtraFieldsExistNegative() throws CompareException {
        String expected = "{\"a\":\"val2\",\"c\":\"val1\",\"!.*\":\".*\"}";
        String actual = "{\"a\":\"val2\",\"c\":\"val1\",\"d\":\"val1\"}";
        JsonCompare matcher = new JsonCompare(expected, actual);
        matcher.compare();
    }

    @Test
    public void compareSimpleJsonWithAssignSymbols() throws CompareException {
        String expected = "{\"!b\":\"~[sym1]\",\"a\":\"~[sym2]\",\"c\":\"~[sym3]\"}";
        String actual = "{\"a\":\"val2\",\"d\":\"val3\",\"c\":\"val1\"}";
        JsonCompare matcher = new JsonCompare(expected, actual);
        Map<String, String> symbols = matcher.compare();
        assertEquals("val2", symbols.get("sym2"));
        assertEquals("val1", symbols.get("sym3"));
        assertEquals(2, symbols.size());
    }

    @Test
    public void compareJsonArray() throws CompareException {
        String expected = "{\"b\":\"val1\",\"a\":[1,2,3,4]}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        JsonCompare matcher = new JsonCompare(expected, actual);
        Map<String, String> symbols = matcher.compare();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareJsonArray_checkNoExtraElementsExist() throws CompareException {
        String expected = "{\"b\":\"val1\",\"a\":[1,2,3,4,\"!.*\"]}";
        String actual = "{\"a\":[4,3,2,1],\"b\":\"val1\"}";
        JsonCompare matcher = new JsonCompare(expected, actual);
        Map<String, String> symbols = matcher.compare();
        assertTrue(symbols.isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void compareJsonArray_checkNoExtraElementsExist_negative() throws CompareException {
        String expected = "{\"b\":\"val1\",\"a\":[1,2,3,4,\"!.*\"]}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        JsonCompare matcher = new JsonCompare(expected, actual);
        matcher.compare();
    }

    @Test
    public void compareJsonArrayWithAssignSymbols() throws CompareException {
        String expected = "{\"b\":\"val1\",\"a\":[2,\"~[sym1]\",4,\"~[sym2]\"]}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        JsonCompare matcher = new JsonCompare(expected, actual);
        Map<String, String> symbols = matcher.compare();
        assertEquals("5", symbols.get("sym1"));
        assertEquals("3", symbols.get("sym2"));
        assertEquals(2, symbols.size());
    }

    @Test(expected = AssertionError.class)
    public void compareJsonArrayWithAssignSymbols_negative() throws CompareException {
        String expected = "{\"b\":\"val1\",\"a\":[\"~[sym1]\",2,3,5]}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        JsonCompare matcher = new JsonCompare(expected, actual);
        Map<String, String> symbols = matcher.compare();
        assertTrue(symbols.isEmpty());
        matcher.compare();
    }

    @Test
    public void compareJsonWithAssignSymbolsAndDoNotFind() throws CompareException {
        String expected = "{\"b\":\"!t~[sym1]1\"}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        JsonCompare matcher = new JsonCompare(expected, actual);
        Map<String, String> symbols = matcher.compare();
        assertTrue(symbols.isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void compareJsonWithAssignSymbolsAndDoNotFind_negative() throws CompareException {
        String expected = "{\"b\":\"!v~[sym1]1\"}";
        String actual = "{\"a\":[5,4,3,2,1],\"b\":\"val1\"}";
        JsonCompare matcher = new JsonCompare(expected, actual);
        matcher.compare();
    }

    @Test
    public void compareBigJsonWithAssignSymbols() throws CompareException {
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
        JsonCompare matcher = new JsonCompare(expected, actual);
        Map<String, String> symbols = matcher.compare();
        assertEquals("1", symbols.get("friendId"));
        assertEquals("-90.447286", symbols.get("longitude"));
        assertEquals(2, symbols.size());
    }

    @Test(expected = AssertionError.class)
    public void checkMessageFromSimpleJsonCompare() throws CompareException {
        String expected = "{\"a\":\"val2\",\"c\":\"val1\",\"!.*\":\".*\"}";
        String actual = "{\"a\":\"val2\",\"c\":\"val1\",\"d\":\"val1\"}";
        JsonCompare matcher = new JsonCompare("some msg", expected, actual);
        try {
            matcher.compare();
        } catch (AssertionError e) {
            assertTrue(e.getMessage().contains("some msg") && e.getMessage().contains("Expected:"));
            throw e;
        }
    }

    @Test
    public void testPlaceholderFillFromJsonCompareWithRegexSymbols() throws CompareException {
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
        JsonCompare matcher = new JsonCompare(expected, actual);
        Map<String, String> symbols = matcher.compare();
        assertEquals("Actual symbol result: " + symbols.get("businessKey"), "1551172176725.com|email-verif|1d55b4f3-6ec1-4d89-ba58-2ba2a3eaa80e", symbols.get("businessKey"));
        assertEquals(1, symbols.size());
    }
}
