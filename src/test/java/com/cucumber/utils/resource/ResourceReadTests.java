package com.cucumber.utils.resource;

import com.cucumber.utils.engineering.utils.JsonUtils;
import com.cucumber.utils.engineering.utils.ResourceUtils;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ResourceReadTests {

    @Test
    public void testStringReadFromFile() throws IOException {
        Assert.assertEquals("some content 1\nsome content 2\n", ResourceUtils.read("foobar/file1.txt"));
    }

    @Test
    public void testJsonReadingFromFile() throws IOException {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.putObject("menu").put("show", true);
        Assert.assertEquals(json, JsonUtils.toJson(ResourceUtils.read("foobar/file1.json")));
    }

    @Test
    public void testPropertiesReadFromFile() {
        assertEquals("some values with white spaces and new lines \n ",
                ResourceUtils.readProps("foobar/foo.properties").get("IAmAProperty"));
    }

    @Test
    public void testPropertiesReadFromFileWithoutExtension() {
        assertEquals("some values with white spaces and new lines \n ",
                ResourceUtils.readProps("foobar/dir/foo/foo").get("IAmAProperty"));
    }

    @Test
    public void testInDepthReadFromDirectory() throws IOException, URISyntaxException {
        Map<String, String> actualData = ResourceUtils.readDirectory("foobar/dir");
        assertEquals(7, actualData.size());
        assertEquals("1", actualData.get("foobar/dir/foobar1.json"));
        assertEquals("2", actualData.get("foobar/dir/foo/foo1.json"));
        assertEquals("3", actualData.get("foobar/dir/foo/foo2.json"));
        assertEquals("4", actualData.get("foobar/dir/foo/bar/bar1.json"));
        assertEquals("5", actualData.get("foobar/dir/foo/bar/bar2.json"));
        assertEquals("test", actualData.get("foobar/dir/foo/bar/bar"));
    }

    @Test
    public void testInDepthReadFromDirectoryFilesWithoutExtension() throws IOException, URISyntaxException {
        Map<String, String> actualData = ResourceUtils.readDirectory("foobar/dir1", ".properties");
        assertEquals(1, actualData.size());
        assertEquals("pass", actualData.get("foobar/dir1/test2.properties"));
    }

    @Test(expected = IOException.class)
    public void testInDepthReadFromNonExistentDirectory() throws IOException, URISyntaxException {
        ResourceUtils.readDirectory("non_existent");
    }

    @Test(expected = IOException.class)
    public void testInDepthReadFromDirectoryWhichIsAFile() throws IOException, URISyntaxException {
        ResourceUtils.readDirectory("foobar/file1.txt");
    }

    @Test
    public void testInDepthReadFromClasspathDir() throws IOException, URISyntaxException {
        Map<String, String> actualData = ResourceUtils.readDirectory("");
        assertTrue(actualData.size() > 0);
        assertEquals("1", actualData.get("foobar/dir/foobar1.json"));
        assertEquals("2", actualData.get("foobar/dir/foo/foo1.json"));
        assertEquals("3", actualData.get("foobar/dir/foo/foo2.json"));
        assertEquals("4", actualData.get("foobar/dir/foo/bar/bar1.json"));
        assertEquals("5", actualData.get("foobar/dir/foo/bar/bar2.json"));
    }
}
