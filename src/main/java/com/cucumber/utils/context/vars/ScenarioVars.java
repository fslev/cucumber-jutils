package com.cucumber.utils.context.vars;

import io.cucumber.guice.ScenarioScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@ScenarioScoped
public class ScenarioVars {

    private static final Logger LOG = LogManager.getLogger();
    private final Map<String, Object> vars = new HashMap<>();

    public String getAsString(String key) {
        Object val = get(key);
        return val != null ? val.toString() : null;
    }

    public Object get(String key) {
        if (key == null) {
            return vars.get(null);
        }
        String trimmedKey = key.trim();
        switch (trimmedKey.toLowerCase()) {
            case "uid":
                return UUID.randomUUID().toString();
            case "now":
                return System.currentTimeMillis();
            case "short-random":
                return (int) (Math.random() * Short.MAX_VALUE);
            case "int-random":
                return (int) (Math.random() * Integer.MAX_VALUE);
            default:
                return vars.get(trimmedKey) instanceof String ?
                        ScenarioVarsParser.parse(vars.get(trimmedKey).toString(), this) : vars.get(trimmedKey);
        }
    }

    public void put(String key, Object val) {
        String trimmedKey = (key == null) ? null : key.trim();
        if (vars.get(trimmedKey) != null) {
            LOG.warn("Scenario variable \"{}\" will be overridden with {}", trimmedKey, val);
        }
        vars.put(trimmedKey, val);
    }

    public void putAll(Map<String, Object> vars) {
        vars.forEach(this::put);
    }

    public Set<String> keySet() {
        return vars.keySet();
    }

    public boolean containsKey(String key) {
        return vars.containsKey(key);
    }

    public int size() {
        return vars.size();
    }

    public enum FileExtension {
        PROPERTIES(".properties"),
        YAML(".yaml"),
        YML(".yml"),
        JSON(".json"),
        XML(".xml"),
        TXT(".txt"),
        CSV(".csv"),
        HTML(".html"),
        TEXT(".text");

        private final String name;

        FileExtension(String name) {
            this.name = name;
        }

        public static String[] allExtensions() {
            return Arrays.stream(values()).map(FileExtension::value).toArray(String[]::new);
        }

        public static String[] varFileExtensions() {
            return Arrays.stream(allExtensions())
                    .filter(val -> val.equals(XML.value()) || val.equals(JSON.value())
                            || val.equals(TXT.value()) || val.equals(HTML.value())
                            || val.equals(TEXT.value()) || val.equals(CSV.value()))
                    .toArray(String[]::new);
        }

        public String value() {
            return name;
        }
    }

    @Override
    public String toString() {
        return this.vars.toString();
    }
}
