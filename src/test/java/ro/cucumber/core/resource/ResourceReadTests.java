package ro.cucumber.core.resource;

import ro.cucumber.core.engineering.utils.ResourceUtils;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ResourceReadTests {

    @Test
    public void testStringReadingFromFile() {
        assertEquals("some content 1\nsome content 2\n", ResourceUtils.read("foobar/file1.txt"));
    }

    @Test
    public void testJsonReadingFromFile() {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.putObject("menu").put("show", true);
        assertEquals(json, ResourceUtils.readToJson("foobar/file1.json"));
    }
}
