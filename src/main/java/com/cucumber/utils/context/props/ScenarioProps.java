package com.cucumber.utils.context.props;

import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ScenarioScoped
public class ScenarioProps {
    private Logger log = LogManager.getLogger();
    private Map<String, Object> props = new HashMap<>();

    @Inject
    private ScenarioProps() {
    }

    public Object get(String key) {
        switch (key.toLowerCase()) {
            case "uid":
                return getUUID();
            case "now":
                return getTimeInMillis();
            case "short-random":
                return (int) (Math.random() * (Short.MAX_VALUE - Short.MIN_VALUE));
            default:
                return props.get(key) instanceof String ?
                        new ScenarioPropsParser(this, props.get(key).toString()).result() : props.get(key);
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

    public enum FileExtension {
        PROPERTIES(".properties"),
        YAML(".yaml"),
        YML(".yml"),
        PROPERTY(".property"),
        JSON(".json"),
        XML(".xml"),
        TXT(".txt");

        private String name;

        FileExtension(String name) {
            this.name = name;
        }

        public static String[] allExtensions() {
            return Arrays.stream(values()).map(FileExtension::value).toArray(String[]::new);
        }

        public static String[] propertyFileExtensions() {
            return Arrays.stream(allExtensions())
                    .filter(val -> val.equals(PROPERTY.value())
                            || val.equals(XML.value())
                            || val.equals(JSON.value())
                            || val.equals(TXT.value()))
                    .toArray(String[]::new);
        }

        public String value() {
            return name;
        }
    }
}
