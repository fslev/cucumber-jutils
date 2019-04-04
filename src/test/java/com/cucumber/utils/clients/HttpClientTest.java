package com.cucumber.utils.clients;

import com.cucumber.utils.clients.http.HttpClient;
import com.cucumber.utils.clients.http.Method;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

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
                        HttpEntity entity = response.getEntity();
                        if (entity == null) {
                            return true;
                        }
                        try {
                            log.info("SERVICE retry: {}", executionCount);
                            if (entity != null) {
                                content = EntityUtils.toString(entity);
                            }
                            return !content.equals("") && executionCount < 3;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } finally {
                            try {
                                EntityUtils.consume(entity);
                                if (content != null) {
                                    response.setEntity(new StringEntity(content));

                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
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
