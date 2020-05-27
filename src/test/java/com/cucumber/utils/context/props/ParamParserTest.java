package com.cucumber.utils.context.props;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ParamParserTest {

    private ScenarioProps scenarioProps;

    @Before
    public void init() {
        this.scenarioProps = new ScenarioProps();
    }

    @Test
    public void testGlobalPlaceholderFillInSimpleText() {
        String a = "The #[animal] is running through the #[location]";
        Map<String, Object> values = new HashMap<>();
        values.put("animal", "chupacabra");
        values.put("location", "forest");
        scenarioProps.putAll(values);
        assertEquals("The chupacabra is running through the forest", ParamParser.parse(a, scenarioProps));
    }

    @Test
    public void testGlobalPlaceholderFillInSimpleTextAndInvalidParameters() {
        String a = "The #[animal] is running through the #[location]";
        Map<String, Object> values = new HashMap<>();
        values.put("animals", "chupacabra");
        values.put("locations", "forest");
        scenarioProps.putAll(values);
        assertEquals("The #[animal] is running through the #[location]", ParamParser.parse(a, scenarioProps));
    }

    @Test
    public void testScenarioPlaceholderFillInSimpleText() {
        String a = "The #[animal] is running through the #[location]";
        Map<String, Object> values = new HashMap<>();
        values.put("animal", "chupacabra");
        values.put("location", "forest");
        scenarioProps.putAll(values);
        assertEquals("The chupacabra is running through the forest", ParamParser.parse(a, scenarioProps));
    }

    @Test
    public void testScenarioPlaceholderFillInSimpleTextAndInvalidParameters() {
        String a = "The #[animal] is running through the #[location]";
        Map<String, Object> values = new HashMap<>();
        values.put("animals", "chupacabra");
        values.put("locations", "forest");
        scenarioProps.putAll(values);
        assertEquals("The #[animal] is running through the #[location]", ParamParser.parse(a, scenarioProps));
    }

    @Test
    public void testGlobalAndScenarioPlaceholderFillInSimpleText() {
        String a = "The #[animal] is running through the #[location]";
        Map<String, Object> values = new HashMap<>();
        values.put("animal", "chupacabra");
        values.put("location", "forest");
        scenarioProps.putAll(values);
        assertEquals("The chupacabra is running through the forest", ParamParser.parse(a, scenarioProps));
    }

    @Test
    public void testGlobalAndScenarioPlaceholderFillInJson() {
        Map<String, Object> values = new HashMap<>();
        values.put("val1", "result 1");
        values.put("val2", "result 2");
        values.put("val3", "result 3");
        values.put("val4", "result 4");
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
                + "    \"eyeColor\": \"result 3\",\n" + "    \"name\": \"Tonya Schneider\",\n"
                + "    \"gender\": \"female\",\n" + "    \"company\": \"PHARMEX\",\n"
                + "    \"email\": \"tonyaschneider@pharmex.com\",\n"
                + "    \"result 1\": \"result 2\",\n"
                + "    \"address\": \"195 Hubbard Place, result 1, New Hampshire, 1382\",\n"
                + "    \"about\": \"Occaecat laboris eu result 1 fugiat. In dolore dolore esse voluptate. Amet ipsum id nisi nulla pariatur do dolore dolore aliquip qui laboris. Aute consequat tempor incididunt sunt voluptate laboris. Velit adipisicing nostrud laboris labore eiusmod. Dolore sint laborum culpa nulla eu sunt excepteur.\\r\\n\",\n"
                + "    \"registered\": \"2015-05-21T05:11:55 -03:00\",\n"
                + "    \"latitude\": 67.096081,\n" + "    \"longitude\": 36.71768,\n"
                + "    \"tags\": [\n" + "      \"aute\",\n" + "      \"dolore\",\n"
                + "      \"result 2\",\n" + "      \"officia\",\n" + "      \"enim\",\n"
                + "      \"aliqua\",\n" + "      \"~[var3]\"\n" + "    ],\n"
                + "    \"friends\": [\n" + "      {\n" + "        \"id\": 0,\n"
                + "        \"name\": \"~[val1] Hart\"\n" + "      },\n" + "      {\n"
                + "        \"id\": 1,\n" + "        \"name\": \"Velazquez Sargent\"\n"
                + "      },\n" + "      {\n" + "        \"id\": 2,\n"
                + "        \"name\": \"De~[var4]sa\"\n" + "      }\n" + "    ],\n"
                + "    \"greeting\": \"Hello, Tonya Schneider! You have 9 unread messages.\",\n"
                + "    \"~[val1]\": \"banana\"\n" + "  }\n" + "]";
        scenarioProps.putAll(values);
        String result = ParamParser.parse(actual, scenarioProps).toString();
        assertEquals(result, expected, result);
    }
}
