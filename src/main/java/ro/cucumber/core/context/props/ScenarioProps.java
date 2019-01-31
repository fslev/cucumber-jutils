package ro.cucumber.core.context.props;

import cucumber.runtime.java.guice.ScenarioScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

@ScenarioScoped
public class ScenarioProps {
    private final static String ENV_FILE = "env.properties";
    private Logger log = LogManager.getLogger();
    private Map<String, Object> props = new HashMap<>();

    public ScenarioProps() {
        loadEnvProps();
    }

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
        if (props.get(key) != null) {
            log.warn("Scenario property \"{}\" will be overridden", key);
        }
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

    private void loadEnvProps() {
        Properties p = new Properties();
        try {
            p.load(ScenarioProps.class.getClassLoader().getResourceAsStream(ENV_FILE));
            p.forEach((k, v) -> this.props.put(k.toString(), v.toString().trim()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
