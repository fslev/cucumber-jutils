package ro.cucumber.poc;

import io.cucumber.datatable.DataTable;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class AbstractHttpScenario {
    private HttpClient client = HttpClientBuilder.create().build();


    protected void setHeaders(DataTable table) {
        System.out.println("test " + table.asMaps());
    }
}
