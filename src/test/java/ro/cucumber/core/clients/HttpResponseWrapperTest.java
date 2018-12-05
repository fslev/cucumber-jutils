package ro.cucumber.core.clients;

import ro.cucumber.core.clients.http.HttpResponseWrapper;
import ro.cucumber.core.engineering.compare.Compare;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class HttpResponseWrapperTest {

    private Logger log = LogManager.getLogger(this.getClass());

    @Test
    public void testWrapperCompareWithJsonStringRepresentation() throws IOException {
        HttpResponse mock = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, null),
                        HttpClientContext.adapt(new BasicHttpContext()));
        mock.setEntity(new StringEntity("Some body"));
        mock.setHeader(new BasicHeader("Content-Type", "application/json"));
        mock.setHeader(new BasicHeader("Accept", "application/json"));
        new Compare("{\"status\": 200,\"body\":\"Some.*\"}", new HttpResponseWrapper(mock)).compare();
    }

    @Test(expected = AssertionError.class)
    public void testWrapperCompareWithJsonStringRepresentation_negative() throws IOException {
        HttpResponse mock = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, null),
                        HttpClientContext.adapt(new BasicHttpContext()));
        mock.setEntity(new StringEntity("Some body"));
        mock.setHeader(new BasicHeader("Content-Type", "application/json"));
        mock.setHeader(new BasicHeader("Accept", "application/json"));
        new Compare("{\"status\": 200,\"body\":\"Some bodies.*\"}", new HttpResponseWrapper(mock)).compare();
    }
}
