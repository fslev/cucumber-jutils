package ro.cucumber.core.context.config;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

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
