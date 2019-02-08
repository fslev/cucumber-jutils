package ro.cucumber.core.context.compare;

import ro.cucumber.core.context.compare.adapters.HttpResponseAdapter;
import ro.cucumber.core.context.props.PlaceholderFiller;
import ro.cucumber.core.context.props.ScenarioProps;
import ro.cucumber.core.engineering.compare.Compare;
import ro.cucumber.core.engineering.poller.MethodPoller;
import ro.cucumber.core.engineering.utils.ResourceUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

import static ro.cucumber.core.context.props.ScenarioProps.FileExtension.*;

public class Cucumbers {

    private Cucumbers() {
    }

    public static String read(String relativeFilePath) {
        try {
            return new PlaceholderFiller(ResourceUtils.read(relativeFilePath)).fill().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadScenarioPropsFromFile(String filePath) {
        ScenarioProps scenarioProps = ScenarioProps.getScenarioProps();
        if (filePath.endsWith(PROPERTIES.toString())) {
            loadPropsFromPropertiesFile(scenarioProps, filePath);
        } else if (filePath.endsWith(YAML.toString())) {
            loadPropsFromYamlFile(scenarioProps, filePath);
        } else {
            throw new RuntimeException("File type not supported for reading scenario properties");
        }
    }

    public static void loadScenarioPropertyFile(String relativeFilePath) {
        ScenarioProps scenarioProps = ScenarioProps.getScenarioProps();
        loadScenarioPropertyFile(scenarioProps, relativeFilePath);
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
        HttpResponseAdapter actualAdapter = new HttpResponseAdapter(actual);
        HttpResponseAdapter expectedAdapter = new HttpResponseAdapter(expected);
        Integer expectedStatus = expectedAdapter.getStatus();
        String expectedReason = expectedAdapter.getReasonPhrase();
        Map<String, String> expectedHeaders = expectedAdapter.getHeaders();
        Object expectedEntity = expectedAdapter.getEntity();
        String enhancedMessage = System.lineSeparator() + "Expected:" + System.lineSeparator()
                + expectedAdapter.toString() + System.lineSeparator() + "Actual:" + System.lineSeparator()
                + actualAdapter.toString() + System.lineSeparator() + (message != null ? message : "") + System.lineSeparator();
        if (expectedStatus != null) {
            compareInternal(enhancedMessage, expectedStatus, actualAdapter.getStatus(), nonExtensibleObject, nonExtensibleArray);
        }
        if (expectedReason != null) {
            compareInternal(enhancedMessage, expectedReason, actualAdapter.getReasonPhrase(), nonExtensibleObject, nonExtensibleArray);
        }
        if (expectedHeaders != null) {
            compareInternal(enhancedMessage, expectedHeaders, actualAdapter.getHeaders(), nonExtensibleObject, nonExtensibleArray);
        }
        if (expectedEntity != null) {
            compareInternal(enhancedMessage, expectedEntity, actualAdapter.getEntity(), nonExtensibleObject, nonExtensibleArray);
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
            if (!fileName.endsWith(".property")) {
                throw new RuntimeException("Invalid file extension: " + relativeFilePath + " .Must use \".property\" extension");
            }
            String value = ResourceUtils.read(relativeFilePath);
            scenarioProps.put(fileName.substring(0, fileName.lastIndexOf(".")), value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadScenarioPropsFromDir(ScenarioProps scenarioProps, String relativeDirPath) {
        try {
            Map<String, String> map = ResourceUtils.readDirectory(relativeDirPath, PROPERTIES.toString(), YAML.toString(), PROPERTY.toString());
            map.forEach((k, v) -> {
                if (k.endsWith(PROPERTIES.toString())) {
                    loadPropsFromPropertiesFile(scenarioProps, k);
                }
                if (k.endsWith(YAML.toString())) {
                    loadPropsFromYamlFile(scenarioProps, k);
                } else if (k.endsWith(PROPERTY.toString())) {
                    loadScenarioPropertyFile(k);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
