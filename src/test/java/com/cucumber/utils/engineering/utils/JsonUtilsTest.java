package com.cucumber.utils.engineering.utils;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class JsonUtilsTest {

    @Test
    public void testBigJsonBeautification() throws IOException {
        String json = ResourceUtils.read("props/bigJsons/actualLargeJson.json");
        assertNotNull(JsonUtils.prettyPrint(json));
    }
}
