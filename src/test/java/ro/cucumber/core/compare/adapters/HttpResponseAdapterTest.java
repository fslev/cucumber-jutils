package ro.cucumber.core.compare.adapters;

import ro.cucumber.core.context.compare.adapters.HttpResponseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

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
}
