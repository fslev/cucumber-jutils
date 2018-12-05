package ro.cucumber.core.compare;

import org.junit.Test;
import ro.cucumber.core.engineering.compare.Compare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CompareTests {

    @Test
    public void compareNulls() {
        String expected = null;
        String actual = null;
        Compare compare = new Compare(expected, actual);
        Map<String, String> symbols = compare.compare();
        assertTrue(symbols.isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void compareNulls_negative() {
        String expected = "text";
        String actual = null;
        new Compare(expected, actual).compare();
    }

    @Test(expected = AssertionError.class)
    public void compareNulls_negative1() {
        String expected = null;
        String actual = "text";
        new Compare(expected, actual).compare();
    }

    @Test
    public void compareJsonWithAssignSymbols() {
        String expected = "{\"b\":\"(~[sym1]\"}";
        String actual = "{\"a\":\"val2\",\"b\":\"(val1\"}";
        Compare compare = new Compare(expected, actual);
        Map<String, String> symbols = compare.compare();
        assertEquals("val1", symbols.get("sym1"));
        assertEquals(1, symbols.size());
    }

    @Test
    public void compareXmlWithAssignSymbols() {
        String expected =
                "<struct><int a=\"~[sym1]\">some ~[sym3] here</int><boolean a=\"bo~[sym2]ue\">false</boolean></struct>";
        String actual = "<struct><boolean a=\"boolAttrValue\">false</boolean>"
                + "<int a=\"(attrValue1\">some text here</int><str a=\"some value\"><a>sub text</a></str></struct>";
        Compare compare = new Compare(expected, actual);
        Map<String, String> symbols = compare.compare();
        assertEquals("(attrValue1", symbols.get("sym1"));
        assertEquals("olAttrVal", symbols.get("sym2"));
        assertEquals("text", symbols.get("sym3"));
        assertEquals(3, symbols.size());
    }

    @Test
    public void compareString() {
        String expected =
                "<struct<int a=\"~[sym1]\">some ~[sym3] here</int><boolean a=\"bo~[sym2]ue\">false</boolean></struct>";
        String actual =
                "<struct<int a=\"val1\">some val3 here</int><boolean a=\"boval2ue\">false</boolean></struct>";
        Compare compare = new Compare(expected, actual);
        Map<String, String> symbols = compare.compare();
        assertEquals("val1", symbols.get("sym1"));
        assertEquals("val2", symbols.get("sym2"));
        assertEquals("val3", symbols.get("sym3"));
        assertEquals(3, symbols.size());
    }

    @Test
    public void compareJsonConvertibleWithAssignSymbols() {
        List<Map<String, String>> expected = new ArrayList<>();
        List<Map<String, String>> actual = new ArrayList<>();
        //Fill expected
        Map<String, String> map1 = new HashMap<>();
        map1.put("firstName", "~[sym1]");
        map1.put("lastName", "Davids1");
        Map<String, String> map2 = new HashMap<>();
        map2.put("firstName", "Eric");
        map2.put("lastName", "~[sym2]");
        expected.add(map1);
        expected.add(map2);
        //Fill actual
        map1 = new HashMap<>();
        map1.put("firstName", "Eric");
        map1.put("lastName", "Davids");
        map2 = new HashMap<>();
        map2.put("firstName", "John1");
        map2.put("lastName", "Davids1");
        actual.add(map1);
        actual.add(map2);
        Compare compare = new Compare(expected, actual);
        Map<String, String> symbols = compare.compare();
        assertEquals("John1", symbols.get("sym1"));
        assertEquals("Davids", symbols.get("sym2"));
        assertEquals(2, symbols.size());
    }

    @Test
    public void compareJsonConvertible_extensible() {
        List<Map<String, String>> expected = new ArrayList<>();
        List<Map<String, String>> actual = new ArrayList<>();
        //Fill expected
        Map<String, String> map1 = new HashMap<>();
        map1.put("firstName", "John1");
        map1.put("lastName", "Davids1");
        expected.add(map1);
        //Fill actual
        map1 = new HashMap<>();
        map1.put("firstName", "Eric");
        map1.put("lastName", "Davids");
        Map<String, String> map2 = new HashMap<>();
        map2.put("firstName", "John1");
        map2.put("lastName", "Davids1");
        actual.add(map1);
        actual.add(map2);
        new Compare(expected, actual).compare();
    }
}
