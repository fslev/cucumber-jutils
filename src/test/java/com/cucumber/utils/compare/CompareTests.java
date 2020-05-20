package com.cucumber.utils.compare;

import com.cucumber.utils.engineering.compare.Compare;
import org.junit.Test;

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
        Map<String, Object> symbols = compare.compare();
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
    public void comparePrimitives() {
        new Compare("", Integer.valueOf(200), Integer.valueOf(200), false, true, false, false, false, false).compare();
    }

    @Test
    public void compareJsonWithAssignSymbols() {
        String expected = "{\"b\":\"(~[sym1]\"}";
        String actual = "{\"a\":\"val2\",\"b\":\"(val1\"}";
        Compare compare = new Compare(expected, actual);
        Map<String, Object> symbols = compare.compare();
        assertEquals("val1", symbols.get("sym1"));
        assertEquals(1, symbols.size());
    }

    @Test
    public void compareJsonWithAssignSymbolsOnFields() {
        String expected = "{\"~[sym1]\":\"2\"}";
        String actual = "{\"a\":\"2\"}";
        Compare compare = new Compare(expected, actual);
        Map<String, Object> symbols = compare.compare();
        assertEquals("a", symbols.get("sym1"));
        assertEquals(1, symbols.size());
    }

    @Test
    public void compareJsonWithAssignSymbolsOnFields_in_depth() {
        String expected = "{\"a\":{\"abc-~[sym1]\":{\"o\":\"0\"}}}";
        String actual = "{\"a\":{\"abc-X\":{\"o\":\"1\"},\"abc-Y\":{\"o\":\"0\"},\"abc-X\":{\"o\":\"2\"}}}";
        Compare compare = new Compare(expected, actual);
        Map<String, Object> symbols = compare.compare();
        assertEquals("Y", symbols.get("sym1"));
        assertEquals(1, symbols.size());
    }

    @Test(expected = AssertionError.class)
    public void compareJsonWithAssignSymbolsOnFields_in_depth_negative() {
        String expected = "{\"a\":{\"abc-~[sym1]\":{\"o\":\"does not exists\"}}}";
        String actual = "{\"a\":{\"abc-X\":{\"o\":\"1\"},\"abc-Y\":{\"o\":\"0\"},\"abc-X\":{\"o\":\"2\"}}}";
        new Compare(expected, actual).compare();
    }

    @Test
    public void compareJsonWithAssignSymbolsOnFieldsAndValues() {
        String expected = "{\"~[sym1]\":\"~[sym2]\"}";
        String actual = "{\"a\":\"3\",\"b\":\"100\"}";
        Compare compare = new Compare(expected, actual);
        Map<String, Object> symbols = compare.compare();
        assertEquals("a", symbols.get("sym1"));
        assertEquals("3", symbols.get("sym2"));
        assertEquals(2, symbols.size());
    }

    @Test
    public void compareJsonWithAssignSymbolsOnFieldsWhichHasValuesThatMatch() {
        String expected = "{\"~[sym1]\":\"100\"}";
        String actual = "{\"a\":\"3\",\"x\":\"o\",\"b\":\"100\",\"c\":\"90\"}";
        Compare compare = new Compare(expected, actual);
        Map<String, Object> symbols = compare.compare();
        assertEquals("b", symbols.get("sym1"));
        assertEquals(1, symbols.size());
    }

    @Test(expected = AssertionError.class)
    public void compareJsonWithAssignSymbolsOnFieldsWhichHasValuesThatMatch_negative() {
        String expected = "{\"~[sym1]\":\"101\"}";
        String actual = "{\"a\":\"3\",\"x\":\"o\",\"b\":\"100\",\"c\":\"90\"}";
        new Compare(expected, actual).compare();
    }

    @Test
    public void compareJsonWithMultipleAssignSymbolsOnFieldsAndValues() {
        String expected = "{\"~[sym1]\":\"~[val1]\",\"~[sym2]\":\"~[val2]\"}";
        String actual = "{\"a\":\"3\",\"b\":\"100\"}";
        Compare compare = new Compare(expected, actual);
        Map<String, Object> symbols = compare.compare();
        assertEquals("a", symbols.get("sym1"));
        assertEquals("3", symbols.get("val1"));
        assertEquals("b", symbols.get("sym2"));
        assertEquals("100", symbols.get("val2"));
        assertEquals(4, symbols.size());
    }

    @Test
    public void compareJsonArrayWithAssignSymbolsOnFieldsAndValues() {
        String expected = "[{\"~[sym1]\":\"~[sym2]\"},{\"x\":false}]";
        String actual = "[{\"c\":0},{\"x\":false}]";
        Compare compare = new Compare(expected, actual);
        Map<String, Object> symbols = compare.compare();
        assertEquals("c", symbols.get("sym1"));
        assertEquals("0", symbols.get("sym2"));
        assertEquals(2, symbols.size());
    }

    @Test
    public void compareJsonArrayWithAssignSymbolsOnFieldsAndValues1() {
        String expected = "[{\"~[sym1]\":false}]";
        String actual = "[{\"c\":0},{\"x\":false}]";
        Compare compare = new Compare(expected, actual);
        Map<String, Object> symbols = compare.compare();
        assertEquals("x", symbols.get("sym1"));
        assertEquals(1, symbols.size());
    }

    @Test
    public void compareXmlWithAssignSymbols() {
        String expected =
                "<struct><int a=\"~[sym1]\">some ~[sym3] here</int><boolean a=\"bo~[sym2]ue\">false</boolean></struct>";
        String actual = "<struct><boolean a=\"boolAttrValue\">false</boolean>"
                + "<int a=\"(attrValue1\">some text here</int><str a=\"some result\"><a>sub text</a></str></struct>";
        Compare compare = new Compare(expected, actual);
        Map<String, Object> symbols = compare.compare();
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
        Map<String, Object> symbols = compare.compare();
        assertEquals("val1", symbols.get("sym1"));
        assertEquals("val2", symbols.get("sym2"));
        assertEquals("val3", symbols.get("sym3"));
        assertEquals(3, symbols.size());
    }

    @Test
    public void compareJsonConvertibleWithAssignSymbols() {
        List<Map<String, Object>> expected = new ArrayList<>();
        List<Map<String, Object>> actual = new ArrayList<>();
        //Fill expected
        Map<String, Object> map1 = new HashMap<>();
        map1.put("firstName", "~[sym1]");
        map1.put("lastName", "Davids1");
        Map<String, Object> map2 = new HashMap<>();
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
        Map<String, Object> symbols = compare.compare();
        assertEquals("John1", symbols.get("sym1"));
        assertEquals("Davids", symbols.get("sym2"));
        assertEquals(2, symbols.size());
    }

    @Test
    public void compareJsonConvertible_extensible() {
        List<Map<String, Object>> expected = new ArrayList<>();
        List<Map<String, Object>> actual = new ArrayList<>();
        //Fill expected
        Map<String, Object> map1 = new HashMap<>();
        map1.put("firstName", "John1");
        map1.put("lastName", "Davids1");
        expected.add(map1);
        //Fill actual
        map1 = new HashMap<>();
        map1.put("firstName", "Eric");
        map1.put("lastName", "Davids");
        Map<String, Object> map2 = new HashMap<>();
        map2.put("firstName", "John1");
        map2.put("lastName", "Davids1");
        actual.add(map1);
        actual.add(map2);
        new Compare(expected, actual).compare();
    }
}
