package ro.cucumber.poc.http;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;

public class HttpRequestBuilder {

    private Map<String, String> headers;
    private URIBuilder uriBuilder;
    private String address;
    private String entity;
    private HttpVerb verb;

    public HttpRequestBuilder() {
        this.headers = new HashMap<>();
        this.uriBuilder = new URIBuilder();
    }

    public HttpRequestBase build() {
        if (verb == null) {
            throw new RuntimeException("Define HTTP method");
        }
        HttpRequestBase request = getHttpRequestBase();
        setHeaders(request);
        return request;
    }

    private HttpRequestBase getHttpRequestBase() {
        String url;
        try {
            url = address + "/" + uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        switch (verb) {
            case GET:
                return new HttpGet(url);
            case POST:
                HttpPost post = new HttpPost(url);
                try {
                    post.setEntity(new StringEntity(entity != null ? entity : ""));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                return post;
            case PUT:
                HttpPut put = new HttpPut(url);
                try {
                    put.setEntity(new StringEntity(entity != null ? entity : ""));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                return put;
            case DELETE:
                return new HttpDelete(url);
            default:
                throw new RuntimeException("HTTP verb is missing");
        }
    }

    public HttpRequestBuilder address(String address) {
        this.address = address;
        return this;
    }

    public HttpRequestBuilder setPath(String path) {
        this.uriBuilder.setPath(path);
        return this;
    }

    public HttpRequestBuilder addHeader(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    public HttpRequestBuilder addQueryParam(String name, String value) {
        this.uriBuilder.addParameter(name, value);
        return this;
    }

    public HttpRequestBuilder entity(String entity) {
        this.entity = entity;
        return this;
    }

    public HttpRequestBuilder method(HttpVerb verb) {
        this.verb = verb;
        return this;
    }

    private void setHeaders(HttpRequestBase request) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request.setHeader(entry.getKey(), entry.getValue());
        }
    }
}


