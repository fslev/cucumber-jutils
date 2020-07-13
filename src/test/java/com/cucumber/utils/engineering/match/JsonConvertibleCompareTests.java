package com.cucumber.utils.engineering.match;

import com.cucumber.utils.engineering.match.condition.MatchCondition;
import com.cucumber.utils.exceptions.InvalidTypeException;
import org.junit.Test;

import java.util.*;

public class JsonConvertibleCompareTests {

    @Test(expected = InvalidTypeException.class)
    public void compareObjectsWithNoJsonRepresentation() throws InvalidTypeException {
        String expected = "a";
        String actual = "ab";
        new JsonMatcher(null, expected, actual, null);
    }

    @Test
    public void compareLists() throws InvalidTypeException {
        List<String> expected = Arrays.asList("a", "b", "c", "c");
        List<String> actual = Arrays.asList("c", "a", "c", "b");
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        matcher.match();
    }

    @Test
    public void compareListsWithRegex() throws InvalidTypeException {
        List<String> expected = Arrays.asList("a", "b", "c", ".*");
        List<String> actual = Arrays.asList("c", "a", "c", "b");
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        matcher.match();
    }

    @Test
    public void compareLists_nonextensible() throws InvalidTypeException {
        List<String> expected = Arrays.asList("a", "b", "c", "c");
        List<String> actual = Arrays.asList("c", "a", "c", "b");
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_NON_EXTENSIBLE_ARRAY, MatchCondition.JSON_NON_EXTENSIBLE_OBJECT)));
        matcher.match();
    }

    @Test(expected = AssertionError.class)
    public void compareLists_nonextensible_negative() throws InvalidTypeException {
        List<String> expected = Arrays.asList("a", "b", "c", "c");
        List<String> actual = Arrays.asList("c", "a", "c", "b", "d");
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_NON_EXTENSIBLE_ARRAY)));
        matcher.match();
    }

    @Test
    public void compareLists_arrays_strict_order() throws InvalidTypeException {
        List<String> expected = Arrays.asList("c", "a", "c", "b");
        List<String> actual = Arrays.asList("c", "a", "c", "b", "d");
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_NON_EXTENSIBLE_OBJECT, MatchCondition.JSON_STRICT_ORDER_ARRAY)));
        matcher.match();
    }

    @Test
    public void doNotMatchLists_arrays_strict_order() throws InvalidTypeException {
        List<String> expected = Arrays.asList("c", "a", "b", "c");
        List<String> actual = Arrays.asList("c", "a", "c", "b", "d");
        new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_NON_EXTENSIBLE_OBJECT,
                MatchCondition.JSON_STRICT_ORDER_ARRAY, MatchCondition.DO_NOT_MATCH))).match();
    }

    @Test(expected = AssertionError.class)
    public void doNotMatchLists_arrays_strict_order_negative() throws InvalidTypeException {
        List<String> expected = Arrays.asList("c", "a", "c", "b");
        List<String> actual = Arrays.asList("c", "a", "c", "b", "d");
        new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_NON_EXTENSIBLE_OBJECT,
                MatchCondition.JSON_STRICT_ORDER_ARRAY, MatchCondition.DO_NOT_MATCH))).match();
    }

    @Test(expected = AssertionError.class)
    public void compareLists_arrays_strict_order_negative() throws InvalidTypeException {
        List<String> expected = Arrays.asList("c", "a", "b", "c");
        List<String> actual = Arrays.asList("c", "a", "c", "b", "d");
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_STRICT_ORDER_ARRAY, MatchCondition.JSON_NON_EXTENSIBLE_OBJECT)));
        matcher.match();
    }

    @Test
    public void compareStrings() throws InvalidTypeException {
        String expected = "{\"a\":\"some val1\"}";
        String actual = "{\"b\":120,\"a\":\"some val1\"}";
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        matcher.match();
    }

    @Test(expected = AssertionError.class)
    public void compareStrings_negative() throws InvalidTypeException {
        String expected = "{\"a\":\"some val1\"}";
        String actual = "{\"b\":120,\"a\":\"some val2\"}";
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        matcher.match();
    }

    @Test
    public void compareListsOfMaps() throws InvalidTypeException {
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
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        matcher.match();
    }

    @Test
    public void compareListsOfMaps_nonExtensible() throws InvalidTypeException {
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
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        matcher.match();
    }

    @Test(expected = AssertionError.class)
    public void compareListsOfMaps_nonExtensible_negative() throws InvalidTypeException {
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
        JsonMatcher matcher = new JsonMatcher(null, expected, actual, null);
        matcher.match();
    }
}
