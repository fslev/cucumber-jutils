package ro.cucumber.core.compare;

import org.junit.Test;
import ro.cucumber.core.engineering.compare.JsonCompare;
import ro.cucumber.core.engineering.compare.exceptions.CompareException;

import java.util.*;

public class JsonConvertibleCompareTests {

    @Test(expected = CompareException.class)
    public void compareObjectsWithNoJsonRepresentation() throws CompareException {
        String expected = "a";
        String actual = "ab";
        new JsonCompare(expected, actual);
    }

    @Test
    public void compareLists() throws CompareException {
        List<String> expected = Arrays.asList(new String[]{"a", "b", "c", "c"});
        List<String> actual = Arrays.asList(new String[]{"c", "a", "c", "b"});
        JsonCompare compare = new JsonCompare(expected, actual);
        compare.compare();
    }

    @Test
    public void compareLists_nonextensible() throws CompareException {
        List<String> expected = Arrays.asList(new String[]{"a", "b", "c", "c"});
        List<String> actual = Arrays.asList(new String[]{"c", "a", "c", "b"});
        JsonCompare compare = new JsonCompare(expected, actual, true, true, false);
        compare.compare();
    }

    @Test(expected = AssertionError.class)
    public void compareLists_nonextensible_negative() throws CompareException {
        List<String> expected = Arrays.asList(new String[]{"a", "b", "c", "c"});
        List<String> actual = Arrays.asList(new String[]{"c", "a", "c", "b", "d"});
        JsonCompare compare = new JsonCompare(expected, actual, false, true);
        compare.compare();
    }

    @Test
    public void compareLists_arrays_strict_order() throws CompareException {
        List<String> expected = Arrays.asList(new String[]{"c", "a", "c", "b"});
        List<String> actual = Arrays.asList(new String[]{"c", "a", "c", "b", "d"});
        JsonCompare compare = new JsonCompare(expected, actual, true, false, true);
        compare.compare();
    }

    @Test(expected = AssertionError.class)
    public void compareLists_arrays_strict_order_negative() throws CompareException {
        List<String> expected = Arrays.asList(new String[]{"c", "a", "b", "c"});
        List<String> actual = Arrays.asList(new String[]{"c", "a", "c", "b", "d"});
        JsonCompare compare = new JsonCompare(expected, actual, true, false, true);
        compare.compare();
    }

    @Test
    public void compareStrings() throws CompareException {
        String expected = "{\"a\":\"some val1\"}";
        String actual = "{\"b\":120,\"a\":\"some val1\"}";
        JsonCompare compare = new JsonCompare(expected, actual);
        compare.compare();
    }

    @Test(expected = AssertionError.class)
    public void compareStrings_negative() throws CompareException {
        String expected = "{\"a\":\"some val1\"}";
        String actual = "{\"b\":120,\"a\":\"some val2\"}";
        JsonCompare compare = new JsonCompare(expected, actual);
        compare.compare();
    }

    @Test
    public void compareListsOfMaps() throws CompareException {
        List<Map<String, Object>> expected = new ArrayList<>();
        List<Map<String, Object>> actual = new ArrayList<>();
        //Fill expected
        Map<String, Object> map1 = new LinkedHashMap<>();
        map1.put("firstName", "John");
        map1.put("lastName", null);
        Map<String, Object> map2 = new LinkedHashMap<>();
        map2.put("firstName", "John1");
        map2.put("lastName", ".+1");
        expected.add(map1);
        expected.add(map2);
        //Fill actual
        map1 = new LinkedHashMap<>();
        map1.put("firstName", "John");
        map1.put("lastName", null);
        map2 = new LinkedHashMap<>();
        map2.put("firstName", "John1");
        map2.put("lastName", "Davids1");
        map2.put("address", "blah blah");
        Map<String, Object> map3 = new LinkedHashMap<>();
        map3.put("1", 2);
        map3.put("3", 4);
        actual.add(map1);
        actual.add(map2);
        actual.add(map3);
        JsonCompare compare = new JsonCompare(expected, actual);
        compare.compare();
    }

    @Test
    public void compareListsOfMaps_nonExtensible() throws CompareException {
        List<Map<String, Object>> expected = new ArrayList<>();
        List<Map<String, Object>> actual = new ArrayList<>();
        //Fill expected
        Map<String, Object> map1 = new LinkedHashMap<>();
        map1.put("firstName", "John");
        map1.put("lastName", null);
        Map<String, Object> map2 = new LinkedHashMap<>();
        map2.put("firstName", "John1");
        map2.put("lastName", ".+1");
        map2.put("!.*", ".*");
        expected.add(map1);
        expected.add(map2);
        //Fill actual
        map1 = new LinkedHashMap<>();
        map1.put("firstName", "John");
        map1.put("lastName", null);
        map2 = new LinkedHashMap<>();
        map2.put("firstName", "John1");
        map2.put("lastName", "Davids1");
        actual.add(map1);
        actual.add(map2);
        JsonCompare compare = new JsonCompare(expected, actual);
        compare.compare();
    }

    @Test(expected = AssertionError.class)
    public void compareListsOfMaps_nonExtensible_negative() throws CompareException {
        List<Map<String, Object>> expected = new ArrayList<>();
        List<Map<String, Object>> actual = new ArrayList<>();
        //Fill expected
        Map<String, Object> map1 = new LinkedHashMap<>();
        map1.put("firstName", "John");
        map1.put("lastName", null);
        Map<String, Object> map2 = new LinkedHashMap<>();
        map2.put("firstName", "John1");
        map2.put("lastName", ".+1");
        map2.put("!.*", ".*");
        expected.add(map1);
        expected.add(map2);
        //Fill actual
        map1 = new LinkedHashMap<>();
        map1.put("firstName", "John");
        map1.put("lastName", null);
        map2 = new LinkedHashMap<>();
        map2.put("firstName", "John1");
        map2.put("lastName", "Davids1");
        map2.put("address", "street address");
        actual.add(map1);
        actual.add(map2);
        JsonCompare compare = new JsonCompare(expected, actual);
        compare.compare();
    }
}
