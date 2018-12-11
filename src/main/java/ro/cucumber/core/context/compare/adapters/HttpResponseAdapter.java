package ro.cucumber.core.context.compare.adapters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpResponseAdapter {

    @JsonProperty(value = "status")
    private Integer status;
    @JsonProperty(value = "body")
    private Object entity;
    @JsonProperty(value = "reason")
    private String reasonPhrase;
    @JsonProperty(value = "headers")
    private Map<String, String> headers;

    public HttpResponseAdapter() {
    }

    public HttpResponseAdapter(Object object) throws IOException {
        if (object instanceof HttpResponse) {
            constructFromHttpResponse((HttpResponse) object);
        } else {
            constructFromObject(object);
        }
    }

    private void constructFromObject(Object content) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
        try {
            HttpResponseAdapter adapter = content instanceof String ?
                    mapper.readValue((String) content, HttpResponseAdapter.class) :
                    mapper.convertValue(content, HttpResponseAdapter.class);
            this.status = adapter.status;
            this.entity = adapter.entity;
            this.reasonPhrase = adapter.reasonPhrase;
            this.headers = adapter.headers;
        } catch (Exception e) {
            throw new IOException("Cannot init HTTP response adapter - invalid input");
        }
    }

    private void constructFromHttpResponse(HttpResponse response) throws IOException {
        this.status = response.getStatusLine().getStatusCode();
        this.reasonPhrase = response.getStatusLine().getReasonPhrase();
        this.headers = getHeaders(response);
        String content = null;
        if (response.getEntity() != null) {
            try {
                content = EntityUtils.toString(response.getEntity());
                this.entity = content;
            } catch (Exception e) {
                throw new IOException("Cannot init HTTP response adapter", e);
            } finally {
                if (content != null) {
                    response.setEntity(new StringEntity(content));
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

    public Integer getStatus() {
        return status;
    }

    public Object getEntity() {
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
        return "HttpResponseAdapter{" +
                "status=" + status +
                ", entity='" + entity + '\'' +
                ", reasonPhrase='" + reasonPhrase + '\'' +
                ", headers=" + headers +
                '}';
    }
}
