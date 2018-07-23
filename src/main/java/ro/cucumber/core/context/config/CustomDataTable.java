package ro.cucumber.core.context.config;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CustomDataTable implements Serializable {

    private List<Map<String, String>> data;

    public CustomDataTable(List<Map<String, String>> data) {
        this.data = data;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "CustomDataTable{" +
                "data=" + data +
                '}';
    }
}
