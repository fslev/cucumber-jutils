package ro.cucumber.core.compare.adapters;

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
import ro.cucumber.core.context.compare.adapters.HttpResponseAdapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class HttpResponseAdapterTest {

    @Test
    public void testAdapterInitFromString() throws IOException {
        String content = "{\"status\":200,\"reason\":\"some thing\",\"body\":{\"wa\":[1,2,3,4]}}";
        HttpResponseAdapter adapter = new HttpResponseAdapter(content);
        Assert.assertEquals(200, (int) adapter.getStatus());
        Assert.assertEquals("some thing", adapter.getReasonPhrase());
        Assert.assertEquals(Map.of("wa", Arrays.asList(new Integer[]{1, 2, 3, 4})), adapter.getEntity());
        Assert.assertNull(adapter.getHeaders());
    }

    @Test(expected = IOException.class)
    public void testAdapterInitFromEmptyString() throws IOException {
        String content = "";
        new HttpResponseAdapter(content);
    }

    @Test
    public void testAdapterInitFromEmptyJsonString() throws IOException {
        String content = "{}";
        HttpResponseAdapter adapter = new HttpResponseAdapter(content);
        Assert.assertNull(adapter.getStatus());
        Assert.assertNull(adapter.getReasonPhrase());
        Assert.assertNull(adapter.getEntity());
        Assert.assertNull(adapter.getHeaders());
    }

    @Test(expected = IOException.class)
    public void testAdapterInitFromOtherJsonString() throws IOException {
        String content = "{\"reasonPhrase\":\"test\"}";
        new HttpResponseAdapter(content);
    }

    @Test
    public void testAdapterInitFromMap() throws IOException {
        Map<String, Object> map = Map.of("status", 200, "reason", "some reason", "body", new int[]{2, 3, 4});
        HttpResponseAdapter adapter = new HttpResponseAdapter(map);
        Assert.assertEquals(200, (int) adapter.getStatus());
        Assert.assertEquals("some reason", adapter.getReasonPhrase());
        Assert.assertEquals(Arrays.asList(new Integer[]{2, 3, 4}), adapter.getEntity());
        Assert.assertNull(adapter.getHeaders());
    }

    @Test
    public void testAdapterInitFromHttpResponse() throws IOException {
        HttpResponse mock = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "some reason"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        mock.setEntity(new StringEntity("{\"a\":100}"));
        mock.setHeader(new BasicHeader("Content-Type", "application/json"));
        mock.setHeader(new BasicHeader("Accept", "application/json"));
        HttpResponseAdapter adapter = new HttpResponseAdapter(mock);
        Assert.assertEquals(200, (int) adapter.getStatus());
        Assert.assertEquals("some reason", adapter.getReasonPhrase());
        Assert.assertEquals("{\"a\":100}", adapter.getEntity());
        Assert.assertEquals(Map.of("Content-Type", "application/json", "Accept", "application/json"), adapter.getHeaders());
    }
}
