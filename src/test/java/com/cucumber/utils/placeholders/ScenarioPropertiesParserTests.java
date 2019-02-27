package com.cucumber.utils.placeholders;

import com.cucumber.utils.engineering.placeholders.AbstractPropertiesParser;
import com.cucumber.utils.engineering.placeholders.ScenarioPropertiesParser;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ScenarioPropertiesParserTests {

    @Test
    public void testGlobalPlaceholderFillInSimpleText() {
        String a = "The #[animal] is running through the #[location]";
        AbstractPropertiesParser parser = new ScenarioPropertiesParser(a);
        Map<String, String> values = new HashMap<>();
        values.put("animal", "chupacabra");
        values.put("location", "forest");
        assertEquals("The chupacabra is running through the forest", parser.parse(values));
    }

    @Test
    public void testGlobalPlaceholderFillInSimpleTextAndInvalidParameters() {
        String a = "The #[animal] is running through the #[location]";
        AbstractPropertiesParser parser = new ScenarioPropertiesParser(a);
        Map<String, String> values = new HashMap<>();
        values.put("animals", "chupacabra");
        values.put("locations", "forest");
        assertEquals("The #[animal] is running through the #[location]", parser.parse(values));
    }

    @Test
    public void testScenarioPlaceholderFillInSimpleText() {
        String a = "The #[animal] is running through the #[location]";
        AbstractPropertiesParser parser = new ScenarioPropertiesParser(a);
        Map<String, String> values = new HashMap<>();
        values.put("animal", "chupacabra");
        values.put("location", "forest");
        assertEquals("The chupacabra is running through the forest", parser.parse(values));
    }

    @Test
    public void testScenarioPlaceholderFillInSimpleTextAndInvalidParameters() {
        String a = "The #[animal] is running through the #[location]";
        AbstractPropertiesParser parser = new ScenarioPropertiesParser(a);
        Map<String, String> values = new HashMap<>();
        values.put("animals", "chupacabra");
        values.put("locations", "forest");
        assertEquals("The #[animal] is running through the #[location]", parser.parse(values));
        assertEquals(new HashSet<>(Arrays.asList("animal", "location")), parser.getPropertyNames());
    }

    @Test
    public void testGlobalAndScenarioPlaceholderFillInSimpleText() {
        String a = "The #[animal] is running through the #[location]";
        AbstractPropertiesParser globalParser = new ScenarioPropertiesParser(a);
        Map<String, String> values = new HashMap<>();
        values.put("animal", "chupacabra");
        values.put("location", "forest");
        AbstractPropertiesParser scenarioParser = new ScenarioPropertiesParser(globalParser.parse(values));
        assertEquals("The chupacabra is running through the forest", scenarioParser.parse(values));
    }

    @Test
    public void testGlobalAndScenarioPlaceholderFillInJson() {
        Map<String, String> values = new HashMap<>();
        values.put("val1", "getParsedTarget 1");
        values.put("val2", "getParsedTarget 2");
        values.put("val3", "getParsedTarget 3");
        values.put("val4", "getParsedTarget 4");
        values.put("location", "forest");
        String actual = "[\n" + "  {\n" + "    \"_id\": \"5b48fcdd5c3da373f5114ec7\",\n"
                + "    \"index\": 0,\n"
                + "    \"guid\": \"ed43e5b1-3da2-4ebe-baa5-e44d4eb18a5c\",\n"
                + "    \"isActive\": true,\n" + "    \"balance\": \"$3,812.47\",\n"
                + "    \"picture\": \"http://placehold.it/32x32\",\n" + "    \"age\": 25,\n"
                + "    \"eyeColor\": \"#[val3]\",\n" + "    \"name\": \"Tonya Schneider\",\n"
                + "    \"gender\": \"female\",\n" + "    \"company\": \"PHARMEX\",\n"
                + "    \"email\": \"tonyaschneider@pharmex.com\",\n"
                + "    \"#[val1]\": \"#[val2]\",\n"
                + "    \"address\": \"195 Hubbard Place, #[val1], New Hampshire, 1382\",\n"
                + "    \"about\": \"Occaecat laboris eu #[val1] fugiat. In dolore dolore esse voluptate. Amet ipsum id nisi nulla pariatur do dolore dolore aliquip qui laboris. Aute consequat tempor incididunt sunt voluptate laboris. Velit adipisicing nostrud laboris labore eiusmod. Dolore sint laborum culpa nulla eu sunt excepteur.\\r\\n\",\n"
                + "    \"registered\": \"2015-05-21T05:11:55 -03:00\",\n"
                + "    \"latitude\": 67.096081,\n" + "    \"longitude\": 36.71768,\n"
                + "    \"tags\": [\n" + "      \"aute\",\n" + "      \"dolore\",\n"
                + "      \"#[val2]\",\n" + "      \"officia\",\n" + "      \"enim\",\n"
                + "      \"aliqua\",\n" + "      \"~[var3]\"\n" + "    ],\n"
                + "    \"friends\": [\n" + "      {\n" + "        \"id\": 0,\n"
                + "        \"name\": \"~[val1] Hart\"\n" + "      },\n" + "      {\n"
                + "        \"id\": 1,\n" + "        \"name\": \"Velazquez Sargent\"\n"
                + "      },\n" + "      {\n" + "        \"id\": 2,\n"
                + "        \"name\": \"De~[var4]sa\"\n" + "      }\n" + "    ],\n"
                + "    \"greeting\": \"Hello, Tonya Schneider! You have 9 unread messages.\",\n"
                + "    \"~[val1]\": \"banana\"\n" + "  }\n" + "]";
        String expected = "[\n" + "  {\n" + "    \"_id\": \"5b48fcdd5c3da373f5114ec7\",\n"
                + "    \"index\": 0,\n"
                + "    \"guid\": \"ed43e5b1-3da2-4ebe-baa5-e44d4eb18a5c\",\n"
                + "    \"isActive\": true,\n" + "    \"balance\": \"$3,812.47\",\n"
                + "    \"picture\": \"http://placehold.it/32x32\",\n" + "    \"age\": 25,\n"
                + "    \"eyeColor\": \"getParsedTarget 3\",\n" + "    \"name\": \"Tonya Schneider\",\n"
                + "    \"gender\": \"female\",\n" + "    \"company\": \"PHARMEX\",\n"
                + "    \"email\": \"tonyaschneider@pharmex.com\",\n"
                + "    \"getParsedTarget 1\": \"getParsedTarget 2\",\n"
                + "    \"address\": \"195 Hubbard Place, getParsedTarget 1, New Hampshire, 1382\",\n"
                + "    \"about\": \"Occaecat laboris eu getParsedTarget 1 fugiat. In dolore dolore esse voluptate. Amet ipsum id nisi nulla pariatur do dolore dolore aliquip qui laboris. Aute consequat tempor incididunt sunt voluptate laboris. Velit adipisicing nostrud laboris labore eiusmod. Dolore sint laborum culpa nulla eu sunt excepteur.\\r\\n\",\n"
                + "    \"registered\": \"2015-05-21T05:11:55 -03:00\",\n"
                + "    \"latitude\": 67.096081,\n" + "    \"longitude\": 36.71768,\n"
                + "    \"tags\": [\n" + "      \"aute\",\n" + "      \"dolore\",\n"
                + "      \"getParsedTarget 2\",\n" + "      \"officia\",\n" + "      \"enim\",\n"
                + "      \"aliqua\",\n" + "      \"~[var3]\"\n" + "    ],\n"
                + "    \"friends\": [\n" + "      {\n" + "        \"id\": 0,\n"
                + "        \"name\": \"~[val1] Hart\"\n" + "      },\n" + "      {\n"
                + "        \"id\": 1,\n" + "        \"name\": \"Velazquez Sargent\"\n"
                + "      },\n" + "      {\n" + "        \"id\": 2,\n"
                + "        \"name\": \"De~[var4]sa\"\n" + "      }\n" + "    ],\n"
                + "    \"greeting\": \"Hello, Tonya Schneider! You have 9 unread messages.\",\n"
                + "    \"~[val1]\": \"banana\"\n" + "  }\n" + "]";
        AbstractPropertiesParser scenarioParser = new ScenarioPropertiesParser(actual);
        String result = scenarioParser.parse(values);
        assertEquals(result, expected, result);
        assertEquals(new HashSet<>(Arrays.asList("val3", "val2", "val1")), scenarioParser.getPropertyNames());
    }
}
