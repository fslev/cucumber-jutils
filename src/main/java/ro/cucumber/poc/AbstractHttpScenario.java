package ro.cucumber.poc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class AbstractHttpScenario {

    private HttpClient client = HttpClientBuilder.create().build();
    private HttpRequestBase request;
    private HttpResponse response;


    protected void setHeaders(Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            this.request.setHeader(entry.getKey(), entry.getValue());
        }
    }

    protected void setURI(String uri) {
        this.request.setURI(URI.create(uri));
    }

    protected void createPostRequest(String entity) throws UnsupportedEncodingException {
        HttpPost post = new HttpPost();
        post.setEntity(new StringEntity(entity));
        this.request = post;
    }

    protected void execute() throws IOException {
        this.response = client.execute(this.request);
    }
}
