package ro.cucumber.core.context.props;

import com.google.inject.Inject;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.cucumber.core.context.config.CustomInjectorSource;
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

    @Inject
    public void init() {
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
            log.warn("Scenario property \"{}\" will be overridden with {}", key, val);
        }
        props.put(key, val);
    }

    public void putAll(Map<String, Object> props) {
        this.props.putAll(props);
    }

    public void loadPropsFromPath(String filePath) {
        Properties p = null;
        try {
            p = ResourceUtils.readProps(filePath);
        } catch (Exception e) {
            return;
        }
        p.forEach((k, v) -> {
            put(k.toString(), new PlaceholderFiller(v.toString().trim()).fill());
        });
    }

    private String getUUID() {
        return UUID.randomUUID().toString();
    }

    private String getTimeInMillis() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static ScenarioProps getScenarioProps() {
        return CustomInjectorSource.getContextInjector().getInstance(ScenarioProps.class);
    }
}
