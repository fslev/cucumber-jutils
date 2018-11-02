package ro.cucumber.core.resource;

import ro.cucumber.core.engineering.utils.JsonUtils;
import ro.cucumber.core.engineering.utils.ResourceUtils;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ResourceReadTests {

    @Test
    public void testStringReadingFromFile() {
        assertEquals("some content 1\nsome content 2\n", ResourceUtils.read("foobar/file1.txt"));
    }

    @Test
    public void testJsonReadingFromFile() throws IOException {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.putObject("menu").put("show", true);
        assertEquals(json, JsonUtils.toJson(ResourceUtils.read("foobar/file1.json")));
    }

    @Test
    public void testRecursiveReaderFromDirectory() {
        String dirpath = "foobar/dir";
        List<Map<String, String>> actualData = ResourceUtils.readDirectory(dirpath);
        assertEquals(5, actualData.size());
        assertTrue(actualData.contains(Map.of("foobar1.json", "1")));
        assertTrue(actualData.contains(Map.of("foo/foo1.json", "2")));
        assertTrue(actualData.contains(Map.of("foo/foo2.json", "3")));
        assertTrue(actualData.contains(Map.of("foo/bar/bar1.json", "4")));
        assertTrue(actualData.contains(Map.of("foo/bar/bar2.json", "5")));

    }
}
