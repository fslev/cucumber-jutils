package ro.cucumber.core.clients;

import ro.cucumber.core.clients.http.HttpClient;
import ro.cucumber.core.clients.http.Method;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class HttpClientTest {

    private Logger log = LogManager.getLogger(this.getClass());

    @Test
    public void testServiceRetryStrategy() throws IOException {
        HttpClient.Builder builder = new HttpClient.Builder()
                .address("http://www.google.com")
                .method(Method.GET)
                .addHeader("some header", "test")
                .serviceUnavailableRetryStrategy(new ServiceUnavailableRetryStrategy() {
                    @Override
                    public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
                        String content = null;
                        try {
                            log.info("SERVICE retry: {}", executionCount);
                            content = EntityUtils.toString(response.getEntity());
                            return executionCount < 3 && !content.equals("");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public long getRetryInterval() {
                        return 3000;
                    }
                });
        log.info(EntityUtils.toString(builder.build().execute().getEntity()));
    }
}
