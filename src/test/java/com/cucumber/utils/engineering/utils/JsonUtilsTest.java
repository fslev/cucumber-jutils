package com.cucumber.utils.engineering.utils;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.*;

public class JsonUtilsTest {

    private Function<String, List<String>> extractSpecialRegexCharsFct = s -> {
        List<String> regexChars = RegexUtils.getRegexCharsFromString(s);
        return regexChars.isEmpty() ? null : regexChars;
    };

    @Test
    public void testBigJsonBeautification() throws IOException {
        String json = ResourceUtils.read("props/bigJsons/actualLargeJson.json");
        assertNotNull(JsonUtils.prettyPrint(json));
    }

    @Test
    public void testSimpleJsonSpecialRegexCharacters() throws ParserConfigurationException, SAXException, IOException {
        String json = ResourceUtils.read("json/regex_chars/json1.json");
        Map<String, List<String>> results = JsonUtils.walkJsonAndProcessNodes(json, extractSpecialRegexCharsFct);
        assertEquals(Arrays.asList("."), results.get("foo.bar/{key}"));
        assertTrue(Arrays.asList("?", "+").containsAll(results.get("foo.bar/a^b/{value}")));
        assertTrue(Arrays.asList("^").containsAll(results.get("foo.bar/a^b/{key}")));
        assertTrue(Arrays.asList(".").containsAll(results.get("foo.bar/array[1]/a/{value}")));
        assertTrue(Arrays.asList(".").containsAll(results.get("foo.bar/array[1]/.a/{key}")));
        assertTrue(Arrays.asList("+").containsAll(results.get("foo.bar/array[1]/b/array[0]/{value}")));
        assertTrue(Arrays.asList("+").containsAll(results.get("foo.bar/array[1]/b/array[1]/ips+um/{key}")));
        assertTrue(Arrays.asList("[").containsAll(results.get("foo.bar/array[1]/b/array[1]/ips+um/{value}")));
        assertTrue(Arrays.asList(".", "$").containsAll(results.get("foo.bar/array[1]/b/array[5]/{value}")));
        assertEquals(9, results.size());
    }
}
