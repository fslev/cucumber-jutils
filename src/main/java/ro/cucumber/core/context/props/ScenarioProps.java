package ro.cucumber.core.context.props;

import cucumber.runtime.java.guice.ScenarioScoped;

import java.util.HashMap;
import java.util.Map;

@ScenarioScoped
public class ScenarioProps {
    private Map<String, String> props = new HashMap<>();

    public Object get(String key) {
        return props.get(key);
    }

    public void put(String key, String val) {
        props.put(key, val);
    }

    public void putAll(Map<String, String> props) {
        this.props.putAll(props);
    }
}
