package ro.cucumber.poc.http;

import cucumber.runtime.java.guice.ScenarioScoped;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

@ScenarioScoped
public final class HttpClient {
    private HttpClientBuilder clientBuilder;
    private HttpRequestBuilder requestBuilder;
    protected HttpResponse response;

    public HttpClient() {
        init();
    }

    public void init() {
        this.clientBuilder = new HttpClientBuilder();
        this.requestBuilder = new HttpRequestBuilder();
        this.response = null;
    }

    public void setAddress(String address) {
        this.requestBuilder.address(address);
    }

    public void setPath(String path) {
        requestBuilder.setPath(path);
    }

    public void addHeader(String name, String val) {
        requestBuilder.addHeader(name, val);
    }

    public void addQueryParam(String name, String val) {
        requestBuilder.addQueryParam(name, val);
    }

    public void setMethod(HttpVerb verb) {
        requestBuilder.method(verb);
    }

    public void setEntity(String entity) {
        requestBuilder.entity(entity);
    }

    public void useProxy(String host, int port, String scheme) {
        clientBuilder.useProxy(host, port, scheme);
    }

    public void setTimeout(int timeout) {
        clientBuilder.timeout(timeout);
    }

    public void execute() {
        CloseableHttpClient client = clientBuilder.build();
        try {
            response = client.execute(requestBuilder.build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse getResponse() {
        return response;
    }
}
