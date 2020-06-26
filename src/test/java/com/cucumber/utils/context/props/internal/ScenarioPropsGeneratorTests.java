package com.cucumber.utils.context.props.internal;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ScenarioPropsGeneratorTests {


    @Test
    public void testPropertiesGeneratorFromObject() {
        String a = "~[var]";
        List<String> b = new ArrayList<>();
        Map<String, Object> props = new ScenarioPropsGenerator(a, b).match().getProperties();
        assertEquals(b, props.get("var"));
        assertEquals(1, props.size());
    }

    @Test
    public void testPropertiesGeneratorFromNull() {
        String a = "~[var]";
        ScenarioPropsGenerator generator = new ScenarioPropsGenerator(a, null).match();
        Map<String, Object> props = generator.getProperties();
        assertNull(props.get("var"));
        assertEquals(1, props.size());
        assertNull(generator.getSubstitutedSource());
    }

    @Test
    public void testPropertiesGeneratorFromSimpleText() {
        String a = "~[sym1]";
        String b = "Moon";
        Map<String, Object> props = new ScenarioPropsGenerator(a, b).match().getProperties();
        assertEquals("Moon", props.get("sym1"));
        assertEquals(1, props.size());
    }

    @Test
    public void testPropertiesGeneratorFromTextWithSpecialCharacters() {
        String a = "~[sym1]";
        String b = "{\"test\":\"M^o|%o$n\"";
        Map<String, Object> props = new ScenarioPropsGenerator(a, b).match().getProperties();
        assertEquals(b, props.get("sym1"));
        assertEquals(1, props.size());
    }

    @Test
    public void testPropertiesGeneratorFromSimpleText_negative() {
        String a = "foo ~[sym1] bar";
        String b = "foo some bra";
        Map<String, Object> props = new ScenarioPropsGenerator(a, b).match().getProperties();
        assertNull(props.get("sym1"));
        assertEquals(0, props.size());
    }

    @Test
    public void testPropertiesGeneratorDuplicates() {
        String a = "~[sym1] ~[sym1] ~[sym2]";
        String b = "Moon Sun Tue";
        Map<String, Object> props = new ScenarioPropsGenerator(a, b).match().getProperties();
        assertEquals("Sun", props.get("sym1"));
        assertEquals("Tue", props.get("sym2"));
        assertEquals(2, props.size());
    }

    @Test
    public void testPropertiesGeneratorWithTailPlaceholder() {
        String a = "Lorem Ipsum ~[prop1]";
        String b = "Lorem Ipsum text";
        Map<String, Object> props = new ScenarioPropsGenerator(a, b).match().getProperties();
        assertEquals("text", props.get("prop1"));
        assertEquals(1, props.size());
    }

    @Test
    public void testPropertiesGeneratorWithNoMatch() {
        String a = "this is ~[prop1] property ~[prop2]";
        String b = "this is ~[prop1] property";
        Map<String, Object> props = new ScenarioPropsGenerator(a, b).match().getProperties();
        assertEquals(0, props.size());
    }

    @Test
    public void testPropertiesGeneratorFromSimpleTextWithRegex() {
        String a = ".*M~[sym1]n.*";
        String b = "Moon";
        Map<String, Object> props = new ScenarioPropsGenerator(a, b).match().getProperties();
        assertEquals("oo", props.get("sym1"));
        assertEquals(1, props.size());
    }

    @Test
    public void testEmptyPropertiesGeneratorFromSimpleText() {
        String a = "~[sym1]Moon";
        String b = "Moon";
        Map<String, Object> props = new ScenarioPropsGenerator(a, b).match().getProperties();
        assertEquals("", props.get("sym1"));
        assertEquals(1, props.size());
    }

    @Test
    public void testEmptyPropertiesGeneratorFromSimpleTextWithRegex() {
        String a = ".*~[sym1]n.*";
        String b = "Moon";
        Map<String, Object> props = new ScenarioPropsGenerator(a, b).match().getProperties();
        assertEquals("", props.get("sym1"));
        assertEquals(1, props.size());
    }

    @Test
    public void testPropertiesGeneratorFromInvalidRegex() {
        String a = "The ~[var1] is ru.*n(ning through the ~[var2]";
        String b = "something here The rab\nbit is ru.*n(ning through the forest";
        ScenarioPropsGenerator generator = new ScenarioPropsGenerator(a, b).match();
        Map<String, Object> props = generator.getProperties();
        assertEquals("rab\nbit", props.get("var1"));
        assertEquals("forest", props.get("var2"));
        assertEquals("The rab\nbit is ru.*n(ning through the forest",
                generator.getSubstitutedSource());
    }

    @Test
    public void testPropertiesGeneratorFromRegex() {
        String a = ".* The ~[var1] is ru\\Q.*\\En.*g through ~[var2] .*";
        String b = "something here The rab\nbit is ru.*nning through the forest";
        ScenarioPropsGenerator generator = new ScenarioPropsGenerator(a, b).match();
        Map<String, Object> props = generator.getProperties();
        assertEquals("rab\nbit", props.get("var1"));
        assertEquals("the", props.get("var2"));
        assertEquals(generator.getSubstitutedSource().toString(),
                ".* The rab\nbit is ru\\Q.*\\En.*g through the .*", generator.getSubstitutedSource());
    }

    @Test
    public void testPropertiesGeneratorFromSimpleJson() {
        String a = "{\"a\":[1,~[var1],3,4,5],\"b\":{\"k\":\"i\"}}";
        String b = "{\"a\":[1,2,3,4,5],\"b\":{\"k\":\"i\"}}";
        ScenarioPropsGenerator generator = new ScenarioPropsGenerator(a, b).match();
        Map<String, Object> props = generator.getProperties();
        assertEquals("2", props.get("var1"));
        assertEquals(b, generator.getSubstitutedSource());
    }

    @Test
    public void testPropertiesGeneratorFromJson() {
        String a = "[\n" + "  {\n" + "    \"_id\": \"5b48fcdd5c3da373f5114ec7\",\n"
                + "    \"index\": 0,\n"
                + "    \"guid\": \"ed43e5b1-3da2-4ebe-baa5-e44d4eb18a5c\",\n"
                + "    \"isActive\": true,\n" + "    \"balance\": \"$3,812.47\",\n"
                + "    \"picture\": \"http://placehold.it/32x32\",\n" + "    \"age\": 25,\n"
                + "    \"eyeColor\": \"green\",\n" + "    \"name\": \"Tonya Schneider\",\n"
                + "    \"gender\": \"female\",\n" + "    \"company\": \"PHARMEX\",\n"
                + "    \"email\": \"tonyaschneider@pharmex.com\",\n"
                + "    \"phone\": \"+1 (904) 411-2158\",\n"
                + "    \"address\": \"195 Hubbard Place, Whitestone, New Hampshire, 1382\",\n"
                + "    \"about\": \"Occaecat laboris eu consequat fugiat. In dolore dolore esse voluptate. Amet ipsum id nisi nulla pariatur do dolore dolore aliquip qui laboris. Aute consequat tempor incididunt sunt voluptate laboris. Velit adipisicing nostrud laboris labore eiusmod. Dolore sint laborum culpa nulla eu sunt excepteur.\\r\\n\",\n"
                + "    \"registered\": \"2015-05-21T05:11:55 -03:00\",\n"
                + "    \"latitude\": 67.096081,\n" + "    \"longitude\": 36.71768,\n"
                + "    \"tags\": [\n" + "      \"aute\",\n" + "      \"dolore\",\n"
                + "      \"est\",\n" + "      \"officia\",\n" + "      \"enim\",\n"
                + "      \"aliqua\",\n" + "      \"~[var3]\"\n" + "    ],\n"
                + "    \"friends\": [\n" + "      {\n" + "        \"id\": 0,\n"
                + "        \"name\": \"~[var1] Hart\"\n" + "      },\n" + "      {\n"
                + "        \"id\": 1,\n" + "        \"name\": \"Velazquez Sargent\"\n"
                + "      },\n" + "      {\n" + "        \"id\": 2,\n"
                + "        \"name\": \"De~[var4]sa\"\n" + "      }\n" + "    ],\n"
                + "    \"greeting\": \"Hello, Tonya Schneider! You have 9 unread messages.\",\n"
                + "    \"~[var2]\": \"banana\"\n" + "  }\n" + "]";
        String b = "[\n" + "  {\n" + "    \"_id\": \"5b48fcdd5c3da373f5114ec7\",\n"
                + "    \"index\": 0,\n"
                + "    \"guid\": \"ed43e5b1-3da2-4ebe-baa5-e44d4eb18a5c\",\n"
                + "    \"isActive\": true,\n" + "    \"balance\": \"$3,812.47\",\n"
                + "    \"picture\": \"http://placehold.it/32x32\",\n" + "    \"age\": 25,\n"
                + "    \"eyeColor\": \"green\",\n" + "    \"name\": \"Tonya Schneider\",\n"
                + "    \"gender\": \"female\",\n" + "    \"company\": \"PHARMEX\",\n"
                + "    \"email\": \"tonyaschneider@pharmex.com\",\n"
                + "    \"phone\": \"+1 (904) 411-2158\",\n"
                + "    \"address\": \"195 Hubbard Place, Whitestone, New Hampshire, 1382\",\n"
                + "    \"about\": \"Occaecat laboris eu consequat fugiat. In dolore dolore esse voluptate. Amet ipsum id nisi nulla pariatur do dolore dolore aliquip qui laboris. Aute consequat tempor incididunt sunt voluptate laboris. Velit adipisicing nostrud laboris labore eiusmod. Dolore sint laborum culpa nulla eu sunt excepteur.\\r\\n\",\n"
                + "    \"registered\": \"2015-05-21T05:11:55 -03:00\",\n"
                + "    \"latitude\": 67.096081,\n" + "    \"longitude\": 36.71768,\n"
                + "    \"tags\": [\n" + "      \"aute\",\n" + "      \"dolore\",\n"
                + "      \"est\",\n" + "      \"officia\",\n" + "      \"enim\",\n"
                + "      \"aliqua\",\n" + "      \"consect(etur\"\n" + "    ],\n"
                + "    \"friends\": [\n" + "      {\n" + "        \"id\": 0,\n"
                + "        \"name\": \"Levine Hart\"\n" + "      },\n" + "      {\n"
                + "        \"id\": 1,\n" + "        \"name\": \"Velazquez Sargent\"\n"
                + "      },\n" + "      {\n" + "        \"id\": 2,\n"
                + "        \"name\": \"Dena \nSosa\"\n" + "      }\n" + "    ],\n"
                + "    \"greeting\": \"Hello, Tonya Schneider! You have 9 unread messages.\",\n"
                + "    \"favoriteFruit\": \"banana\"\n" + "  }\n" + "]";
        ScenarioPropsGenerator generator = new ScenarioPropsGenerator(a, b).match();
        Map<String, Object> props = generator.getProperties();
        assertEquals("Levine", props.get("var1"));
        assertEquals("favoriteFruit", props.get("var2"));
        assertEquals("consect(etur", props.get("var3"));
        assertEquals("na \nSo", props.get("var4"));
        assertEquals(b, generator.getSubstitutedSource());
    }
}
