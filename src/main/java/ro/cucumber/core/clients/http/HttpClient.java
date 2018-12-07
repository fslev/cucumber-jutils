package ro.cucumber.core.clients.http;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class HttpClient {
    private Integer timeout;
    private HttpHost proxyHost;
    private String address;
    private URIBuilder uriBuilder;
    private Map<String, String> headers;
    private String requestEntity;
    private Method method;
    private SSLContext sslContext;
    private HostnameVerifier hostnameVerifier;
    private HttpRequestRetryHandler requestRetryHandler;
    private ServiceUnavailableRetryStrategy serviceUnavailableRetryStrategy;

    private CloseableHttpClient client;
    private HttpRequestBase request;

    private HttpClient(Builder builder) {
        this.proxyHost = builder.proxyHost;
        this.timeout = builder.timeout;
        this.address = builder.address;
        this.uriBuilder = builder.uriBuilder;
        this.headers = builder.headers;
        this.requestEntity = builder.requestEntity;
        this.method = builder.method;
        this.sslContext = builder.sslContext;
        this.hostnameVerifier = builder.hostnameVerifier;
        this.requestRetryHandler = builder.requestRetryHandler;
        this.serviceUnavailableRetryStrategy = builder.serviceUnavailableRetryStrategy;

        validateMethod();
        validateAddress();

        this.client = getClient();
        this.request = getRequest();
    }

    public HttpResponse execute() {
        try {
            HttpResponse response = client.execute(request);
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        HttpClientBuilder builder = HttpClients.custom()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext == null ?
                        defaultSslContext() : sslContext, hostnameVerifier == null ?
                        new NoopHostnameVerifier() : hostnameVerifier))
                .setDefaultRequestConfig(configBuilder.build());
        if (requestRetryHandler != null) {
            builder.setRetryHandler(requestRetryHandler);
        }
        if (serviceUnavailableRetryStrategy != null) {
            builder.setServiceUnavailableRetryStrategy(serviceUnavailableRetryStrategy);
        }
        return builder.addInterceptorLast(new HttpResponseLoggerInterceptor())
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
        String url;

        try {
            url = address + "/" + uriBuilder.build().toString();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e.getMessage());
        }
        switch (method) {
            case GET:
                request = new HttpGet(url);
                break;
            case POST:
                HttpPost post = new HttpPost(url);
                try {
                    post.setEntity(new StringEntity(requestEntity != null ? requestEntity : ""));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                request = post;
                break;
            case PUT:
                HttpPut put = new HttpPut(url);
                try {
                    put.setEntity(new StringEntity(requestEntity != null ? requestEntity : ""));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                request = put;
                break;
            case DELETE:
                request = new HttpDelete(url);
                break;
            case OPTIONS:
                request = new HttpOptions(url);
                break;
            case TRACE:
                request = new HttpTrace(url);
                break;
            case HEAD:
                request = new HttpHead(url);
                break;
            case PATCH:
                HttpPatch patch = new HttpPatch(url);
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

    private void setHeaders(HttpRequestBase request) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request.setHeader(entry.getKey(), entry.getValue());
        }
    }

    private void validateMethod() {
        if (method == null) {
            throw new IllegalStateException("HTTP Method missing");
        }
    }

    private void validateAddress() {
        if (address == null) {
            throw new IllegalStateException("HTTP Address missing");
        }
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

        public Builder useProxy(String proxyHost, int proxyPort, String proxyScheme) {
            this.proxyHost = new HttpHost(proxyHost, proxyPort, proxyScheme);
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder address(String address) {
            this.address = address.replaceFirst("/*$", "");
            return this;
        }

        public Builder path(String path) {
            this.uriBuilder.setPath(path.replaceFirst("^/*", ""));
            return this;
        }

        public Builder addHeader(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder setHeader(String name, String value) {
            this.headers.clear();
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
            List<NameValuePair> paramsList = new ArrayList();
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

        public HttpClient build() {
            return new HttpClient(this);
        }
    }

    @Override
    public String toString() {
        return "HttpClient{" + "timeout=" + timeout + ", proxyHost=" + proxyHost + ", address='"
                + address + '\'' + ", uriBuilder=" + uriBuilder + ", headers=" + headers
                + ", requestEntity='" + requestEntity + '\'' + ", method=" + method + '}';
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