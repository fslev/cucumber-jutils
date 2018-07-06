package ro.cucumber.poc.http;

import io.cucumber.datatable.DataTable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

public class HttpBaseStepDefs {
    private HttpClientBuilder clientBuilder;
    private HttpRequestBuilder requestBuilder;
    protected HttpResponse response;

    protected void init() {
        this.clientBuilder = new HttpClientBuilder();
        this.requestBuilder = new HttpRequestBuilder();
        this.response = null;
    }

    protected void setAddress(String address) {
        this.requestBuilder.address(address);
    }

    protected void setPath(String path) {
        requestBuilder.setPath(path);
    }

    protected void setHeaders(DataTable headers) {
        List<Map<String, String>> list = headers.asMaps();
        if (!list.isEmpty()) {
            for (Map.Entry<String, String> entry : list.get(list.size() - 1).entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    protected void setQueryParams(DataTable params) {
        List<Map<String, String>> list = params.asMaps();
        if (!list.isEmpty()) {
            for (Map.Entry<String, String> e : list.get(list.size() - 1).entrySet()) {
                requestBuilder.addQueryParam(e.getKey(), e.getValue());
            }
        }
    }

    protected void setMethod(HttpVerb verb) {
        requestBuilder.method(verb);
    }

    protected void setEntity(String entity) {
        requestBuilder.entity(entity);
    }

    protected void useProxy(String host, int port, String scheme) {
        clientBuilder.useProxy(host, port, scheme);
    }

    protected void setTimeout(int timeout) {
        clientBuilder.timeout(timeout);
    }

    protected void execute() {
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
