package ro.cucumber.core.http;

import cucumber.runtime.java.guice.ScenarioScoped;
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
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClient {
    private Integer timeout;
    private HttpHost proxyHost;
    private String address;
    private URIBuilder uriBuilder;
    private Map<String, String> headers;
    private String entity;
    private HttpVerb verb;

    private CloseableHttpClient client;
    private HttpRequestBase request;

    private HttpClient(Builder builder) {
        this.proxyHost = builder.proxyHost;
        this.timeout = builder.timeout;
        this.address = builder.address;
        this.uriBuilder = builder.uriBuilder;
        this.headers = builder.headers;
        this.entity = builder.entity;
        this.verb = builder.verb;

        validateVerb();
        validateAddress();

        this.client = getClient();
        this.request = getRequest();
    }

    public HttpResponse execute() {
        try {
            return client.execute(request);
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
        HostnameVerifier allowAllHosts = new NoopHostnameVerifier();
        return HttpClients.custom()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(getSslContext(), allowAllHosts))
                .setDefaultRequestConfig(configBuilder.build()).build();
    }

    private SSLContext getSslContext() {
        SSLContext ctx;
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()},
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
            url = address + "/" + uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e.getMessage());
        }
        switch (verb) {
            case GET:
                request = new HttpGet(url);
                break;
            case POST:
                HttpPost post = new HttpPost(url);
                try {
                    post.setEntity(new StringEntity(entity != null ? entity : ""));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                request = post;
                break;
            case PUT:
                HttpPut put = new HttpPut(url);
                try {
                    put.setEntity(new StringEntity(entity != null ? entity : ""));
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
            default:
                throw new IllegalStateException("Invalid HTTP verb");
        }
        setHeaders(request);
        return request;
    }

    private void setHeaders(HttpRequestBase request) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request.setHeader(entry.getKey(), entry.getValue());
        }
    }

    private void validateVerb() {
        if (verb == null) {
            throw new IllegalStateException("Define HTTP Method");
        }
    }

    private void validateAddress() {
        if (address == null) {
            throw new IllegalStateException("Define HTTP Address");
        }
    }

    public static class Builder {
        private Integer timeout;
        private HttpHost proxyHost;
        private String address;
        private URIBuilder uriBuilder = new URIBuilder();
        private Map<String, String> headers = new HashMap<>();
        private String entity;
        private HttpVerb verb;

        public Builder useProxy(String proxyHost, int proxyPort, String proxyScheme) {
            this.proxyHost = new HttpHost(proxyHost, proxyPort, proxyScheme);
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder path(String path) {
            this.uriBuilder.setPath(path);
            return this;
        }

        public Builder addHeader(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder addQueryParam(String name, String value) {
            this.uriBuilder.addParameter(name, value);
            return this;
        }

        public Builder entity(String entity) {
            this.entity = entity;
            return this;
        }

        public Builder method(HttpVerb verb) {
            this.verb = verb;
            return this;
        }

        public HttpClient build() {
            return new HttpClient(this);
        }
    }
}


class DefaultTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {}

    @Override
    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {}

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
