package ro.cucumber.core.resource;

import ro.cucumber.core.engineering.utils.JsonUtils;
import ro.cucumber.core.engineering.utils.ResourceUtils;
import java.io.IOException;
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
    public void testInDepthReadingFromDirectory() {
        Map<String, String> actualData = ResourceUtils.readDirectory("foobar/dir");
        assertEquals(5, actualData.size());
        assertTrue(actualData.get("foobar1.json").equals("1"));
        assertTrue(actualData.get("foo/foo1.json").equals("2"));
        assertTrue(actualData.get("foo/foo2.json").equals("3"));
        assertTrue(actualData.get("foo/bar/bar1.json").equals("4"));
        assertTrue(actualData.get("foo/bar/bar2.json").equals("5"));
    }
}
