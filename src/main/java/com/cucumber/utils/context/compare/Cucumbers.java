package com.cucumber.utils.context.compare;

import com.cucumber.utils.context.compare.wrappers.HttpResponseWrapper;
import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.context.props.ScenarioPropsParser;
import com.cucumber.utils.engineering.compare.Compare;
import com.cucumber.utils.engineering.poller.MethodPoller;
import com.cucumber.utils.engineering.utils.ResourceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

import static com.cucumber.utils.context.props.ScenarioProps.FileExtension.*;

public class Cucumbers {

    private static Logger log = LogManager.getLogger();

    private Cucumbers() {
    }

    public static String read(String relativeFilePath) {
        try {
            return new ScenarioPropsParser(ResourceUtils.read(relativeFilePath)).result().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadScenarioPropsFromFile(String relativeFilePath) {
        ScenarioProps scenarioProps = ScenarioProps.getScenarioProps();
        if (relativeFilePath.endsWith(PROPERTIES.toString())) {
            loadPropsFromPropertiesFile(scenarioProps, relativeFilePath);
        } else if (relativeFilePath.endsWith(YAML.toString()) || relativeFilePath.endsWith(YML.toString())) {
            loadPropsFromYamlFile(scenarioProps, relativeFilePath);
        } else if (relativeFilePath.endsWith(PROPERTY.toString())) {
            loadScenarioPropertyFile(scenarioProps, relativeFilePath);
        } else {
            throw new RuntimeException("File type not supported for reading scenario properties");
        }
    }

    /**
     * Loads scenario properties from all supported file patterns: .properties, .yaml, .property
     */

    public static void loadScenarioPropsFromDir(String relativeDirPath) {
        ScenarioProps scenarioProps = ScenarioProps.getScenarioProps();
        loadScenarioPropsFromDir(scenarioProps, relativeDirPath);
    }

    public static void compare(Object expected, Object actual) {
        compare(null, expected, actual, false, false);
    }

    public static void compare(String message, Object expected, Object actual) {
        compare(message, expected, actual, false, false);
    }

    public static void compare(Object expected, Object actual, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        compare(null, expected, actual, nonExtensibleObject, nonExtensibleArray);
    }

    public static void compare(String message, Object expected, Object actual, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        try {
            compareHttpResponse(message, expected, actual, nonExtensibleObject, nonExtensibleArray);
            return;
        } catch (IOException e) {
        }
        compareInternal(message, expected, actual, nonExtensibleObject, nonExtensibleArray);
    }

    public static void pollAndCompare(String message, Object expected, Supplier<Object> supplier, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        pollAndCompare(message, expected, null, null, supplier, nonExtensibleObject, nonExtensibleArray);
    }

    public static void pollAndCompare(Object expected, Supplier<Object> supplier, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        pollAndCompare(null, expected, null, null, supplier, nonExtensibleObject, nonExtensibleArray);
    }

    public static void pollAndCompare(String message, Object expected, Supplier<Object> supplier) {
        pollAndCompare(message, expected, null, null, supplier);
    }

    public static void pollAndCompare(Object expected, Supplier<Object> supplier) {
        pollAndCompare(null, expected, null, null, supplier);
    }

    public static void pollAndCompare(String message, Object expected, int pollDurationInSeconds, Supplier<Object> supplier, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        pollAndCompare(message, expected, pollDurationInSeconds, null, supplier, nonExtensibleObject, nonExtensibleArray);
    }

    public static void pollAndCompare(Object expected, int pollDurationInSeconds, Supplier<Object> supplier, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        pollAndCompare(null, expected, pollDurationInSeconds, null, supplier, nonExtensibleObject, nonExtensibleArray);
    }

    public static void pollAndCompare(String message, Object expected, int pollDurationInSeconds, Supplier<Object> supplier) {
        pollAndCompare(message, expected, pollDurationInSeconds, null, supplier);
    }

    public static void pollAndCompare(Object expected, int pollDurationInSeconds, Supplier<Object> supplier) {
        pollAndCompare(null, expected, pollDurationInSeconds, null, supplier);
    }

    public static void pollAndCompare(String message, Object expected, long pollIntervalInMillis, Supplier<Object> supplier, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        pollAndCompare(message, expected, null, pollIntervalInMillis, supplier, nonExtensibleObject, nonExtensibleArray);
    }

    public static void pollAndCompare(Object expected, long pollIntervalInMillis, Supplier<Object> supplier, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        pollAndCompare(null, expected, null, pollIntervalInMillis, supplier, nonExtensibleObject, nonExtensibleArray);
    }

    public static void pollAndCompare(String message, Object expected, long pollIntervalInMillis, Supplier<Object> supplier) {
        pollAndCompare(message, expected, null, pollIntervalInMillis, supplier);
    }

    public static void pollAndCompare(Object expected, long pollIntervalInMillis, Supplier<Object> supplier) {
        pollAndCompare(null, expected, null, pollIntervalInMillis, supplier);
    }

    public static void pollAndCompare(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Supplier<Object> supplier) {
        pollAndCompare(message, expected, pollDurationInSeconds, pollIntervalInMillis, supplier, false, false);
    }

    public static void pollAndCompare(Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Supplier<Object> supplier) {
        pollAndCompare(null, expected, pollDurationInSeconds, pollIntervalInMillis, supplier, false, false);
    }

    public static void pollAndCompare(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Supplier<Object> supplier, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        Object result = new MethodPoller<>()
                .duration(pollDurationInSeconds, pollIntervalInMillis)
                .method(supplier)
                .until(p -> {
                    try {
                        compare(message, expected, p);
                        return true;
                    } catch (AssertionError e) {
                        return false;
                    }
                }).poll();
        compare(message, expected, result, nonExtensibleObject, nonExtensibleArray);
    }

    private static void compareHttpResponse(String message, Object expected, Object actual, boolean nonExtensibleObject, boolean nonExtensibleArray) throws IOException {
        HttpResponseWrapper actualWrapper = new HttpResponseWrapper(actual);
        HttpResponseWrapper expectedWrapper;
        try {
            expectedWrapper = new HttpResponseWrapper(expected);
        } catch (IOException e) {
            log.warn("Expected value has no HTTP Response format\n{}", expected);
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

    private static void compareInternal(String message, Object expected, Object actual, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        Map<String, String> placeholdersAndValues = new Compare(message, expected, actual, nonExtensibleObject, nonExtensibleArray).compare();
        ScenarioProps scenarioProps = ScenarioProps.getScenarioProps();
        placeholdersAndValues.forEach(scenarioProps::put);
    }

    private static void loadPropsFromPropertiesFile(ScenarioProps scenarioProps, String filePath) {
        Properties p = ResourceUtils.readProps(filePath);
        p.forEach((k, v) -> scenarioProps.put(k.toString(), v.toString().trim()));
    }

    private static void loadPropsFromYamlFile(ScenarioProps scenarioProps, String filePath) {
        Map<String, Object> map;
        try {
            map = ResourceUtils.readYaml(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        map.forEach((k, v) -> scenarioProps.put(k, v));
    }

    private static void loadScenarioPropertyFile(ScenarioProps scenarioProps, String relativeFilePath) {
        try {
            String fileName = ResourceUtils.getFileName(relativeFilePath);
            if (!fileName.endsWith(PROPERTY.toString())) {
                throw new RuntimeException("Invalid file extension: " + relativeFilePath + " .Must use \"" + PROPERTY + "\" extension");
            }
            String value = ResourceUtils.read(relativeFilePath);
            scenarioProps.put(fileName.substring(0, fileName.lastIndexOf(".")), value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadScenarioPropsFromDir(ScenarioProps scenarioProps, String relativeDirPath) {
        try {
            Map<String, String> map = ResourceUtils.readDirectory(relativeDirPath, ScenarioProps.FileExtension.stringValues());
            map.forEach((k, v) -> {
                if (k.endsWith(PROPERTIES.toString())) {
                    loadPropsFromPropertiesFile(scenarioProps, k);
                }
                if (k.endsWith(YAML.toString()) || k.endsWith(YML.toString())) {
                    loadPropsFromYamlFile(scenarioProps, k);
                } else if (k.endsWith(PROPERTY.toString())) {
                    loadScenarioPropertyFile(scenarioProps, k);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
