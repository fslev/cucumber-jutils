package com.cucumber.utils.context.utils;

import com.cucumber.utils.clients.http.wrappers.HttpResponseWrapper;
import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.context.props.ScenarioPropsParser;
import com.cucumber.utils.engineering.compare.Compare;
import com.cucumber.utils.engineering.poller.MethodPoller;
import com.cucumber.utils.engineering.utils.ResourceUtils;
import com.cucumber.utils.exceptions.InvalidScenarioPropertyFileType;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Supplier;

import static com.cucumber.utils.context.props.ScenarioProps.FileExtension.*;

@ScenarioScoped
public class Cucumbers {

    private Logger log = LogManager.getLogger();

    private ScenarioProps scenarioProps;

    @Inject
    private Cucumbers(ScenarioProps scenarioProps) {
        this.scenarioProps = scenarioProps;
    }

    public String read(String relativeFilePath) {
        try {
            return new ScenarioPropsParser(scenarioProps, ResourceUtils.read(relativeFilePath)).result().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<String> loadScenarioPropsFromFile(String relativeFilePath) {
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

    /**
     * Loads scenario properties from all supported file patterns: .properties, .yaml, .property, ...
     */

    public Set<String> loadScenarioPropsFromDir(String relativeDirPath) {
        Set<String> properties = new HashSet<>();
        try {
            Map<String, String> map = ResourceUtils.readDirectory(relativeDirPath, ScenarioProps.FileExtension.allExtensions());
            map.forEach((k, v) -> {
                try {
                    if (!properties.addAll(loadScenarioPropsFromFile(k))) {
                        throw new RuntimeException("\nAmbiguous loading of scenario properties from dir '" + relativeDirPath
                                + "'\nScenario properties file '" + k + "' has scenario properties or is named after a property that was already set while traversing directory.");
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

    public void compare(Object expected, Object actual) {
        compare(null, expected, actual, false, false);
    }

    public void compare(String message, Object expected, Object actual) {
        compare(message, expected, actual, false, false);
    }

    public void compare(Object expected, Object actual, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        compare(null, expected, actual, nonExtensibleObject, nonExtensibleArray);
    }

    public void compare(String message, Object expected, Object actual, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        try {
            log.debug("First, check expected and actual are in HTTP response format: {\"status\":\"...\", \"reason\":\"...\", \"body\":{},\"headers\":{}} and compare them");
            compareHttpResponse(message, expected, actual, nonExtensibleObject, nonExtensibleArray);
            return;
        } catch (IOException e) {
            log.debug("Cannot compare with HTTP response: {} ---> Proceed to normal comparing mechanism", e.getMessage());
        }
        compareInternal(message, expected, actual, nonExtensibleObject, nonExtensibleArray);
    }

    public void pollAndCompare(String message, Object expected, int pollDurationInSeconds, Supplier<Object> supplier, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        pollAndCompare(message, expected, pollDurationInSeconds, null, supplier, nonExtensibleObject, nonExtensibleArray);
    }

    public void pollAndCompare(Object expected, int pollDurationInSeconds, Supplier<Object> supplier, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        pollAndCompare(null, expected, pollDurationInSeconds, null, supplier, nonExtensibleObject, nonExtensibleArray);
    }

    public void pollAndCompare(String message, Object expected, int pollDurationInSeconds, Supplier<Object> supplier) {
        pollAndCompare(message, expected, pollDurationInSeconds, null, supplier, false, false);
    }

    public void pollAndCompare(Object expected, int pollDurationInSeconds, Supplier<Object> supplier) {
        pollAndCompare(null, expected, pollDurationInSeconds, null, supplier, false, false);
    }

    public void pollAndCompare(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Supplier<Object> supplier, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        pollAndCompare(message, expected, pollDurationInSeconds, pollIntervalInMillis, 1.0, supplier, false, false);
    }

    public void pollAndCompare(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Double exponentialBackOff, Supplier<Object> supplier, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        Object result = new MethodPoller<>()
                .duration(pollDurationInSeconds, pollIntervalInMillis)
                .exponentialBackOff(exponentialBackOff)
                .method(supplier)
                .until(p -> {
                    try {
                        compare(message, expected, p, nonExtensibleObject, nonExtensibleArray);
                        return true;
                    } catch (AssertionError e) {
                        return false;
                    }
                }).poll();
        compare(message, expected, result, nonExtensibleObject, nonExtensibleArray);
    }

    private void compareHttpResponse(String message, Object expected, Object actual, boolean nonExtensibleObject, boolean nonExtensibleArray) throws IOException {
        HttpResponseWrapper actualWrapper = new HttpResponseWrapper(actual);
        HttpResponseWrapper expectedWrapper;
        expectedWrapper = new HttpResponseWrapper(expected);
        String expectedStatus = expectedWrapper.getStatus();
        String expectedReason = expectedWrapper.getReasonPhrase();
        Map<String, String> expectedHeaders = expectedWrapper.getHeaders();
        Object expectedEntity = expectedWrapper.getEntity();
        String enhancedMessage = System.lineSeparator() + "EXPECTED:" + System.lineSeparator()
                + expectedWrapper.toString() + System.lineSeparator() + "ACTUAL:" + System.lineSeparator()
                + actualWrapper.toString() + System.lineSeparator() + (message != null ? message : "") + System.lineSeparator();
        if (expectedStatus != null) {
            compareInternal(enhancedMessage, expectedStatus, actualWrapper.getStatus(), nonExtensibleObject, nonExtensibleArray);
        }
        if (expectedReason != null) {
            compareInternal(enhancedMessage, expectedReason, actualWrapper.getReasonPhrase(), nonExtensibleObject, nonExtensibleArray);
        }
        if (expectedHeaders != null) {
            compareInternal(enhancedMessage, expectedHeaders, actualWrapper.getHeaders(), nonExtensibleObject, nonExtensibleArray);
        }
        if (expectedEntity != null) {
            compareInternal(enhancedMessage, expectedEntity, actualWrapper.getEntity(), nonExtensibleObject, nonExtensibleArray);
        }
    }

    private void compareInternal(String message, Object expected, Object actual, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        Map<String, String> placeholdersAndValues = new Compare(message, expected, actual, nonExtensibleObject, nonExtensibleArray).compare();
        placeholdersAndValues.forEach(scenarioProps::put);
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

    private String extractSimpleName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf(".")).trim();
    }
}
