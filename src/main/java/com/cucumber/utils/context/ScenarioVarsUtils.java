package com.cucumber.utils.context;

import com.cucumber.utils.context.vars.ScenarioVars;
import com.cucumber.utils.context.vars.ScenarioVarsParser;
import com.cucumber.utils.exceptions.InvalidScenarioVarFileType;
import io.jtest.utils.common.ResourceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static com.cucumber.utils.context.vars.ScenarioVars.FileExtension.PROPERTIES;
import static com.cucumber.utils.context.vars.ScenarioVars.FileExtension.YAML;
import static com.cucumber.utils.context.vars.ScenarioVars.FileExtension.YML;
import static com.cucumber.utils.context.vars.ScenarioVars.FileExtension.varFileExtensions;

public final class ScenarioVarsUtils {

    private ScenarioVarsUtils() {
    }

    private static final Logger LOG = LogManager.getLogger();

    /**
     * Reads file content and substitutes any scenario variables ({@code #[var]}) and SpEL expressions ({@code #{...}}) inside it.
     */
    public static String parse(String filePath, ScenarioVars scenarioVars) {
        try {
            return ScenarioVarsParser.parse(ResourceUtils.read(filePath), scenarioVars).toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Recursively scans a directory and loads each supported file as one or more scenario variables.
     *
     * @return names of loaded variables
     */
    public static Set<String> loadScenarioVarsFromDir(String dirPath, ScenarioVars scenarioVars) {
        Set<String> vars = new HashSet<>();
        try {
            Set<String> filePaths = ResourceUtils.getFilesFromDir(dirPath, ScenarioVars.FileExtension.allExtensions());
            filePaths.forEach(filePath -> {
                try {
                    if (!vars.addAll(loadScenarioVarsFromFile(filePath, scenarioVars))) {
                        throw new RuntimeException(("""

                                Ambiguous loading of scenario variables from dir '%s'
                                File '%s' contains a variable or is named after a variable that was already set while traversing directory""")
                                .formatted(dirPath, filePath));
                    }
                } catch (InvalidScenarioVarFileType e) {
                    LOG.warn(e.getMessage());
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        LOG.debug("Loaded from dir '{}', scenario variables with the following names:\n{}", dirPath, vars);
        return vars;
    }

    /**
     * Loads scenario variables from a single file. Properties and YAML files are flattened into one variable per
     * key; other supported text files become a single variable named after the file.
     *
     * @return names of loaded variables
     */
    public static Set<String> loadScenarioVarsFromFile(String filePath, ScenarioVars scenarioVars) {
        if (filePath.endsWith(PROPERTIES.value())) {
            return loadVarsFromPropertiesFile(filePath, scenarioVars);
        }
        if (filePath.endsWith(YAML.value()) || filePath.endsWith(YML.value())) {
            return loadVarsFromYamlFile(filePath, scenarioVars);
        }
        if (Arrays.stream(varFileExtensions()).anyMatch(filePath::endsWith)) {
            return Set.of(loadFileAsScenarioVariable(filePath, scenarioVars, null));
        }
        throw new InvalidScenarioVarFileType();
    }

    public static String loadFileAsScenarioVariable(String filePath, ScenarioVars scenarioVars, @Nullable String varName) {
        try {
            String fileName = ResourceUtils.getFileName(filePath);
            if (Arrays.stream(varFileExtensions()).noneMatch(fileName::endsWith)) {
                throw new RuntimeException("Invalid file extension: " + filePath +
                        " .Must use one of the following: \"" + Arrays.toString(varFileExtensions()));
            }
            String key = varName == null ? extractSimpleName(fileName) : varName;
            String value = ResourceUtils.read(filePath);
            scenarioVars.put(key, value);
            LOG.debug("-> Loaded file '{}' into a scenario variable", filePath);
            return key;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static Set<String> loadVarsFromPropertiesFile(String filePath, ScenarioVars scenarioVars) {
        Properties p;
        try {
            p = ResourceUtils.readProps(filePath);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        p.forEach((k, v) -> scenarioVars.put(k.toString(), v.toString().trim()));
        LOG.debug("-> Loaded scenario variables from file {}", filePath);
        return p.stringPropertyNames();
    }

    private static Set<String> loadVarsFromYamlFile(String filePath, ScenarioVars scenarioVars) {
        Map<String, Object> map;
        try {
            map = ResourceUtils.readYaml(filePath);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        if (map == null) {
            throw new RuntimeException("Incorrect data inside Yaml file: " + filePath);
        }
        map.forEach(scenarioVars::put);
        LOG.debug("-> Loaded scenario variables from file '{}'", filePath);
        return map.keySet();
    }

    private static String extractSimpleName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf(".")).trim();
    }
}
