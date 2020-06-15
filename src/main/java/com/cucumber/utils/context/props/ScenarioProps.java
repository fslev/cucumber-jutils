package com.cucumber.utils.context.props;

import io.cucumber.guice.ScenarioScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@ScenarioScoped
public class ScenarioProps {

    private final Logger log = LogManager.getLogger();
    private final Map<String, Object> props = new HashMap<>();

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
                return UUID.randomUUID().toString();
            case "now":
                return System.currentTimeMillis();
            case "short-random":
                return (int) (Math.random() * (Short.MAX_VALUE - Short.MIN_VALUE));
            default:
                return props.get(trimmedKey) instanceof String ?
                        ScenarioPropsParser.parse(props.get(trimmedKey).toString(), this) : props.get(trimmedKey);
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
        props.forEach(this::put);
    }

    public Set<String> keySet() {
        return props.keySet();
    }

    public boolean containsKey(String key) {
        return props.containsKey(key);
    }

    public int size() {
        return props.size();
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
        TEXT(".text"),
        YANG(".yang");

        private final String name;

        FileExtension(String name) {
            this.name = name;
        }

        public static String[] allExtensions() {
            return Arrays.stream(values()).map(FileExtension::value).toArray(String[]::new);
        }

        public static String[] propertyFileExtensions() {
            return Arrays.stream(allExtensions())
                    .filter(val -> val.equals(PROPERTY.value()) || val.equals(XML.value())
                            || val.equals(JSON.value()) || val.equals(TXT.value())
                            || val.equals(HTML.value()) || val.equals(TEXT.value())
                            || val.equals(CSV.value()) || val.equals(YANG.value())
                    )
                    .toArray(String[]::new);
        }

        public String value() {
            return name;
        }
    }

    @Override
    public String toString() {
        return this.props.toString();
    }
}
