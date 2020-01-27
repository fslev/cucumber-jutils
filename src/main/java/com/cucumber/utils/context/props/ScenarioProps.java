package com.cucumber.utils.context.props;

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

    public String getAsString(String key) {
        Object val = get(key);
        return val != null ? val.toString() : null;
    }

    public Object get(String key) {
        if (key == null) {
            return props.get(null);
        }
        String trimmedKey = key.trim();
        switch (trimmedKey.toLowerCase()) {
            case "uid":
                return getUUID();
            case "now":
                return getTimeInMillis();
            case "short-random":
                return (int) (Math.random() * (Short.MAX_VALUE - Short.MIN_VALUE));
            default:
                return props.get(trimmedKey) instanceof String ?
                        new ScenarioPropsParser(this, props.get(trimmedKey).toString()).result() : props.get(trimmedKey);
        }
    }

    public void put(String key, Object val) {
        String trimmedKey = (key == null) ? null : key.trim();
        if (props.get(trimmedKey) != null) {
            log.warn("Scenario property \"{}\" will be overridden with {}", trimmedKey, val);
        }
        props.put(trimmedKey, val);
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
        TXT(".txt"),
        CSV(".csv"),
        HTML(".html"),
        MD(".md"),
        TEXT(".text"),
        YANG(".yang");

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

    @Override
    public String toString() {
        return "ScenarioProps{" +
                "props=" + props +
                '}';
    }
}
