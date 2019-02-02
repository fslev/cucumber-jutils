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
    private final static String GLOBAL_FILE_PROPS = "scenario.properties";
    private final static String GLOBAL_FILE_YAML = "scenario.yaml";
    private Logger log = LogManager.getLogger();
    private Map<String, Object> props = new HashMap<>();

    @Inject
    public void init() {
        try {
            loadPropsFromPropertiesFile(GLOBAL_FILE_PROPS);
        } catch (Exception e) {
            log.info("Cannot read scenario props from global " + GLOBAL_FILE_PROPS);
        }
        try {
            loadPropsFromYamlFile(GLOBAL_FILE_PROPS);
        } catch (Exception e) {
            log.info("Cannot read scenario props from global " + GLOBAL_FILE_YAML);
        }
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
                return props.get(key) instanceof String ?
                        new PlaceholderFiller(props.get(key).toString()).fill()
                        : props.get(key);
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
        if (filePath.endsWith(".properties")) {
            loadPropsFromPropertiesFile(filePath);
        } else if (filePath.endsWith(".yaml")) {
            loadPropsFromYamlFile(filePath);
        }
    }

    private String getUUID() {
        return UUID.randomUUID().toString();
    }

    private String getTimeInMillis() {
        return String.valueOf(System.currentTimeMillis());
    }

    private void loadPropsFromPropertiesFile(String filePath) {
        Properties p = ResourceUtils.readProps(filePath);
        p.forEach((k, v) -> put(k.toString(), v.toString().trim()));
    }

    private void loadPropsFromYamlFile(String filePath) {
        Map<String, Object> map = ResourceUtils.readYaml(filePath);
        map.forEach((k, v) -> put(k, v));
    }

    public static ScenarioProps getScenarioProps() {
        return CustomInjectorSource.getContextInjector().getInstance(ScenarioProps.class);
    }
}
