package ro.cucumber.core.context.props;

import cucumber.runtime.java.guice.ScenarioScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.cucumber.core.engineering.utils.ResourceUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

@ScenarioScoped
public class ScenarioProps {
    private final static String ENV_FILE = "scenario.properties";
    private Logger log = LogManager.getLogger();
    private Map<String, Object> props = new HashMap<>();

    public ScenarioProps() {
        loadPropsFromPath(ENV_FILE);
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

    public void loadPropsFromPath(String filePath) {
        Properties p = ResourceUtils.readProps(filePath);
        p.forEach((k, v) -> put(k.toString(), v.toString().trim()));
    }

    private String getUUID() {
        return UUID.randomUUID().toString();
    }

    private String getTimeInMillis() {
        return String.valueOf(System.currentTimeMillis());
    }
}
