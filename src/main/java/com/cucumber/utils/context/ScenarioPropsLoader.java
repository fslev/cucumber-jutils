package com.cucumber.utils.context;

import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.exceptions.InvalidScenarioPropertyFileType;
import com.cucumber.utils.helper.ResourceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static com.cucumber.utils.context.props.ScenarioProps.FileExtension.*;

final class ScenarioPropsLoader {

    private static final Logger log = LogManager.getLogger();

    static Set<String> loadScenarioPropsFromDir(String dirPath, ScenarioProps scenarioProps) {
        Set<String> properties = new HashSet<>();
        try {
            Set<String> filePaths = ResourceUtils.getFilesFromDir(dirPath, ScenarioProps.FileExtension.allExtensions());
            filePaths.forEach(filePath -> {
                try {
                    if (!properties.addAll(loadScenarioPropsFromFile(filePath, scenarioProps))) {
                        throw new RuntimeException("\nAmbiguous loading of scenario properties from dir '" + dirPath
                                + "'\nScenario properties file '" + filePath + "' has scenario properties or is named after a property that was already set while traversing directory.");
                    }
                } catch (InvalidScenarioPropertyFileType e) {
                    log.warn(e.getMessage());
                }
            });
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        log.info("Loaded from dir '{}', scenario properties with the following names:\n{}", dirPath, properties);
        return properties;
    }

    static Set<String> loadScenarioPropsFromFile(String filePath, ScenarioProps scenarioProps) {
        if (filePath.endsWith(PROPERTIES.value())) {
            return loadPropsFromPropertiesFile(filePath, scenarioProps);
        } else if (filePath.endsWith(YAML.value()) || filePath.endsWith(YML.value())) {
            return loadPropsFromYamlFile(filePath, scenarioProps);
        } else if (Arrays.stream(propertyFileExtensions()).anyMatch(filePath::endsWith)) {
            return new HashSet<>(Collections.singletonList(loadScenarioPropertyFile(filePath, scenarioProps, null)));
        } else {
            throw new InvalidScenarioPropertyFileType();
        }
    }

    private static Set<String> loadPropsFromPropertiesFile(String filePath, ScenarioProps scenarioProps) {
        Properties p = ResourceUtils.readProps(filePath);
        p.forEach((k, v) -> scenarioProps.put(k.toString(), v.toString().trim()));
        log.debug("-> Loaded scenario properties from file {}", filePath);
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
        log.debug("-> Loaded scenario properties from file '{}'", filePath);
        return map.keySet();
    }

    static String loadScenarioPropertyFile(String filePath, ScenarioProps scenarioProps, String propertyName) {
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
            log.debug("-> Loaded file '{}' into a scenario property", filePath);
            return key;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String extractSimpleName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf(".")).trim();
    }
}
