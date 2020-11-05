package com.cucumber.utils.context;

import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.context.props.ScenarioPropsParser;
import com.cucumber.utils.exceptions.InvalidScenarioPropertyFileType;
import io.jtest.utils.common.ResourceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static com.cucumber.utils.context.props.ScenarioProps.FileExtension.*;

public final class ScenarioPropsUtils {

    private static final Logger LOG = LogManager.getLogger();

    /**
     * Reads file content, parses it by any scenario properties found (...#[scenarioProperty]...) and returns it
     *
     * @param filePath
     * @param scenarioProps
     * @return
     */
    public static String parse(String filePath, ScenarioProps scenarioProps) {
        try {
            return ScenarioPropsParser.parse(ResourceUtils.read(filePath), scenarioProps).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads scenario properties from all supported file patterns: .properties, .yaml, .property, ...
     *
     * @return names of loaded properties
     */
    public static Set<String> loadPropsFromDir(String dirPath, ScenarioProps scenarioProps) {
        Set<String> properties = new HashSet<>();
        try {
            Set<String> filePaths = ResourceUtils.getFilesFromDir(dirPath, ScenarioProps.FileExtension.allExtensions());
            filePaths.forEach(filePath -> {
                try {
                    if (!properties.addAll(loadPropsFromFile(filePath, scenarioProps))) {
                        throw new RuntimeException("\nAmbiguous loading of scenario properties from dir '" + dirPath
                                + "'\nScenario properties file '" + filePath + "' has scenario properties or is named after a property that was already set while traversing directory.");
                    }
                } catch (InvalidScenarioPropertyFileType e) {
                    LOG.warn(e.getMessage());
                }
            });
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        LOG.info("Loaded from dir '{}', scenario properties with the following names:\n{}", dirPath, properties);
        return properties;
    }

    /**
     * @param filePath
     * @return names of loaded properties
     */
    public static Set<String> loadPropsFromFile(String filePath, ScenarioProps scenarioProps) {
        if (filePath.endsWith(PROPERTIES.value())) {
            return loadPropsFromPropertiesFile(filePath, scenarioProps);
        } else if (filePath.endsWith(YAML.value()) || filePath.endsWith(YML.value())) {
            return loadPropsFromYamlFile(filePath, scenarioProps);
        } else if (Arrays.stream(propertyFileExtensions()).anyMatch(filePath::endsWith)) {
            return new HashSet<>(Collections.singletonList(loadFileAsScenarioProperty(filePath, scenarioProps, null)));
        } else {
            throw new InvalidScenarioPropertyFileType();
        }
    }

    public static String loadFileAsScenarioProperty(String filePath, ScenarioProps scenarioProps, String propertyName) {
        try {
            String fileName = ResourceUtils.getFileName(filePath);
            if (Arrays.stream(propertyFileExtensions())
                    .noneMatch(fileName::endsWith)) {
                throw new RuntimeException("Invalid file extension: " + filePath +
                        " .Must use one of the following: \"" + Arrays.toString(propertyFileExtensions()));
            }
            String key = propertyName == null ? extractSimpleName(fileName) : propertyName;
            String value = ResourceUtils.read(filePath);
            scenarioProps.put(key, value);
            LOG.debug("-> Loaded file '{}' into a scenario property", filePath);
            return key;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Set<String> loadPropsFromPropertiesFile(String filePath, ScenarioProps scenarioProps) {
        Properties p = ResourceUtils.readProps(filePath);
        p.forEach((k, v) -> scenarioProps.put(k.toString(), v.toString().trim()));
        LOG.debug("-> Loaded scenario properties from file {}", filePath);
        return p.stringPropertyNames();
    }

    private static Set<String> loadPropsFromYamlFile(String filePath, ScenarioProps scenarioProps) {
        Map<String, Object> map;
        try {
            map = ResourceUtils.readYaml(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        map.forEach((k, v) -> scenarioProps.put(k, v));
        LOG.debug("-> Loaded scenario properties from file '{}'", filePath);
        return map.keySet();
    }

    private static String extractSimpleName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf(".")).trim();
    }
}
