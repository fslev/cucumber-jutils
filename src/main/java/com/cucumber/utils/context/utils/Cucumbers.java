package com.cucumber.utils.context.utils;

import com.cucumber.utils.clients.http.wrappers.HttpResponseWrapper;
import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.context.props.ScenarioPropsParser;
import com.cucumber.utils.engineering.poller.MethodPoller;
import com.cucumber.utils.engineering.utils.ResourceUtils;
import com.cucumber.utils.exceptions.InvalidScenarioPropertyFileType;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static com.cucumber.utils.context.props.ScenarioProps.FileExtension.*;

@ScenarioScoped
public class Cucumbers {

    private Logger log = LogManager.getLogger();

    private ScenarioProps scenarioProps;
    private ScenarioPropsLoader scenarioPropsLoader;
    private GenericCompare genericCompare;

    @Inject
    private Cucumbers(ScenarioProps scenarioProps) {
        this.scenarioProps = scenarioProps;
        this.scenarioPropsLoader = new ScenarioPropsLoader(scenarioProps);
        this.genericCompare = new GenericCompare(scenarioProps);
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
            return scenarioPropsLoader.loadPropsFromPropertiesFile(relativeFilePath);
        } else if (relativeFilePath.endsWith(YAML.value()) || relativeFilePath.endsWith(YML.value())) {
            return scenarioPropsLoader.loadPropsFromYamlFile(relativeFilePath);
        } else if (Arrays.stream(propertyFileExtensions()).anyMatch(relativeFilePath::endsWith)) {
            return new HashSet<>(Arrays.asList(scenarioPropsLoader.loadScenarioPropertyFile(relativeFilePath)));
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

    public void compare(Object expected, Object actual,
                        boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder) {
        compare(null, expected, actual, jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder);
    }

    public void compare(String message, Object expected, Object actual,
                        boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder) {
        compare(message, expected, actual, jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder, false, false, false);
    }

    public void compare(Object expected, Object actual) {
        compare(null, expected, actual);
    }

    public void compare(String message, Object expected, Object actual) {
        compare(message, expected, actual, false, false, false, false, false, false);
    }

    /**
     * @param message                 <p>Custom message</p>
     * @param expected
     * @param actual
     * @param jsonNonExtensibleObject <p>If expected and actual are JSONs or JSON convertible objects,
     *                                then check json objects from actual include json objects from expected.</p>
     * @param jsonNonExtensibleArray  <p>If expected and actual are JSONs or JSON convertible objects,
     *                                then check json arrays from actual include json arrays from expected.</p>
     * @param jsonArrayStrictOrder    <p>If expected and actual are JSONs or JSON convertible objects,
     *                                then compare order of elements inside the json arrays.</p>
     * @param xmlChildListLength      <p>If expected and actual are XMLs, then compare number of child nodes.</p>
     * @param xmlChildListSequence    <p>If expected and actual are XMLs, then compare order of child nodes.</p>
     * @param xmlElementNumAttributes <p>If expected and actual are XMLs, then compare number of attributes.</p>
     */
    public void compare(String message, Object expected, Object actual,
                        boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder,
                        boolean xmlChildListLength, boolean xmlChildListSequence, boolean xmlElementNumAttributes) {
        try {
            log.debug("First, check expected and actual are in HTTP response format: {\"status\":\"...\", \"reason\":\"...\", \"body\":{},\"headers\":{}} and compare them");
            compareHttpResponse(message, expected, actual, jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder,
                    xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes);
            return;
        } catch (IOException e) {
            log.debug("Cannot compare with HTTP response: {} ---> Proceed to normal comparing mechanism", e.getMessage());
        }
        genericCompare.compare(message, expected, actual, jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder,
                xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes);
    }


    public void pollAndCompare(Object expected, int pollDurationInSeconds, Supplier<Object> supplier) {
        pollAndCompare(null, expected, pollDurationInSeconds, supplier);
    }

    public void pollAndCompare(String message, Object expected, int pollDurationInSeconds, Supplier<Object> supplier) {
        pollAndCompare(message, expected, pollDurationInSeconds, null, supplier);
    }

    public void pollAndCompare(Object expected, int pollDurationInSeconds, Double exponentialBackOff, Supplier<Object> supplier) {
        pollAndCompare(null, expected, pollDurationInSeconds, exponentialBackOff, supplier);
    }

    public void pollAndCompare(Object expected, int pollDurationInSeconds, Double exponentialBackOff, Supplier<Object> supplier,
                               boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder) {
        pollAndCompare(null, expected, pollDurationInSeconds, exponentialBackOff, supplier, jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder);
    }

    public void pollAndCompare(String message, Object expected, int pollDurationInSeconds, Double exponentialBackOff, Supplier<Object> supplier,
                               boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder) {
        pollAndCompare(message, expected, pollDurationInSeconds, null, exponentialBackOff, supplier, jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder, false, false, false);
    }

    public void pollAndCompare(String message, Object expected, int pollDurationInSeconds, Double exponentialBackOff, Supplier<Object> supplier) {
        pollAndCompare(message, expected, pollDurationInSeconds, null, exponentialBackOff, supplier, false, false, false, false, false, false);
    }

    public void pollAndCompare(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Supplier<Object> supplier) {
        pollAndCompare(message, expected, pollDurationInSeconds, pollIntervalInMillis, 1.0, supplier, false, false, false, false, false, false);
    }

    public void pollAndCompare(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Double exponentialBackOff,
                               Supplier<Object> supplier) {
        pollAndCompare(message, expected, pollDurationInSeconds, pollIntervalInMillis, exponentialBackOff, supplier, false, false, false, false, false, false);
    }

    public void pollAndCompare(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Double exponentialBackOff,
                               Supplier<Object> supplier, boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder,
                               boolean xmlChildListLength, boolean xmlChildListSequence, boolean xmlElementNumAttributes) {
        Object result = new MethodPoller<>()
                .duration(pollDurationInSeconds, pollIntervalInMillis)
                .exponentialBackOff(exponentialBackOff)
                .method(supplier)
                .until(p -> {
                    try {
                        compare(message, expected, p, jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder,
                                xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes);
                        return true;
                    } catch (AssertionError e) {
                        return false;
                    }
                }).poll();
        compare(message, expected, result, jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder,
                xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes);
    }

    private void compareHttpResponse(String message, Object expected, Object actual,
                                     boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder,
                                     boolean xmlChildListLength, boolean xmlChildListSequence, boolean xmlElementNumAttributes) throws IOException {
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
            genericCompare.compare(enhancedMessage, expectedStatus, actualWrapper.getStatus(), false, false, false, false, false, false);
        }
        if (expectedReason != null) {
            genericCompare.compare(enhancedMessage, expectedReason, actualWrapper.getReasonPhrase(), false, false, false, false, false, false);
        }
        if (expectedHeaders != null) {
            genericCompare.compare(enhancedMessage, expectedHeaders, actualWrapper.getHeaders(), false, false, false, false, false, false);
        }
        if (expectedEntity != null) {
            genericCompare.compare(enhancedMessage, expectedEntity, actualWrapper.getEntity(),
                    jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder,
                    xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes);
        }
    }


}
