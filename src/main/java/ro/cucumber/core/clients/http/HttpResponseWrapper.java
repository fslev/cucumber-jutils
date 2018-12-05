package ro.cucumber.core.clients.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HttpResponseWrapper {

    @JsonProperty(value = "status")
    private int status;
    @JsonProperty(value = "body")
    private String entity;
    @JsonProperty(value = "reason")
    private String reasonPhrase;
    @JsonProperty(value = "headers")
    private Map<String, String> headers;


    public HttpResponseWrapper(HttpResponse response) {
        this.status = response.getStatusLine().getStatusCode();
        this.reasonPhrase = response.getStatusLine().getReasonPhrase();
        this.headers = getHeaders(response);
        if (response.getEntity() != null) {
            try {
                this.entity = EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (entity != null) {
                    try {
                        response.setEntity(new StringEntity(entity));
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private Map<String, String> getHeaders(HttpResponse response) {
        Map<String, String> headers = new HashMap<>();
        for (Header h : response.getAllHeaders()) {
            headers.put(h.getName(), h.getValue());
        }
        return headers;
    }

    public int getStatus() {
        return status;
    }

    public String getEntity() {
        return entity;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        return "HttpResponseWrapper{" +
                "status=" + status +
                ", entity='" + entity + '\'' +
                ", reasonPhrase='" + reasonPhrase + '\'' +
                ", headers=" + headers +
                '}';
    }
}
