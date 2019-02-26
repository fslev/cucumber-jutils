package com.cucumber.utils.clients.http;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class HttpRequestLoggerInterceptor implements HttpRequestInterceptor {

    private Logger log = LogManager.getLogger();

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
        log.debug("Request BODY:{}{}", () -> System.lineSeparator(), () -> {
            String content = null;
            HttpEntityEnclosingRequest entityEnclosingRequest;
            if (request instanceof HttpEntityEnclosingRequest) {
                entityEnclosingRequest = (HttpEntityEnclosingRequest) request;
                try {
                    content = EntityUtils.toString(entityEnclosingRequest.getEntity());
                } catch (IOException e) {
                    log.error(e);
                } finally {
                    try {
                        if (entityEnclosingRequest != null && content != null) {
                            entityEnclosingRequest.setEntity(new StringEntity(content));
                        }
                    } catch (UnsupportedEncodingException e) {
                        log.error(e);
                    }
                }
            }
            return content != null ? content : "N/A";
        });
        log.debug("----------------------");
    }
}
