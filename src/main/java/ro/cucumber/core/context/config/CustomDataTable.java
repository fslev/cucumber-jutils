package ro.cucumber.core.context.config;

import java.util.List;

public class CustomDataTable {

    private List data;

    public CustomDataTable(List data) {
        this.data = data;
    }

    public List getData() {
        return data;
    }

    @Override
    public String toString() {
        return "CustomDataTable{" + "data=" + data + '}';
    }
}
