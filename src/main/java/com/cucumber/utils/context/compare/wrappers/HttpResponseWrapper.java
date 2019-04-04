package com.cucumber.utils.context.compare.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseWrapper {

    @JsonProperty(value = "status")
    private String status;
    @JsonProperty(value = "body")
    private Object entity;
    @JsonProperty(value = "reason")
    private String reasonPhrase;
    @JsonProperty(value = "headers")
    private Map<String, String> headers;

    public HttpResponseWrapper() {
    }

    public HttpResponseWrapper(Object object) throws IOException {
        if (object instanceof HttpResponse) {
            fromHttpResponse((HttpResponse) object);
        } else {
            fromObject(object);
        }
    }

    private void fromObject(Object content) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
        try {
            HttpResponseWrapper wrapper = content instanceof String ?
                    mapper.readValue((String) content, HttpResponseWrapper.class) :
                    mapper.convertValue(content, HttpResponseWrapper.class);
            this.status = wrapper.status;
            this.entity = wrapper.entity;
            this.reasonPhrase = wrapper.reasonPhrase;
            this.headers = wrapper.headers;
        } catch (Exception e) {
            throw new IOException("HTTP Response wrapper of invalid format\n");
        }
    }

    private void fromHttpResponse(HttpResponse response) throws IOException {
        this.status = String.valueOf(response.getStatusLine().getStatusCode());
        this.reasonPhrase = response.getStatusLine().getReasonPhrase();
        this.headers = getHeaders(response);
        String content = null;
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            try {
                content = EntityUtils.toString(entity);
                this.entity = content;
            } catch (Exception e) {
                throw new IOException("HTTP Response wrapper of invalid format", e);
            } finally {
                EntityUtils.consume(entity);
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

    public String getStatus() {
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
        return "{" +
                (status != null ? "status=" + status : "") +
                (reasonPhrase != null ? ", reason='" + reasonPhrase + '\'' : "") +
                (entity != null ? ", body='" + entity + '\'' : "") +
                (headers != null ? ", headers=" + headers : "") +
                '}';
    }
}
