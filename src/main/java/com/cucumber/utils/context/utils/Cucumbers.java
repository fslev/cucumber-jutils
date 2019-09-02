package com.cucumber.utils.context.utils;

import com.cucumber.utils.clients.http.wrappers.HttpResponseWrapper;
import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.context.props.ScenarioPropsParser;
import com.cucumber.utils.engineering.compare.Compare;
import com.cucumber.utils.engineering.poller.MethodPoller;
import com.cucumber.utils.engineering.utils.ResourceUtils;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
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

    public void loadScenarioPropsFromFile(String relativeFilePath) {
        if (relativeFilePath.endsWith(PROPERTIES.value())) {
            loadPropsFromPropertiesFile(relativeFilePath);
        } else if (relativeFilePath.endsWith(YAML.value()) || relativeFilePath.endsWith(YML.value())) {
            loadPropsFromYamlFile(relativeFilePath);
        } else if (Arrays.stream(propertyFileExtensions()).anyMatch(val -> relativeFilePath.endsWith(val))) {
            loadScenarioPropertyFile(relativeFilePath);
        } else {
            throw new RuntimeException("File type not supported for reading scenario properties." +
                    " Must have one of the extensions: " + Arrays.toString(allExtensions()));
        }
    }

    /**
     * Loads scenario properties from all supported file patterns: .properties, .yaml, .property
     */

    public void loadScenarioPropsFromDir(String relativeDirPath) {
        try {
            Map<String, String> map = ResourceUtils.readDirectory(relativeDirPath, ScenarioProps.FileExtension.allExtensions());
            map.forEach((k, v) -> {
                if (k.endsWith(PROPERTIES.value())) {
                    loadPropsFromPropertiesFile(k);
                }
                if (k.endsWith(YAML.value()) || k.endsWith(YML.value())) {
                    loadPropsFromYamlFile(k);
                } else if (Arrays.stream(propertyFileExtensions()).anyMatch(val -> k.endsWith(val))) {
                    loadScenarioPropertyFile(k);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            compareHttpResponse(message, expected, actual, nonExtensibleObject, nonExtensibleArray);
            return;
        } catch (IOException e) {
            log.debug("Cannot compare with HTTP response: {}. Proceed to normal comparing mechanism", e.getMessage());
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
        Object result = new MethodPoller<>()
                .duration(pollDurationInSeconds, pollIntervalInMillis)
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
        try {
            expectedWrapper = new HttpResponseWrapper(expected);
        } catch (IOException e) {
            log.info("Expected value has no HTTP Response format\n{}\nProceed to normal comparing mechanism", expected);
            throw e;
        }
        String expectedStatus = expectedWrapper.getStatus();
        String expectedReason = expectedWrapper.getReasonPhrase();
        Map<String, String> expectedHeaders = expectedWrapper.getHeaders();
        Object expectedEntity = expectedWrapper.getEntity();
        String enhancedMessage = System.lineSeparator() + "Expected:" + System.lineSeparator()
                + expectedWrapper.toString() + System.lineSeparator() + "Actual:" + System.lineSeparator()
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

    private void loadPropsFromPropertiesFile(String filePath) {
        Properties p = ResourceUtils.readProps(filePath);
        p.forEach((k, v) -> scenarioProps.put(k.toString(), v.toString().trim()));
    }

    private void loadPropsFromYamlFile(String filePath) {
        Map<String, Object> map;
        try {
            map = ResourceUtils.readYaml(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        map.forEach((k, v) -> scenarioProps.put(k, v));
    }

    private void loadScenarioPropertyFile(String relativeFilePath) {
        try {
            String fileName = ResourceUtils.getFileName(relativeFilePath);
            if (!Arrays.stream(propertyFileExtensions())
                    .anyMatch(val -> fileName.endsWith(val))) {
                throw new RuntimeException("Invalid file extension: " + relativeFilePath + " .Must use one of the following: \"" + propertyFileExtensions());
            }
            String value = ResourceUtils.read(relativeFilePath);
            scenarioProps.put(fileName.substring(0, fileName.lastIndexOf(".")), value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
