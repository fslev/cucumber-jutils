package com.cucumber.utils.context.vars;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScenarioVarsParserTest {

    private ScenarioVars scenarioVars;

    @BeforeEach
    void init() {
        this.scenarioVars = new ScenarioVars();
    }

    @Test
    void testScenarioVarsDoesNotAcceptInvalidNames() {
        for (String invalid : new String[]{"a[d", "a#d", "a/d", "a,d", "a{d"}) {
            RuntimeException e = assertThrows(RuntimeException.class, () -> scenarioVars.put(invalid, 1));
            assertTrue(e.getMessage().contains("Invalid variable name"));
        }
    }

    @Test
    void testGlobalPlaceholderFillInSimpleText() {
        String a = "The #[animal] is running through the #[location]";
        Map<String, Object> values = new HashMap<>();
        values.put("animal", "chupacabra");
        values.put("location", "forest");

        scenarioVars.putAll(values);
        assertEquals(2, scenarioVars.size());
        assertTrue(scenarioVars.nameSet().containsAll(Arrays.asList("animal", "location")));
        assertEquals("The chupacabra is running through the forest", ScenarioVarsParser.parse(a, scenarioVars));
    }

    @Test
    void testGlobalPlaceholderFillInSimpleTextAndInvalidParameters() {
        String a = "The #[animal] is running through the #[location]";
        Map<String, Object> values = new HashMap<>();
        values.put("animals", "chupacabra");
        values.put("locations", "forest");
        scenarioVars.putAll(values);
        assertEquals("The #[animal] is running through the #[location]", ScenarioVarsParser.parse(a, scenarioVars));
    }

    @Test
    void testScenarioPlaceholderFillInSimpleText() {
        String a = "The #[animal] is running through the #[location]";
        Map<String, Object> values = new HashMap<>();
        values.put("animal", "chupacabra");
        values.put("location", "forest");
        scenarioVars.putAll(values);
        assertEquals("The chupacabra is running through the forest", ScenarioVarsParser.parse(a, scenarioVars));
    }

    @Test
    void testScenarioPlaceholderFillInSimpleTextAndInvalidParameters() {
        String a = "The #[animal] is running through the #[location]";
        Map<String, Object> values = new HashMap<>();
        values.put("animals", "chupacabra");
        values.put("locations", "forest");
        scenarioVars.putAll(values);
        assertEquals("The #[animal] is running through the #[location]", ScenarioVarsParser.parse(a, scenarioVars));
    }

    @Test
    void testGlobalAndScenarioPlaceholderFillInSimpleText() {
        String a = "The #[animal] is running through the #[location]";
        Map<String, Object> values = new HashMap<>();
        values.put("animal", "chupacabra");
        values.put("location", "forest");
        scenarioVars.putAll(values);
        assertEquals("The chupacabra is running through the forest", ScenarioVarsParser.parse(a, scenarioVars));
    }

    @Test
    void testGlobalAndScenarioPlaceholderFillInJson() {
        Map<String, Object> values = new HashMap<>();
        values.put("val1", "result 1");
        values.put("val2", "result 2");
        values.put("val3", "result 3");
        values.put("val4", "result 4");
        values.put("location", "forest");
        String actual = """
                [
                  {
                    "_id": "5b48fcdd5c3da373f5114ec7",
                    "index": 0,
                    "guid": "ed43e5b1-3da2-4ebe-baa5-e44d4eb18a5c",
                    "isActive": true,
                    "balance": "$3,812.47",
                    "picture": "http://placehold.it/32x32",
                    "age": 25,
                    "eyeColor": "#[val3]",
                    "name": "Tonya Schneider",
                    "gender": "female",
                    "company": "PHARMEX",
                    "email": "tonyaschneider@pharmex.com",
                    "#[val1]": "#[val2]",
                    "address": "195 Hubbard Place, #[val1], New Hampshire, 1382",
                    "about": "Occaecat laboris eu #[val1] fugiat. In dolore dolore esse voluptate. Amet ipsum id nisi nulla pariatur do dolore dolore aliquip qui laboris. Aute consequat tempor incididunt sunt voluptate laboris. Velit adipisicing nostrud laboris labore eiusmod. Dolore sint laborum culpa nulla eu sunt excepteur.\\r\\n",
                    "registered": "2015-05-21T05:11:55 -03:00",
                    "latitude": 67.096081,
                    "longitude": 36.71768,
                    "tags": [
                      "aute",
                      "dolore",
                      "#[val2]",
                      "officia",
                      "enim",
                      "aliqua",
                      "~[var3]"
                    ],
                    "friends": [
                      {
                        "id": 0,
                        "name": "~[val1] Hart"
                      },
                      {
                        "id": 1,
                        "name": "Velazquez Sargent"
                      },
                      {
                        "id": 2,
                        "name": "De~[var4]sa"
                      }
                    ],
                    "greeting": "Hello, Tonya Schneider! You have 9 unread messages.",
                    "~[val1]": "banana"
                  }
                ]""";
        String expected = """
                [
                  {
                    "_id": "5b48fcdd5c3da373f5114ec7",
                    "index": 0,
                    "guid": "ed43e5b1-3da2-4ebe-baa5-e44d4eb18a5c",
                    "isActive": true,
                    "balance": "$3,812.47",
                    "picture": "http://placehold.it/32x32",
                    "age": 25,
                    "eyeColor": "result 3",
                    "name": "Tonya Schneider",
                    "gender": "female",
                    "company": "PHARMEX",
                    "email": "tonyaschneider@pharmex.com",
                    "result 1": "result 2",
                    "address": "195 Hubbard Place, result 1, New Hampshire, 1382",
                    "about": "Occaecat laboris eu result 1 fugiat. In dolore dolore esse voluptate. Amet ipsum id nisi nulla pariatur do dolore dolore aliquip qui laboris. Aute consequat tempor incididunt sunt voluptate laboris. Velit adipisicing nostrud laboris labore eiusmod. Dolore sint laborum culpa nulla eu sunt excepteur.\\r\\n",
                    "registered": "2015-05-21T05:11:55 -03:00",
                    "latitude": 67.096081,
                    "longitude": 36.71768,
                    "tags": [
                      "aute",
                      "dolore",
                      "result 2",
                      "officia",
                      "enim",
                      "aliqua",
                      "~[var3]"
                    ],
                    "friends": [
                      {
                        "id": 0,
                        "name": "~[val1] Hart"
                      },
                      {
                        "id": 1,
                        "name": "Velazquez Sargent"
                      },
                      {
                        "id": 2,
                        "name": "De~[var4]sa"
                      }
                    ],
                    "greeting": "Hello, Tonya Schneider! You have 9 unread messages.",
                    "~[val1]": "banana"
                  }
                ]""";
        scenarioVars.putAll(values);
        String result = ScenarioVarsParser.parse(actual, scenarioVars).toString();
        assertEquals(expected, result);
    }

    @Test
    void testSimpleSpel() {
        String s = "#{T(java.net.IDN).toASCII('testá.com')}";
        assertEquals("xn--test-8na.com", ScenarioVarsParser.parse(s, scenarioVars));
        s = "Domain #{T(java.net.IDN).toASCII('testá.com')}-> idn";
        assertEquals("Domain xn--test-8na.com-> idn", ScenarioVarsParser.parse(s, scenarioVars));
    }

    @Test
    void testInvalidSpel() {
        String s = "This is: #{T(java.net.I).toASCII('testá.com')}";
        assertEquals("This is: #{T(java.net.I).toASCII('testá.com')}", ScenarioVarsParser.parse(s, scenarioVars));

        String s1 = "This is: \\#{T(java.net.I).toASCII('testá.com')}";
        assertEquals("This is: \\#{T(java.net.I).toASCII('testá.com')}", ScenarioVarsParser.parse(s1, scenarioVars));

        String s2 = "This is: #{1+2} and this is #{'#{'}1+2";
        assertEquals("This is: 3 and this is #{1+2", ScenarioVarsParser.parse(s2, scenarioVars));
    }

    @Test
    void testSpelGeneratesObject() {
        String s = "#{new Long(1000)}";
        assertInstanceOf(Long.class, ScenarioVarsParser.parse(s, scenarioVars));
    }

    @Test
    void testSpelWithScenarioVars() {
        scenarioVars.put("a", 1000);
        scenarioVars.put("b", 1);
        String s = "#{#[a]+#[b]}";
        assertEquals(1001, ScenarioVarsParser.parse(s, scenarioVars));
    }

    @Test
    void testEmptySpel() {
        String s = "#{}";
        assertEquals("#{}", ScenarioVarsParser.parse(s, scenarioVars));
    }

    @Test
    void testValidSpelExpWithEmbeddedInvalidSpelExp() {
        String s = "#{'a#{bc'+'def'}";
        assertEquals("a#{bcdef", ScenarioVarsParser.parse(s, scenarioVars));
        String s1 = "#{'a#{b}c'+'def'";
        assertEquals("#{'a#{b}c'+'def'", ScenarioVarsParser.parse(s1, scenarioVars));
    }

    @Test
    void testSpelWithEscapedBraces() {
        String s = "#{'a\\}bc'+'d\\}ef'}";
        assertEquals("a\\}bcd\\}ef", ScenarioVarsParser.parse(s, scenarioVars));
        s = "#{'a\\#{bc'+'d\\}ef'}";
        assertEquals("a\\#{bcd\\}ef", ScenarioVarsParser.parse(s, scenarioVars));
    }

    @Test
    void testSpelWithScenarioVarsHavingBraces() {
        scenarioVars.put("myJson", "{\"a\":1}");
        String s = "#{T(io.json.compare.util.JsonUtils).toJson('#[myJson]').get('a').asInt()}";
        assertEquals(1, ScenarioVarsParser.parse(s, scenarioVars));
    }
}
