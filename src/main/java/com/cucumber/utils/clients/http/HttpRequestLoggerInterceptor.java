package com.cucumber.utils.clients.http;

import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;

public class HttpRequestLoggerInterceptor implements HttpRequestInterceptor {

    private final Logger log = LogManager.getLogger();

    @Override
    public void process(HttpRequest request, HttpContext context) {
        log.debug("---- HTTP REQUEST ----");
        log.debug("{}: {}{}", request.getRequestLine().getMethod(), HttpClientContext.adapt(context).getTargetHost().toURI(),
                request.getRequestLine().getUri());
        log.debug("PROXY host: {}", () -> {
            RequestConfig config = HttpClientContext.adapt(context).getRequestConfig();
            HttpHost proxy = config.getProxy();
            return proxy != null ? proxy.toURI() : "N/A";
        });
        log.debug("Request HEADERS: {}", Arrays.asList(request.getAllHeaders()));
        log.debug("Request BODY:{}{}", System::lineSeparator, () -> {
            String content = null;
            HttpEntityEnclosingRequest entityEnclosingRequest;
            if (request instanceof HttpEntityEnclosingRequest) {
                entityEnclosingRequest = (HttpEntityEnclosingRequest) request;
                HttpEntity entity = entityEnclosingRequest.getEntity();
                try {
                    content = EntityUtils.toString(entity);
                } catch (IOException e) {
                    log.error(e);
                } finally {
                    try {
                        EntityUtils.consume(entity);
                        if (content != null) {
                            entityEnclosingRequest.setEntity(new StringEntity(content));
                        }
                    } catch (IOException e) {
                        log.error(e);
                    }
                }
            }
            return content != null ? content : "N/A";
        });
        log.debug("----------------------");
    }
}
