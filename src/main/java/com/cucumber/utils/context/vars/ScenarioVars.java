package com.cucumber.utils.context.vars;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.guice.ScenarioScoped;
import io.json.compare.util.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

@ScenarioScoped
public class ScenarioVars {

    private static final Logger LOG = LogManager.getLogger();
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z0-9_$\\-.@]+");

    private final Map<String, Object> vars = new HashMap<>();

    public String getAsString(String name) {
        Object val = get(name);
        return val != null ? val.toString() : null;
    }

    public Object get(String name) {
        if (name == null) {
            return vars.get(null);
        }
        String trimmedName = name.trim();
        return switch (trimmedName.toLowerCase()) {
            case "uid" -> UUID.randomUUID().toString();
            case "now" -> System.currentTimeMillis();
            case "short-random" -> ThreadLocalRandom.current().nextInt(Short.MAX_VALUE);
            case "int-random" -> ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
            default -> resolve(name, trimmedName);
        };
    }

    public void put(String name, Object val) {
        String trimmedName = (name == null) ? null : name.trim();
        if (trimmedName == null || !NAME_PATTERN.matcher(trimmedName).matches()) {
            throw new RuntimeException("Invalid variable name: " + trimmedName + ". Allowed pattern: " + NAME_PATTERN);
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
        return vars.containsKey(name) || (isJsonPointerExpression(name) && getJsonPointerValue(name) != null);
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

        private static final Set<FileExtension> VAR_TYPES = EnumSet.of(JSON, XML, TXT, CSV, HTML, TEXT);
        private static final String[] ALL = toValueArray(EnumSet.allOf(FileExtension.class));
        private static final String[] VAR_ONLY = toValueArray(VAR_TYPES);

        private final String name;

        FileExtension(String name) {
            this.name = name;
        }

        public static String[] allExtensions() {
            return ALL.clone();
        }

        public static String[] varFileExtensions() {
            return VAR_ONLY.clone();
        }

        public String value() {
            return name;
        }

        private static String[] toValueArray(Set<FileExtension> set) {
            return set.stream().map(FileExtension::value).toArray(String[]::new);
        }
    }

    @Override
    public String toString() {
        return this.vars.toString();
    }

    private Object resolve(String name, String trimmedName) {
        Object value = isJsonPointerExpression(name) ? getJsonPointerValue(name) : vars.get(trimmedName);
        return value instanceof String s ? ScenarioVarsParser.parse(s, this) : value;
    }

    private Object getJsonPointerValue(String varName) {
        String[] paths = varName.split(String.valueOf(JsonPointer.SEPARATOR), 2);
        String rootPath = paths[0];
        if (!vars.containsKey(rootPath)) {
            return null;
        }
        Object rootValue = vars.get(rootPath);
        try {
            JsonNode rootJsonValue = JsonUtils.toJson(rootValue);
            JsonNode jsonValue = rootJsonValue.at(JsonPointer.SEPARATOR + paths[1]);
            if (jsonValue.isMissingNode()) {
                return null;
            }
            return rootValue instanceof String
                    ? (jsonValue.isValueNode() ? jsonValue.asText() : jsonValue.toString())
                    : jsonValue;
        } catch (IOException e) {
            return null;
        }
    }

    private static boolean isJsonPointerExpression(String varName) {
        return varName.contains(String.valueOf(JsonPointer.SEPARATOR));
    }
}
