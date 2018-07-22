package ro.cucumber.core.context.props;

import cucumber.runtime.java.guice.ScenarioScoped;

import java.util.HashMap;
import java.util.Map;

@ScenarioScoped
public class ScenarioProps {
    private Map<String, Object> props = new HashMap<>();

    public Object get(String key) {
        return props.get(key);
    }

    public void put(String key, Object val) {
        props.put(key, val);
    }

    public void putAll(Map<String, Object> props) {
        this.props.putAll(props);
    }
}
