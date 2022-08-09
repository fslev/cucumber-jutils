package com.cucumber.utils.context.vars;

import com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.guice.ScenarioScoped;
import io.json.compare.util.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@ScenarioScoped
public class ScenarioVars {

    private static final Logger LOG = LogManager.getLogger();
    private final Map<String, Object> vars = new HashMap<>();
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z0-9_$\\-.@]+");

    public String getAsString(String name) {
        Object val = get(name);
        return val != null ? val.toString() : null;
    }

    public Object get(String name) {
        if (name == null) {
            return vars.get(null);
        }
        String trimmedName = name.trim();
        switch (trimmedName.toLowerCase()) {
            case "uid":
                return UUID.randomUUID().toString();
            case "now":
                return System.currentTimeMillis();
            case "short-random":
                return (int) (Math.random() * Short.MAX_VALUE);
            case "int-random":
                return (int) (Math.random() * Integer.MAX_VALUE);
            default:
                Object value = !isPathVariable(name) ? vars.get(trimmedName) : getPathVariableValue(name);
                return value instanceof String ? ScenarioVarsParser.parse(value.toString(), this) : value;
        }
    }

    public void put(String name, Object val) {
        String trimmedName = (name == null) ? null : name.trim();
        if (trimmedName == null || !NAME_PATTERN.matcher(trimmedName).matches()) {
            throw new RuntimeException("Scenario variable not allowed having name: " + trimmedName);
        }
        if (vars.containsKey(trimmedName)) {
            LOG.warn("Scenario variable \"{}\" will be overridden with {}", trimmedName, val);
        }
        vars.put(trimmedName, val);
    }

    public void putAll(Map<String, Object> vars) {
        vars.forEach(this::put);
    }

    public Set<String> nameSet() {
        return vars.keySet();
    }

    public boolean containsVariable(String name) {
        return vars.containsKey(name) || (isPathVariable(name) && getPathVariableValue(name) != null);
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

    private Object getPathVariableValue(String varName) {
        List<String> paths = extractPaths(varName);
        String rootPath = paths.get(0);
        if (vars.containsKey(rootPath)) {
            Object rootValue = vars.get(rootPath);
            try {
                JsonNode rootJsonValue = JsonUtils.toJson(rootValue);
                String relativePath = "/" + paths.get(1);
                JsonNode jsonValue = rootJsonValue.at(relativePath);
                if (!jsonValue.isMissingNode()) {
                    return rootValue instanceof String ? (jsonValue.isValueNode() ? jsonValue.asText() : jsonValue.toString()) : jsonValue;
                }
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    private static boolean isPathVariable(String varName) {
        return varName.contains("/");
    }

    private static List<String> extractPaths(String varName) {
        return Arrays.asList(varName.split("/", 2));
    }
}
