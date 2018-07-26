package ro.cucumber.core.context.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class CustomDataTable implements Serializable {

    @JsonProperty
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
