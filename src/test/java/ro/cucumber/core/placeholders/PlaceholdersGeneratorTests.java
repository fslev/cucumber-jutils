package ro.cucumber.core.placeholders;

import org.junit.Test;
import ro.cucumber.core.engineering.placeholders.PlaceholdersGenerator;

import static org.junit.Assert.assertEquals;

public class PlaceholdersGeneratorTests {

    @Test
    public void testPlaceholderGeneratorFromSimpleText() {
        String a = "~[sym1]";
        String b = "Moon";
        PlaceholdersGenerator parser = new PlaceholdersGenerator(a, b);
        assertEquals("Moon", parser.getPlaceholdersMap().get("sym1"));
        assertEquals(1, parser.getPlaceholdersMap().size());
    }

    @Test
    public void testPlaceholderGeneratorFromSimpleText_negative() {
        String a = "foo ~[sym1] bar";
        String b = "foo some bra";
        PlaceholdersGenerator parser = new PlaceholdersGenerator(a, b);
        assertEquals(null, parser.getPlaceholdersMap().get("sym1"));
        assertEquals(0, parser.getPlaceholdersMap().size());
    }

    @Test
    public void testPlaceholderGeneratorDuplicated() {
        String a = "~[sym1] ~[sym1]";
        String b = "Moon Sun";
        PlaceholdersGenerator parser = new PlaceholdersGenerator(a, b);
        assertEquals("Sun", parser.getPlaceholdersMap().get("sym1"));
        assertEquals(1, parser.getPlaceholdersMap().size());
    }

    @Test
    public void testPlaceholderGeneratorFromSimpleTextWithRegex() {
        String a = ".*M~[sym1]n.*";
        String b = "Moon";
        PlaceholdersGenerator parser = new PlaceholdersGenerator(a, b);
        assertEquals("oo", parser.getPlaceholdersMap().get("sym1"));
        assertEquals(1, parser.getPlaceholdersMap().size());
    }

    @Test
    public void testEmptyPlaceholderGeneratorFromSimpleText() {
        String a = "~[sym1]Moon";
        String b = "Moon";
        PlaceholdersGenerator parser = new PlaceholdersGenerator(a, b);
        assertEquals("", parser.getPlaceholdersMap().get("sym1"));
        assertEquals(1, parser.getPlaceholdersMap().size());
    }

    @Test
    public void testEmptyPlaceholderGeneratorFromSimpleTextWithRegex() {
        String a = ".*~[sym1]n.*";
        String b = "Moon";
        PlaceholdersGenerator parser = new PlaceholdersGenerator(a, b);
        assertEquals("", parser.getPlaceholdersMap().get("sym1"));
        assertEquals(1, parser.getPlaceholdersMap().size());
    }

    @Test
    public void testPlaceholderGeneratorFromInvalidRegex() {
        String a = "The ~[var1] is ru.*n(ning through the ~[var2]";
        String b = "something here The rab\nbit is ru.*n(ning through the forest";
        PlaceholdersGenerator parser = new PlaceholdersGenerator(a, b);
        assertEquals("rab\nbit", parser.getPlaceholdersMap().get("var1"));
        assertEquals("forest", parser.getPlaceholdersMap().get("var2"));
        assertEquals("The rab\nbit is ru.*n(ning through the forest",
                parser.parse());
    }

    @Test
    public void testPlaceholderGeneratorFromRegex() {
        String a = ".* The ~[var1] is ru\\Q.*\\En.*g through ~[var2] .*";
        String b = "something here The rab\nbit is ru.*nning through the forest";
        PlaceholdersGenerator parser = new PlaceholdersGenerator(a, b);
        assertEquals("rab\nbit", parser.getPlaceholdersMap().get("var1"));
        assertEquals("the", parser.getPlaceholdersMap().get("var2"));
        assertEquals(parser.parse(),
                ".* The rab\nbit is ru\\Q.*\\En.*g through the .*",
                parser.parse());
    }

    @Test
    public void testPlaceholderGeneratorFromSimpleJson() {
        String a = "{\"a\":[1,~[var1],3,4,5],\"b\":{\"k\":\"i\"}}";
        String b = "{\"a\":[1,2,3,4,5],\"b\":{\"k\":\"i\"}}";
        PlaceholdersGenerator parser = new PlaceholdersGenerator(a, b);
        assertEquals("2", parser.getPlaceholdersMap().get("var1"));
        assertEquals(b, parser.parse());
    }

    @Test
    public void testPlaceholderGeneratorFromJson() {
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
        PlaceholdersGenerator parser = new PlaceholdersGenerator(a, b);
        assertEquals("Levine", parser.getPlaceholdersMap().get("var1"));
        assertEquals("favoriteFruit", parser.getPlaceholdersMap().get("var2"));
        assertEquals("consect(etur", parser.getPlaceholdersMap().get("var3"));
        assertEquals("na \nSo", parser.getPlaceholdersMap().get("var4"));
        assertEquals(b, parser.parse());
    }
}
