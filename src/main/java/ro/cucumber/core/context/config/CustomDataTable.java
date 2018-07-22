package ro.cucumber.core.context.config;

import java.util.List;
import java.util.Map;

public class CustomDataTable {
    private List<Map<String, String>> data;

    public CustomDataTable(List<Map<String, String>> data) {
        this.data = data;
    }

    public List<Map<String, String>> asMaps() {
        return data;
    }

    @Override
    public String toString() {
        return "CustomDataTable{" +
                "data=" + data +
                '}';
    }
}
