package com.cucumber.utils.context.vars;

import com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.guice.ScenarioScoped;
import io.jtest.utils.common.JsonUtils;
import io.jtest.utils.common.StringParser;
import org.apache.commons.lang3.tuple.Pair;
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
        Optional<String> rootPath = rootPath(varName);
        if (rootPath.isPresent()) {
            Object rootValue = vars.get(rootPath.get());
            try {
                JsonNode rootJsonValue = JsonUtils.toJson(rootValue);
                String relativePath = varName.substring(rootPath.get().length());
                JsonNode jsonValue = rootJsonValue.at(jsonPtrExpr(relativePath));
                if (!jsonValue.isMissingNode()) {
                    return rootValue instanceof String ? (jsonValue.isValueNode() ? jsonValue.asText() : jsonValue.toString()) : jsonValue;
                }
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    private Optional<String> rootPath(String varName) {
        List<String> paths = extractPaths(varName);
        StringBuilder rootPath = new StringBuilder();
        for (String path : paths) {
            Optional<Pair<String, String>> indexPath = indexPath(path);
            if (indexPath.isPresent()) {
                rootPath.append(indexPath.get().getLeft());
                return vars.containsKey(rootPath.toString()) ? Optional.of(rootPath.toString()) : Optional.empty();
            }
            rootPath.append(path);
            if (vars.containsKey(rootPath.toString())) {
                return Optional.of(rootPath.toString());
            }
        }
        return Optional.empty();
    }

    private boolean isPathVariable(String varName) {
        return (varName.contains(".") || (varName.contains("[") && varName.endsWith("]"))) && !vars.containsKey(varName);
    }

    private static List<String> extractPaths(String varName) {
        return Arrays.asList(varName.split("\\."));
    }

    private static String jsonPtrExpr(String path) {
        List<String> paths = extractPaths(path);
        StringBuilder expr = new StringBuilder();
        for (String p : paths) {
            Optional<Pair<String, String>> indexPath = indexPath(p);
            if (indexPath.isPresent()) {
                expr.append("/").append(indexPath.get().getLeft())
                        .append(!indexPath.get().getLeft().isEmpty() ? "/" : "").append(indexPath.get().getRight());
            } else {
                expr.append(p.isEmpty() && expr.toString().isEmpty() ? "" : "/").append(p);
            }
        }
        return expr.toString();
    }

    private static Optional<Pair<String, String>> indexPath(String path) {
        List<String> result = StringParser.captureValues(path, Pattern.compile("(.*?)\\[([0-9]+?)]$"));
        if (result.size() == 2) {
            return Optional.of(Pair.of(result.get(0), result.get(1)));
        }
        return Optional.empty();
    }
}
