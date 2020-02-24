package com.cucumber.utils.context.utils;

import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.engineering.utils.ResourceUtils;
import com.cucumber.utils.exceptions.InvalidScenarioPropertyFileType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static com.cucumber.utils.context.props.ScenarioProps.FileExtension.*;

final class ScenarioPropsLoader {

    private Logger log = LogManager.getLogger();
    private ScenarioProps scenarioProps;

    ScenarioPropsLoader(ScenarioProps scenarioProps) {
        this.scenarioProps = scenarioProps;
    }

    Set<String> loadScenarioPropsFromDir(String relativeDirPath) {
        Set<String> properties = new HashSet<>();
        try {
            Set<String> filePaths = ResourceUtils.getFilesFromDir(relativeDirPath, ScenarioProps.FileExtension.allExtensions());
            filePaths.forEach(filePath -> {
                try {
                    if (!properties.addAll(loadScenarioPropsFromFile(filePath))) {
                        throw new RuntimeException("\nAmbiguous loading of scenario properties from dir '" + relativeDirPath
                                + "'\nScenario properties file '" + filePath + "' has scenario properties or is named after a property that was already set while traversing directory.");
                    }
                } catch (InvalidScenarioPropertyFileType e) {
                    log.warn(e.getMessage());
                }
            });
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        log.info("Loaded from dir '{}', scenario properties with the following names:\n{}", relativeDirPath, properties);
        return properties;
    }

     Set<String> loadScenarioPropsFromFile(String relativeFilePath) {
        if (relativeFilePath.endsWith(PROPERTIES.value())) {
            return loadPropsFromPropertiesFile(relativeFilePath);
        } else if (relativeFilePath.endsWith(YAML.value()) || relativeFilePath.endsWith(YML.value())) {
            return loadPropsFromYamlFile(relativeFilePath);
        } else if (Arrays.stream(propertyFileExtensions()).anyMatch(relativeFilePath::endsWith)) {
            return new HashSet<>(Arrays.asList(loadScenarioPropertyFile(relativeFilePath)));
        } else {
            throw new InvalidScenarioPropertyFileType();
        }
    }

    private Set<String> loadPropsFromPropertiesFile(String filePath) {
        Properties p = ResourceUtils.readProps(filePath);
        p.forEach((k, v) -> scenarioProps.put(k.toString(), v.toString().trim()));
        log.debug("-> Loaded scenario properties from file {}", filePath);
        return p.stringPropertyNames();
    }

    private Set<String> loadPropsFromYamlFile(String filePath) {
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

    private String loadScenarioPropertyFile(String relativeFilePath) {
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
