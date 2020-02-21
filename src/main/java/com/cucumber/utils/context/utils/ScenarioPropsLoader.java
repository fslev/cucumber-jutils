package com.cucumber.utils.context.utils;

import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.engineering.utils.ResourceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static com.cucumber.utils.context.props.ScenarioProps.FileExtension.propertyFileExtensions;

final class ScenarioPropsLoader {

    private Logger log = LogManager.getLogger();
    private ScenarioProps scenarioProps;

    ScenarioPropsLoader(ScenarioProps scenarioProps) {
        this.scenarioProps = scenarioProps;
    }

    Set<String> loadPropsFromPropertiesFile(String filePath) {
        Properties p = ResourceUtils.readProps(filePath);
        p.forEach((k, v) -> scenarioProps.put(k.toString(), v.toString().trim()));
        log.debug("-> Loaded scenario properties from file {}", filePath);
        return p.stringPropertyNames();
    }

    Set<String> loadPropsFromYamlFile(String filePath) {
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

    String loadScenarioPropertyFile(String relativeFilePath) {
        try {
            String fileName = ResourceUtils.getFileName(relativeFilePath);
            if (Arrays.stream(propertyFileExtensions())
                    .noneMatch(fileName::endsWith)) {
                throw new RuntimeException("Invalid file extension: " + relativeFilePath +
                        " .Must use one of the following: \"" + Arrays.toString(propertyFileExtensions()));
            }
            String propertyName = extractSimpleName(fileName);
            String value = ResourceUtils.read(relativeFilePath);
            scenarioProps.put(propertyName, value);
            log.debug("-> Loaded file '{}' into a scenario property", relativeFilePath);
            return propertyName;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String extractSimpleName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf(".")).trim();
    }
}
