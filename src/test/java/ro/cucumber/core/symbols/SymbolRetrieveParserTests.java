package ro.cucumber.core.symbols;

import org.junit.Test;
import ro.cucumber.core.engineering.placeholder.AbstractPlaceholderFill;
import ro.cucumber.core.engineering.placeholder.GlobalPlaceholderFill;
import ro.cucumber.core.engineering.placeholder.ScenarioPlaceholderFill;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SymbolRetrieveParserTests {

    @Test
    public void testGlobalSymbolRetrieveInSimpleText() {
        String a = "The ${animal} is running through the ${location}";
        AbstractPlaceholderFill parser = new GlobalPlaceholderFill(a);
        Map<String, String> values = new HashMap<>();
        values.put("animal", "chupacabra");
        values.put("location", "forest");
        assertEquals("The chupacabra is running through the forest", parser.fill(values));
    }

    @Test
    public void testGlobalSymbolRetrieveInSimpleTextAndInvalidParameters() {
        String a = "The ${animal} is running through the ${location}";
        AbstractPlaceholderFill parser = new GlobalPlaceholderFill(a);
        Map<String, String> values = new HashMap<>();
        values.put("animals", "chupacabra");
        values.put("locations", "forest");
        assertEquals("The ${animal} is running through the ${location}", parser.fill(values));
    }

    @Test
    public void testScenarioSymbolRetrieveInSimpleText() {
        String a = "The #[animal] is running through the #[location]";
        AbstractPlaceholderFill parser = new ScenarioPlaceholderFill(a);
        Map<String, String> values = new HashMap<>();
        values.put("animal", "chupacabra");
        values.put("location", "forest");
        assertEquals("The chupacabra is running through the forest", parser.fill(values));
    }

    @Test
    public void testScenarioSymbolRetrieveInSimpleTextAndInvalidParameters() {
        String a = "The #[animal] is running through the #[location]";
        AbstractPlaceholderFill parser = new ScenarioPlaceholderFill(a);
        Map<String, String> values = new HashMap<>();
        values.put("animals", "chupacabra");
        values.put("locations", "forest");
        assertEquals("The #[animal] is running through the #[location]", parser.fill(values));
        assertEquals(new HashSet<>(Arrays.asList("animal", "location")), parser.searchForPlaceholders());
    }

    @Test
    public void testGlobalAndScenarioSymbolRetrieveInSimpleText() {
        String a = "The ${animal} is running through the #[location]";
        AbstractPlaceholderFill globalParser = new GlobalPlaceholderFill(a);
        Map<String, String> values = new HashMap<>();
        values.put("animal", "chupacabra");
        values.put("location", "forest");
        AbstractPlaceholderFill scenarioParser = new ScenarioPlaceholderFill(globalParser.fill(values));
        assertEquals("The chupacabra is running through the forest", scenarioParser.fill(values));
    }

    @Test
    public void testGlobalAndScenarioSymbolRetrieveInJson() {
        Map<String, String> values = new HashMap<>();
        values.put("val1", "value 1");
        values.put("val2", "value 2");
        values.put("val3", "value 3");
        values.put("val4", "value 4");
        values.put("location", "forest");
        String actual = "[\n" + "  {\n" + "    \"_id\": \"5b48fcdd5c3da373f5114ec7\",\n"
                + "    \"index\": 0,\n"
                + "    \"guid\": \"ed43e5b1-3da2-4ebe-baa5-e44d4eb18a5c\",\n"
                + "    \"isActive\": true,\n" + "    \"balance\": \"$3,812.47\",\n"
                + "    \"picture\": \"http://placehold.it/32x32\",\n" + "    \"age\": 25,\n"
                + "    \"eyeColor\": \"#[val3]\",\n" + "    \"name\": \"Tonya Schneider\",\n"
                + "    \"gender\": \"female\",\n" + "    \"company\": \"PHARMEX\",\n"
                + "    \"email\": \"tonyaschneider@pharmex.com\",\n"
                + "    \"${val1}\": \"#[val2]\",\n"
                + "    \"address\": \"195 Hubbard Place, ${val1}, New Hampshire, 1382\",\n"
                + "    \"about\": \"Occaecat laboris eu #[val1] fugiat. In dolore dolore esse voluptate. Amet ipsum id nisi nulla pariatur do dolore dolore aliquip qui laboris. Aute consequat tempor incididunt sunt voluptate laboris. Velit adipisicing nostrud laboris labore eiusmod. Dolore sint laborum culpa nulla eu sunt excepteur.\\r\\n\",\n"
                + "    \"registered\": \"2015-05-21T05:11:55 -03:00\",\n"
                + "    \"latitude\": 67.096081,\n" + "    \"longitude\": 36.71768,\n"
                + "    \"tags\": [\n" + "      \"aute\",\n" + "      \"dolore\",\n"
                + "      \"${val2}\",\n" + "      \"officia\",\n" + "      \"enim\",\n"
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
                + "    \"eyeColor\": \"value 3\",\n" + "    \"name\": \"Tonya Schneider\",\n"
                + "    \"gender\": \"female\",\n" + "    \"company\": \"PHARMEX\",\n"
                + "    \"email\": \"tonyaschneider@pharmex.com\",\n"
                + "    \"value 1\": \"value 2\",\n"
                + "    \"address\": \"195 Hubbard Place, value 1, New Hampshire, 1382\",\n"
                + "    \"about\": \"Occaecat laboris eu value 1 fugiat. In dolore dolore esse voluptate. Amet ipsum id nisi nulla pariatur do dolore dolore aliquip qui laboris. Aute consequat tempor incididunt sunt voluptate laboris. Velit adipisicing nostrud laboris labore eiusmod. Dolore sint laborum culpa nulla eu sunt excepteur.\\r\\n\",\n"
                + "    \"registered\": \"2015-05-21T05:11:55 -03:00\",\n"
                + "    \"latitude\": 67.096081,\n" + "    \"longitude\": 36.71768,\n"
                + "    \"tags\": [\n" + "      \"aute\",\n" + "      \"dolore\",\n"
                + "      \"value 2\",\n" + "      \"officia\",\n" + "      \"enim\",\n"
                + "      \"aliqua\",\n" + "      \"~[var3]\"\n" + "    ],\n"
                + "    \"friends\": [\n" + "      {\n" + "        \"id\": 0,\n"
                + "        \"name\": \"~[val1] Hart\"\n" + "      },\n" + "      {\n"
                + "        \"id\": 1,\n" + "        \"name\": \"Velazquez Sargent\"\n"
                + "      },\n" + "      {\n" + "        \"id\": 2,\n"
                + "        \"name\": \"De~[var4]sa\"\n" + "      }\n" + "    ],\n"
                + "    \"greeting\": \"Hello, Tonya Schneider! You have 9 unread messages.\",\n"
                + "    \"~[val1]\": \"banana\"\n" + "  }\n" + "]";
        AbstractPlaceholderFill globalParser = new GlobalPlaceholderFill(actual);
        AbstractPlaceholderFill scenarioParser = new ScenarioPlaceholderFill(globalParser.fill(values));
        String result = scenarioParser.fill(values);
        assertEquals(result, expected, result);
        assertEquals(new HashSet<>(Arrays.asList("val3", "val2", "val1")), scenarioParser.searchForPlaceholders());
        assertEquals(new HashSet<>(Arrays.asList("val2", "val1")), globalParser.searchForPlaceholders());
    }
}
