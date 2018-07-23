package ro.cucumber.core.compare;

import org.junit.Test;
import ro.cucumber.core.engineering.compare.JsonConvertibleObjectCompare;
import ro.cucumber.core.engineering.compare.exceptions.CompareException;

import java.util.*;

public class JsonConvertibleCompareTests {

    @Test(expected = CompareException.class)
    public void compareObjectsWithNoJsonRepresentation() throws CompareException {
        String expected = "a";
        String actual = "ab";
        new JsonConvertibleObjectCompare(expected, actual);
    }

    @Test
    public void compareLists() throws CompareException {
        List<String> expected = Arrays.asList(new String[]{"a", "b", "c", "c"});
        List<String> actual = Arrays.asList(new String[]{"c", "a", "c", "b"});
        JsonConvertibleObjectCompare compare = new JsonConvertibleObjectCompare(expected, actual);
        compare.compare();
    }

    @Test(expected = AssertionError.class)
    public void compareLists_nonExtensible() throws CompareException {
        List<String> expected = Arrays.asList(new String[]{"a", "b", "c", "d"});
        List<String> actual = Arrays.asList(new String[]{"d", "a", "c", "b", "e"});
        JsonConvertibleObjectCompare compare = new JsonConvertibleObjectCompare(expected, actual);
        compare.compare();
    }

    @Test
    public void compareListsOfMaps() throws CompareException {
        List<Map<String, String>> expected = new ArrayList<>();
        List<Map<String, String>> actual = new ArrayList<>();
        //Fill expected
        Map<String, String> map1 = new HashMap<>();
        map1.put("firstName", "John");
        map1.put("lastName", "Davids");
        Map<String, String> map2 = new HashMap<>();
        map2.put("firstName", "John1");
        map2.put("lastName", ".+1");
        expected.add(map1);
        expected.add(map2);
        //Fill actual
        map1 = new HashMap<>();
        map1.put("firstName", "John");
        map1.put("lastName", "Davids");
        map2 = new HashMap<>();
        map2.put("firstName", "John1");
        map2.put("lastName", "Davids1");
        actual.add(map1);
        actual.add(map2);
        JsonConvertibleObjectCompare compare = new JsonConvertibleObjectCompare(expected, actual);
        compare.compare();
    }

    @Test(expected = AssertionError.class)
    public void compareListsOfMaps_nonExtensible() throws CompareException {
        List<Map<String, String>> expected = new ArrayList<>();
        List<Map<String, String>> actual = new ArrayList<>();
        //Fill expected
        Map<String, String> map1 = new HashMap<>();
        map1.put("firstName", "John");
        Map<String, String> map2 = new HashMap<>();
        map2.put("firstName", "John1");
        map2.put("lastName", ".+1");
        expected.add(map1);
        expected.add(map2);
        //Fill actual
        map1 = new HashMap<>();
        map1.put("firstName", "John");
        map1.put("lastName", "Davids");
        map2 = new HashMap<>();
        map2.put("firstName", "John1");
        map2.put("lastName", "Davids1");
        actual.add(map1);
        actual.add(map2);
        JsonConvertibleObjectCompare compare = new JsonConvertibleObjectCompare(expected, actual);
        compare.compare();
    }
}
