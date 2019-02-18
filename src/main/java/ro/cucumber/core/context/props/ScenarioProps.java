package ro.cucumber.core.context.props;

import cucumber.runtime.java.guice.ScenarioScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.cucumber.core.context.config.CustomInjectorSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ScenarioScoped
public class ScenarioProps {
    private Logger log = LogManager.getLogger();
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

    private String getUUID() {
        return UUID.randomUUID().toString();
    }

    private String getTimeInMillis() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static ScenarioProps getScenarioProps() {
        return CustomInjectorSource.getContextInjector().getInstance(ScenarioProps.class);
    }

    public enum FileExtension {
        PROPERTIES(".properties"),
        PROPERTY(".property"),
        YAML(".yaml"),
        YML(".yml");

        private String name;

        FileExtension(String name) {
            this.name = name;
        }

        public static String[] stringValues() {
            return Arrays.stream(values()).map(FileExtension::toString).toArray(String[]::new);
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
