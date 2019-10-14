package com.cucumber.utils.clients.http;

import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import javax.annotation.concurrent.NotThreadSafe;
import javax.net.ssl.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClient {
    private Integer timeout;
    private HttpHost proxyHost;
    private String uri;
    private Map<String, String> headers;
    private String requestEntity;
    private Method method;
    private SSLContext sslContext;
    private HostnameVerifier hostnameVerifier;
    private HttpRequestRetryHandler requestRetryHandler;
    private ServiceUnavailableRetryStrategy serviceUnavailableRetryStrategy;
    private HttpClientBuilder clientBuilder;

    private CloseableHttpClient client;
    private HttpRequestBase request;

    protected HttpClient(Builder builder) {

        validateMethod(builder);
        validateAddress(builder);

        this.proxyHost = builder.proxyHost;
        this.timeout = builder.timeout;
        try {
            this.uri = new URIBuilder(builder.address + (builder.uriBuilder.isPathEmpty() ? "" : builder.uriBuilder.build().toString())).build().toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        this.headers = builder.headers;
        this.requestEntity = builder.requestEntity;
        this.method = builder.method;
        this.sslContext = builder.sslContext;
        this.hostnameVerifier = builder.hostnameVerifier;
        this.requestRetryHandler = builder.requestRetryHandler;
        this.serviceUnavailableRetryStrategy = builder.serviceUnavailableRetryStrategy;
        this.clientBuilder = builder.clientBuilder;

        this.client = getClient();
        this.request = getRequest();
    }

    public CloseableHttpResponse execute() {
        try {
            return client.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() throws IOException {
        this.client.close();
    }

    private CloseableHttpClient getClient() {
        RequestConfig.Builder configBuilder = RequestConfig.custom();
        if (timeout != null) {
            configBuilder.setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
                    .setSocketTimeout(timeout);
        }
        if (proxyHost != null) {
            configBuilder.setProxy(proxyHost);
        }
        clientBuilder.setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext == null ?
                defaultSslContext() : sslContext, hostnameVerifier == null ?
                new NoopHostnameVerifier() : hostnameVerifier))
                .setDefaultRequestConfig(configBuilder.build());
        if (requestRetryHandler != null) {
            clientBuilder.setRetryHandler(requestRetryHandler);
        }
        if (serviceUnavailableRetryStrategy != null) {
            clientBuilder.setServiceUnavailableRetryStrategy(serviceUnavailableRetryStrategy);
        }
        return clientBuilder.addInterceptorLast(new HttpResponseLoggerInterceptor())
                .addInterceptorLast(new HttpRequestLoggerInterceptor()).build();
    }

    private SSLContext defaultSslContext() {
        SSLContext ctx;
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()},
                    new SecureRandom());
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return ctx;
    }

    private HttpRequestBase getRequest() {

        HttpRequestBase request;
        switch (method) {
            case GET:
                request = new HttpGet(uri);
                break;
            case POST:
                HttpPost post = new HttpPost(uri);
                try {
                    post.setEntity(new StringEntity(requestEntity != null ? requestEntity : ""));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                request = post;
                break;
            case PUT:
                HttpPut put = new HttpPut(uri);
                try {
                    put.setEntity(new StringEntity(requestEntity != null ? requestEntity : ""));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                request = put;
                break;
            case DELETE:
                if (requestEntity == null || requestEntity.isEmpty()) {
                    request = new HttpDelete(uri);
                } else {
                    HttpDeleteWithBody deleteWithBody = new HttpDeleteWithBody(uri);
                    try {
                        deleteWithBody.setEntity(new StringEntity(requestEntity));
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    request = deleteWithBody;
                }
                break;
            case OPTIONS:
                request = new HttpOptions(uri);
                break;
            case TRACE:
                request = new HttpTrace(uri);
                break;
            case HEAD:
                request = new HttpHead(uri);
                break;
            case PATCH:
                HttpPatch patch = new HttpPatch(uri);
                try {
                    patch.setEntity(new StringEntity(requestEntity != null ? requestEntity : ""));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                request = patch;
                break;
            default:
                throw new IllegalStateException("Invalid HTTP method");
        }
        setHeaders(request);
        return request;
    }

    private void validateMethod(Builder builder) {
        if (builder.method == null) {
            throw new IllegalStateException("HTTP Method missing");
        }
    }

    private void validateAddress(Builder builder) {
        if (builder.address == null) {
            throw new IllegalStateException("HTTP Address missing");
        }
    }

    public Integer getTimeout() {
        return timeout;
    }

    public HttpHost getProxyHost() {
        return proxyHost;
    }

    public String getUri() {
        return this.uri;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    private void setHeaders(HttpRequestBase request) {
        headers.entrySet().forEach(e -> request.setHeader(e.getKey(), e.getValue()));
    }

    public String getRequestEntity() {
        return requestEntity;
    }

    public Method getMethod() {
        return method;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public HttpRequestRetryHandler getRequestRetryHandler() {
        return requestRetryHandler;
    }

    public ServiceUnavailableRetryStrategy getServiceUnavailableRetryStrategy() {
        return serviceUnavailableRetryStrategy;
    }

    @Override
    public String toString() {
        return "HttpClient{" + "timeout=" + timeout + ", proxyHost=" + proxyHost + ", uri='"
                + uri + ", headers=" + headers + ", requestEntity='"
                + requestEntity + '\'' + ", method=" + method + '}';
    }

    public static class Builder {
        private Integer timeout;
        private HttpHost proxyHost;
        private String address;
        private URIBuilder uriBuilder = new URIBuilder();
        private Map<String, String> headers = new HashMap<>();
        private String requestEntity;
        private Method method;
        private SSLContext sslContext;
        private HostnameVerifier hostnameVerifier;
        private HttpRequestRetryHandler requestRetryHandler;
        private ServiceUnavailableRetryStrategy serviceUnavailableRetryStrategy;
        private HttpClientBuilder clientBuilder = HttpClients.custom();

        public Builder useProxy(String proxyHost, int proxyPort, String proxyScheme) {
            this.proxyHost = new HttpHost(proxyHost, proxyPort, proxyScheme);
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder address(String address) {
            if (address != null) {
                this.address = address.replaceFirst("/*$", "");
            }
            return this;
        }

        public Builder path(String path) {
            if (path != null) {
                this.uriBuilder.setPath(path.replaceFirst("^/*", ""));
            }
            return this;
        }

        public Builder addHeader(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder setHeaders(Map<String, String> headers) {
            this.headers.clear();
            this.headers.putAll(headers);
            return this;
        }

        public Builder addQueryParam(String name, String value) {
            this.uriBuilder.addParameter(name, value);
            return this;
        }

        public Builder setQueryParam(String name, String value) {
            this.uriBuilder.setParameter(name, value);
            return this;
        }

        public Builder setQueryParams(Map<String, String> queryParams) {
            List<NameValuePair> paramsList = new ArrayList<>();
            queryParams.forEach((k, v) -> paramsList.add(new BasicNameValuePair(k, v)));
            this.uriBuilder.setParameters(paramsList);
            return this;
        }

        public Builder entity(String entity) {
            this.requestEntity = entity;
            return this;
        }

        public Builder method(Method method) {
            this.method = method;
            return this;
        }

        public Builder sslContext(SSLContext sslContext) {
            this.sslContext = sslContext;
            return this;
        }

        public Builder hostnameVerifier(HostnameVerifier hostnameVerifier) {
            this.hostnameVerifier = hostnameVerifier;
            return this;
        }

        public Builder requestRetryHandler(HttpRequestRetryHandler requestRetryHandler) {
            this.requestRetryHandler = requestRetryHandler;
            return this;
        }

        public Builder serviceUnavailableRetryStrategy(ServiceUnavailableRetryStrategy serviceRetryStrategy) {
            this.serviceUnavailableRetryStrategy = serviceRetryStrategy;
            return this;
        }

        public Builder clientBuilder(HttpClientBuilder clientBuilder) {
            this.clientBuilder = clientBuilder;
            return this;
        }

        public HttpClient build() {
            return new HttpClient(this);
        }
    }
}


@NotThreadSafe
class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "DELETE";

    public HttpDeleteWithBody(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    public HttpDeleteWithBody(final URI uri) {
        super();
        setURI(uri);
    }

    public HttpDeleteWithBody() {
        super();
    }

    public String getMethod() {
        return METHOD_NAME;
    }
}

class DefaultTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}