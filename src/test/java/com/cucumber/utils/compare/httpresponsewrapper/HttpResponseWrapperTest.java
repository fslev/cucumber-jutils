package com.cucumber.utils.compare.httpresponsewrapper;

import com.cucumber.utils.context.compare.wrappers.HttpResponseWrapper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.BasicHttpContext;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseWrapperTest {

    @Test
    public void testWrapperInitFromString() throws IOException {
        String content = "{\"status\":200,\"reason\":\"some thing\",\"body\":{\"wa\":[1,2,3,4]}}";
        HttpResponseWrapper wrapper = new HttpResponseWrapper(content);
        Assert.assertEquals("200", wrapper.getStatus());
        Assert.assertEquals("some thing", wrapper.getReasonPhrase());
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("wa", Arrays.asList(new Integer[]{1, 2, 3, 4}));
        Assert.assertEquals(expectedMap, wrapper.getEntity());
        Assert.assertNull(wrapper.getHeaders());
    }

    @Test
    public void testWrapperInitFromStringStatus() throws IOException {
        String content = "{\"status\":\"200\"}";
        HttpResponseWrapper wrapper = new HttpResponseWrapper(content);
        Assert.assertEquals("200", wrapper.getStatus());
        Assert.assertNull(wrapper.getHeaders());
    }

    @Test(expected = IOException.class)
    public void testWrapperInitFromEmptyString() throws IOException {
        String content = "";
        new HttpResponseWrapper(content);
    }

    @Test
    public void testWrapperInitFromEmptyJsonString() throws IOException {
        String content = "{}";
        HttpResponseWrapper wrapper = new HttpResponseWrapper(content);
        Assert.assertNull(wrapper.getStatus());
        Assert.assertNull(wrapper.getReasonPhrase());
        Assert.assertNull(wrapper.getEntity());
        Assert.assertNull(wrapper.getHeaders());
    }

    @Test(expected = IOException.class)
    public void testWrapperInitFromOtherJsonString() throws IOException {
        String content = "{\"reasonPhrase\":\"test\"}";
        new HttpResponseWrapper(content);
    }

    @Test
    public void testWrapperInitFromMap() throws IOException {
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("status", 200);
        expectedMap.put("reason", "some reason");
        expectedMap.put("body", new int[]{2, 3, 4});
        HttpResponseWrapper wrapper = new HttpResponseWrapper(expectedMap);
        Assert.assertEquals("200", wrapper.getStatus());
        Assert.assertEquals("some reason", wrapper.getReasonPhrase());
        Assert.assertEquals(Arrays.asList(new Integer[]{2, 3, 4}), wrapper.getEntity());
        Assert.assertNull(wrapper.getHeaders());
    }

    @Test
    public void testWrapperInitFromHttpResponse() throws IOException {
        HttpResponse mock = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "some reason"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        mock.setEntity(new StringEntity("{\"a\":100}"));
        mock.setHeader(new BasicHeader("Content-Type", "application/json"));
        mock.setHeader(new BasicHeader("Accept", "application/json"));
        HttpResponseWrapper wrapper = new HttpResponseWrapper(mock);
        Assert.assertEquals("200", wrapper.getStatus());
        Assert.assertEquals("some reason", wrapper.getReasonPhrase());
        Assert.assertEquals("{\"a\":100}", wrapper.getEntity());
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("Content-Type", "application/json");
        expectedMap.put("Accept", "application/json");
        Assert.assertEquals(expectedMap, wrapper.getHeaders());
    }
}
