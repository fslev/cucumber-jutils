package com.cucumber.utils.engineering.match;

import com.cucumber.utils.exceptions.InvalidTypeException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class StringMatcherTests {

    @Test
    public void compareNulls() throws InvalidTypeException {
        StringMatcher matcher = new StringMatcher(null, null, null, null);
        Map<String, Object> props = matcher.match();
        assertTrue(props.isEmpty());
    }

    @Test
    public void compareWithNull() throws InvalidTypeException {
        StringMatcher matcher = new StringMatcher(null, "~[prop]", null, null);
        Map<String, Object> props = matcher.match();
        assertEquals(1, props.size());
        assertNull(props.get("prop1"));
    }

    @Test
    public void compareEmptyStrings() throws InvalidTypeException {
        StringMatcher matcher = new StringMatcher(null, "", "", null);
        Map<String, Object> props = matcher.match();
        assertEquals(0, props.size());
    }

    @Test
    public void compareSimpleString() throws InvalidTypeException {
        String expected = "result";
        String actual = "result";
        StringMatcher matcher = new StringMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareSimpleInts() throws InvalidTypeException {
        int expected = 1;
        int actual = 1;
        StringMatcher matcher = new StringMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareSimpleIntWithAssignSymbols() throws InvalidTypeException {
        String expected = "~[sym1]";
        int actual = 10;
        StringMatcher matcher = new StringMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertEquals(10, symbols.get("sym1"));
        assertEquals(1, symbols.size());
    }

    @Test
    public void compareStringWithManyConsecutiveAssignSymbols() throws InvalidTypeException {
        String expected = "~[prop1]~[prop2]~[prop3]~[prop4]";
        String actual = "Andorra";
        StringMatcher matcher = new StringMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertEquals("Andorra", symbols.get("prop1"));
        assertTrue(symbols.get("prop2").toString().isEmpty());
        assertTrue(symbols.get("prop3").toString().isEmpty());
        assertTrue(symbols.get("prop4").toString().isEmpty());
        assertEquals(4, symbols.size());
    }

    @Test
    public void compareStringWithManyAssignSymbolsBetweenNewLines() throws InvalidTypeException {
        String expected = "~[prop1],\n This is a ~[prop2]\n ~[prop3]!";
        String actual = "Hello,\n This is a world of many nations \n And 7 continents...!";
        StringMatcher matcher = new StringMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertEquals("Hello", symbols.get("prop1"));
        assertEquals("world of many nations ", symbols.get("prop2"));
        assertEquals("And 7 continents...", symbols.get("prop3"));
    }

    @Test(expected = AssertionError.class)
    public void compareSimpleInt_negative() throws InvalidTypeException {
        int expected = 1;
        int actual = 2;
        StringMatcher matcher = new StringMatcher(null, expected, actual, null);
        matcher.match();
    }

    @Test
    public void compareSimpleInteger() throws InvalidTypeException {
        Integer expected = 1;
        int actual = 1;
        StringMatcher matcher = new StringMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareSimpleBoolean() throws InvalidTypeException {
        boolean expected = true;
        Boolean actual = true;
        StringMatcher matcher = new StringMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void compareSimpleBoolean_negative() throws InvalidTypeException {
        boolean expected = true;
        Boolean actual = false;
        StringMatcher matcher = new StringMatcher(null, expected, actual, null);
        matcher.match();
    }

    @Test(expected = AssertionError.class)
    public void compareSimpleString_negative() throws InvalidTypeException {
        String expected = "val";
        String actual = "result";
        StringMatcher matcher = new StringMatcher(null, expected, actual, null);
        matcher.match();
    }

    @Test
    public void compareInvalidRegex() throws InvalidTypeException {
        String expected = "va(lue";
        String actual = "va(lue";
        StringMatcher matcher = new StringMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareRegex() throws InvalidTypeException {
        String expected = "va.*ue";
        String actual = "va(lue";
        StringMatcher matcher = new StringMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertTrue(symbols.isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void compareRegex_negative() throws InvalidTypeException {
        String expected = "va.*ue";
        String actual = "va(le";
        StringMatcher matcher = new StringMatcher(null, expected, actual, null);
        matcher.match();
    }

    @Test
    public void compareSimpleStringWithAssignSymbols() throws InvalidTypeException {
        String expected = "The ~[sym1] is running through the ~[sym2]";
        String actual = "The rabbit is running through the forest";
        StringMatcher matcher = new StringMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertEquals("rabbit", symbols.get("sym1"));
        assertEquals("forest", symbols.get("sym2"));
        assertEquals(2, symbols.size());
    }

    @Test
    public void compareRegexWithAssignSymbols() throws InvalidTypeException {
        String expected = ".* Rabbit ~[sym1] in the .*";
        String actual = "The Rabbit is running in the forest";
        StringMatcher matcher = new StringMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertEquals("is running", symbols.get("sym1"));
        assertEquals(1, symbols.size());
    }

    @Test(expected = AssertionError.class)
    public void compareRegexWithAssignSymbols_negative() throws InvalidTypeException {
        String expected = ".* Fox ~[sym1] in the .*";
        String actual = "The Rabbit is running in the forest";
        StringMatcher matcher = new StringMatcher(null, expected, actual, null);
        matcher.match();
    }

    @Test(expected = AssertionError.class)
    public void checkMessageFromFailedCompare() throws InvalidTypeException {
        String expected = "wa";
        int actual = 1;
        try {
            new StringMatcher("Some mess", expected, actual, null).match();
        } catch (AssertionError e) {
            assertTrue(e.getMessage().contains("Some mess") && e.getMessage().contains("Expected:"));
            throw e;
        }
    }

    @Test
    public void compareStringWithAssignSymbolsAgainstStringWithRegexCharacters() throws InvalidTypeException {
        String expected = "This is regex ~[regex]";
        String actual = "This is regex a|b|c|d";
        StringMatcher matcher = new StringMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertEquals("a|b|c|d", symbols.get("regex"));
        assertEquals(1, symbols.size());
    }

    @Test
    public void compareStringWithAssignSymbolsAndRegexAgainstStringWithRegexCharacters() throws InvalidTypeException {
        String expected = ".* is regex ~[regex]";
        String actual = "This is regex a|b|c|d";
        StringMatcher matcher = new StringMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertEquals("a|b|c|d", symbols.get("regex"));
        assertEquals(1, symbols.size());
    }

    @Test
    public void compareStringWithAssignSymbolsAndSeparateRegexAgainstStringWithRegexCharacters() throws InvalidTypeException {
        String expected = ".* is regex ~[regex] \\Q[0-9]*\\E";
        String actual = "This is regex a|b|c|d [0-9]*";
        StringMatcher matcher = new StringMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertEquals("a|b|c|d", symbols.get("regex"));
        assertEquals(1, symbols.size());
    }

    @Test
    public void compareStringWithAssignSymbolsAndSeparateRegexAgainstStringWithRegexCharacters_negative() {
        String expected = ".* is regex ~[regex] [0-9]*";
        String actual = "This is regex a|b|c|d [0-9]*";
        try {
            new StringMatcher(null, expected, actual, null).match();
        } catch (AssertionError | InvalidTypeException e) {
            assertEquals("\nEXPECTED:\n" +
                    ".* is regex ~[regex] [0-9]*\n" +
                    "BUT GOT:\n" +
                    "This is regex a|b|c|d [0-9]*\n", e.getMessage());
            return;
        }
        fail("Values should not match ! But they do...");
    }

    @Test
    public void compareStringWithAssignSymbolsAndRegexAgainstStringWithRegexCharacters_negative() {
        String expected = ".* is regex ~[regex]lorem";
        String actual = "This is regex a|b|c|d";
        try {
            new StringMatcher(null, expected, actual, null).match();
        } catch (AssertionError | InvalidTypeException e) {
            assertEquals("\n" +
                    "EXPECTED:\n" +
                    ".* is regex ~[regex]lorem\n" +
                    "BUT GOT:\n" +
                    "This is regex a|b|c|d\n", e.getMessage());
            return;
        }
        fail("Values should not match ! But they do...");
    }

    @Test
    public void compareStringWithAssignSymbolsAndInvalidRegexAgainstStringWithRegexCharacters() throws InvalidTypeException {
        String expected = "[ is regex ~[regex]";
        String actual = "[ is regex a|b|c|d";
        StringMatcher matcher = new StringMatcher(null, expected, actual, null);
        Map<String, Object> symbols = matcher.match();
        assertEquals("a|b|c|d", symbols.get("regex"));
        assertEquals(1, symbols.size());
    }

    @Test
    public void testPropertiesGeneratorFromObject() throws InvalidTypeException {
        String a = "~[var]";
        List<String> b = new ArrayList<>();
        Map<String, Object> props = new StringMatcher(null, a, b, null).match();
        assertEquals(b, props.get("var"));
        assertEquals(1, props.size());
    }

    @Test
    public void testPropertiesGeneratorFromNull() throws InvalidTypeException {
        String a = "~[var]";
        Map<String, Object> props = new StringMatcher(null, a, null, null).match();
        assertNull(props.get("var"));
        assertEquals(1, props.size());
    }

    @Test
    public void testPropertiesGeneratorFromSimpleText() throws InvalidTypeException {
        String a = "~[sym1]";
        String b = "Moon";
        Map<String, Object> props = new StringMatcher(null, a, b, null).match();
        assertEquals("Moon", props.get("sym1"));
        assertEquals(1, props.size());
    }

    @Test
    public void testPropertiesGeneratorFromTextWithSpecialCharacters() throws InvalidTypeException {
        String a = "~[sym1]";
        String b = "{\"test\":\"M^o|%o$n\"";
        Map<String, Object> props = new StringMatcher(null, a, b, null).match();
        assertEquals(b, props.get("sym1"));
        assertEquals(1, props.size());
    }

    @Test(expected = AssertionError.class)
    public void testPropertiesGeneratorFromSimpleText_negative() throws InvalidTypeException {
        String a = "foo ~[sym1] bar";
        String b = "foo some bra";
        Map<String, Object> props = new StringMatcher(null, a, b, null).match();
    }

    @Test
    public void testPropertiesGeneratorDuplicates() throws InvalidTypeException {
        String a = "~[sym1] ~[sym1] ~[sym2]";
        String b = "Moon Sun Tue";
        Map<String, Object> props = new StringMatcher(null, a, b, null).match();
        assertEquals("Sun", props.get("sym1"));
        assertEquals("Tue", props.get("sym2"));
        assertEquals(2, props.size());
    }

    @Test
    public void testPropertiesGeneratorWithTailPlaceholder() throws InvalidTypeException {
        String a = "Lorem Ipsum ~[prop1]";
        String b = "Lorem Ipsum text";
        Map<String, Object> props = new StringMatcher(null, a, b, null).match();
        assertEquals("text", props.get("prop1"));
        assertEquals(1, props.size());
    }

    @Test(expected = AssertionError.class)
    public void testPropertiesGeneratorWithNoMatch() throws InvalidTypeException {
        String a = "this is ~[prop1] property ~[prop2]";
        String b = "this is ~[prop1] property";
        Map<String, Object> props = new StringMatcher(null, a, b, null).match();
        assertEquals(0, props.size());
    }

    @Test
    public void testPropertiesGeneratorFromSimpleTextWithRegex() throws InvalidTypeException {
        String a = ".*M~[sym1]n.*";
        String b = "Moon";
        Map<String, Object> props = new StringMatcher(null, a, b, null).match();
        assertEquals("oo", props.get("sym1"));
        assertEquals(1, props.size());
    }

    @Test
    public void testEmptyPropertiesGeneratorFromSimpleText() throws InvalidTypeException {
        String a = "~[sym1]Moon";
        String b = "Moon";
        Map<String, Object> props = new StringMatcher(null, a, b, null).match();
        assertEquals("", props.get("sym1"));
        assertEquals(1, props.size());
    }

    @Test
    public void testEmptyPropertiesGeneratorFromSimpleTextWithRegex() throws InvalidTypeException {
        String a = ".*~[sym1]n.*";
        String b = "Moon";
        Map<String, Object> props = new StringMatcher(null, a, b, null).match();
        assertEquals("", props.get("sym1"));
        assertEquals(1, props.size());
    }

    @Test
    public void testPropertiesGeneratorFromInvalidRegex() throws InvalidTypeException {
        String a = ".* The ~[var1] is \\Qru.*n(ning\\E through the ~[var2]";
        String b = "something here The rab\nbit is ru.*n(ning through the forest";
        Map<String, Object> props = new StringMatcher(null, a, b, null).match();
        assertEquals("rab\nbit", props.get("var1"));
        assertEquals("forest", props.get("var2"));
    }

    @Test
    public void testPropertiesGeneratorFromRegex() throws InvalidTypeException {
        String a = ".* The ~[var1] is ru\\Q.*\\En.*g through ~[var2] .*";
        String b = "something here The rab\nbit is ru.*nning through the forest";
        Map<String, Object> props = new StringMatcher(null, a, b, null).match();
        assertEquals("rab\nbit", props.get("var1"));
        assertEquals("the", props.get("var2"));
    }

    @Test
    public void testPropertiesGeneratorFromSimpleJson() throws InvalidTypeException {
        String a = "{\"a\":[1,~[var1],3,4,5],\"b\":{\"k\":\"i\"}}";
        String b = "{\"a\":[1,2,3,4,5],\"b\":{\"k\":\"i\"}}";
        Map<String, Object> props = new StringMatcher(null, a, b, null).match();
        assertEquals("2", props.get("var1"));
    }

    @Test
    public void testPropertiesGeneratorFromJson() throws InvalidTypeException {
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
        Map<String, Object> props = new StringMatcher(null, a, b, null).match();
        assertEquals("Levine", props.get("var1"));
        assertEquals("favoriteFruit", props.get("var2"));
        assertEquals("consect(etur", props.get("var3"));
        assertEquals("na \nSo", props.get("var4"));
    }

}
