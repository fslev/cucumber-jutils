package ro.cucumber.core.context.props;

import cucumber.runtime.java.guice.ScenarioScoped;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ScenarioScoped
public class ScenarioProps {
    private Map<String, Object> props = new HashMap<>();

    public Object get(String key) {
        switch (key) {
            case "uid":
            case "UID":
                return getUUID();
            case "now":
            case "NOW":
                return getTimeInMillis();
            default:
                return props.get(key);
        }
    }

    public void put(String key, Object val) {
        props.put(key, val);
    }

    public void putAll(Map<String, Object> props) {
        this.props.putAll(props);
    }

    private String getUUID() {
        return UUID.randomUUID().toString();
    }

    private String getTimeInMillis() {
        return String.valueOf(System.currentTimeMillis());
    }
}
