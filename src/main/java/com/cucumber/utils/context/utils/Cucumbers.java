package com.cucumber.utils.context.utils;

import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.context.props.ScenarioPropsParser;
import com.cucumber.utils.engineering.utils.ResourceUtils;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.Set;
import java.util.function.Supplier;

@ScenarioScoped
public class Cucumbers {

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

    /**
     * @param relativeFilePath
     * @return names of loaded properties
     */
    public Set<String> loadScenarioPropsFromFile(String relativeFilePath) {
        return scenarioPropsLoader.loadScenarioPropsFromFile(relativeFilePath);
    }

    /**
     * Loads scenario properties from all supported file patterns: .properties, .yaml, .property, ...
     *
     * @return names of loaded properties
     */

    public Set<String> loadScenarioPropsFromDir(String relativeDirPath) {
        return scenarioPropsLoader.loadScenarioPropsFromDir(relativeDirPath);
    }

    public void compare(Object expected, Object actual) {
        compare(null, expected, actual);
    }

    public void compare(String message, Object expected, Object actual) {
        compare(message, expected, actual, false, false, false, false, false, false);
    }

    public void compare(Object expected, Object actual,
                        boolean jsonBodyNonExtensibleObject, boolean jsonBodyNonExtensibleArray, boolean jsonBodyArrayStrictOrder) {
        compare(null, expected, actual, jsonBodyNonExtensibleObject, jsonBodyNonExtensibleArray, jsonBodyArrayStrictOrder);
    }

    public void compare(String message, Object expected, Object actual,
                        boolean jsonBodyNonExtensibleObject, boolean jsonBodyNonExtensibleArray, boolean jsonBodyArrayStrictOrder) {
        compare(message, expected, actual, jsonBodyNonExtensibleObject, jsonBodyNonExtensibleArray, jsonBodyArrayStrictOrder, false, false, false);
    }

    /**
     * @param message                     <p>Custom message</p>
     * @param expected
     * @param actual
     * @param jsonBodyNonExtensibleObject <p>If expected and actual are JSONs or JSON convertible objects,
     *                                    then check json objects from actual include json objects from expected.</p>
     * @param jsonBodyNonExtensibleArray  <p>If expected and actual are JSONs or JSON convertible objects,
     *                                    then check json arrays from actual include json arrays from expected.</p>
     * @param jsonBodyArrayStrictOrder    <p>If expected and actual are JSONs or JSON convertible objects,
     *                                    then compare order of elements inside the json arrays.</p>
     * @param xmlBodyChildListLength      <p>If expected and actual are XMLs, then compare number of child nodes.</p>
     * @param xmlBodyChildListSequence    <p>If expected and actual are XMLs, then compare order of child nodes.</p>
     * @param xmlBodyElementNumAttributes <p>If expected and actual are XMLs, then compare number of attributes.</p>
     */
    public void compare(String message, Object expected, Object actual,
                        boolean jsonBodyNonExtensibleObject, boolean jsonBodyNonExtensibleArray, boolean jsonBodyArrayStrictOrder,
                        boolean xmlBodyChildListLength, boolean xmlBodyChildListSequence, boolean xmlBodyElementNumAttributes) {
        genericCompare.compare(message, expected, actual, jsonBodyNonExtensibleObject, jsonBodyNonExtensibleArray, jsonBodyArrayStrictOrder,
                xmlBodyChildListLength, xmlBodyChildListSequence, xmlBodyElementNumAttributes);
    }

    public void pollAndCompare(Object expected, int pollDurationInSeconds, Supplier<Object> supplier) {
        pollAndCompare(null, expected, pollDurationInSeconds, supplier);
    }

    public void pollAndCompare(String message, Object expected, int pollDurationInSeconds, Supplier<Object> supplier) {
        pollAndCompare(message, expected, pollDurationInSeconds, null, null, supplier);
    }

    public void pollAndCompare(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Double exponentialBackOff,
                               Supplier<Object> supplier) {
        pollAndCompare(message, expected, pollDurationInSeconds, pollIntervalInMillis, exponentialBackOff, supplier, false, false, false, false, false, false);
    }

    public void pollAndCompare(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Double exponentialBackOff,
                               Supplier<Object> supplier, boolean jsonBodyNonExtensibleObject, boolean jsonBodyNonExtensibleArray, boolean jsonBodyArrayStrictOrder) {
        pollAndCompare(message, expected, pollDurationInSeconds, pollIntervalInMillis, exponentialBackOff, supplier,
                jsonBodyNonExtensibleObject, jsonBodyNonExtensibleArray, jsonBodyArrayStrictOrder, false, false, false);
    }

    public void pollAndCompare(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Double exponentialBackOff,
                               Supplier<Object> supplier, boolean jsonBodyNonExtensibleObject, boolean jsonBodyNonExtensibleArray, boolean jsonBodyArrayStrictOrder,
                               boolean xmlBodyChildListLength, boolean xmlBodyChildListSequence, boolean xmlBodyElementNumAttributes) {
        genericCompare.pollAndCompare(message, expected, pollDurationInSeconds, pollIntervalInMillis, exponentialBackOff,
                supplier, jsonBodyNonExtensibleObject, jsonBodyNonExtensibleArray, jsonBodyArrayStrictOrder, xmlBodyChildListLength, xmlBodyChildListSequence, xmlBodyElementNumAttributes);
    }


    public <T extends HttpResponse> void compareHttpResponse(Object expected, T actual) {
        compareHttpResponse(null, expected, actual);
    }

    public <T extends HttpResponse> void compareHttpResponse(String message, Object expected, T actual) {
        compareHttpResponse(message, expected, actual, false, false, false);
    }

    public <T extends HttpResponse> void compareHttpResponse(String message, Object expected, T actual,
                                                             boolean jsonBodyNonExtensibleObject, boolean jsonBodyNonExtensibleArray, boolean jsonBodyArrayStrictOrder) {
        compareHttpResponse(message, expected, actual, jsonBodyNonExtensibleObject, jsonBodyNonExtensibleArray, jsonBodyArrayStrictOrder,
                false, false, false);
    }

    /**
     * Compares HttpResponse with expected
     *
     * @param expected Object convertible to HttpResponseWrapper:
     *                 <p>
     *                 {"status":int, "body": "string"|{json}, "headers":{}, reason:"string"}
     *                 </p>
     */
    public <T extends HttpResponse> void compareHttpResponse(String message, Object expected, T actual,
                                                             boolean jsonBodyNonExtensibleObject, boolean jsonBodyNonExtensibleArray, boolean jsonBodyArrayStrictOrder,
                                                             boolean xmlBodyChildListLength, boolean xmlBodyChildListSequence, boolean xmlBodyElementNumAttributes) {
        try {
            genericCompare.compareHttpResponse(message, expected, actual, jsonBodyNonExtensibleObject, jsonBodyNonExtensibleArray, jsonBodyArrayStrictOrder,
                    xmlBodyChildListLength, xmlBodyChildListSequence, xmlBodyElementNumAttributes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends HttpResponse> void negativeCompareHttpResponse(String message, Object expected, T actual, boolean body, boolean status, boolean headers, boolean reason,
                                                                     boolean jsonBodyNonExtensibleObject, boolean jsonBodyNonExtensibleArray, boolean jsonBodyArrayStrictOrder) {
        negativeCompareHttpResponse(message, expected, actual, body, status, headers, reason, jsonBodyNonExtensibleObject, jsonBodyNonExtensibleArray, jsonBodyArrayStrictOrder, false, false, false);
    }

    public <T extends HttpResponse> void negativeCompareHttpResponse(String message, Object expected, T actual, boolean body, boolean status, boolean headers, boolean reason,
                                                                     boolean jsonBodyNonExtensibleObject, boolean jsonBodyNonExtensibleArray, boolean jsonBodyArrayStrictOrder,
                                                                     boolean xmlBodyChildListLength, boolean xmlBodyChildListSequence, boolean xmlBodyElementNumAttributes) {
        try {
            genericCompare.negativeCompareHttpResponse(message, expected, actual, body, status, headers, reason,
                    jsonBodyNonExtensibleObject, jsonBodyNonExtensibleArray, jsonBodyArrayStrictOrder, xmlBodyChildListLength, xmlBodyChildListSequence, xmlBodyElementNumAttributes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends HttpResponse> void pollAndCompareHttpResponse(Object expected, Integer pollDurationInSeconds, Supplier<T> supplier) {
        pollAndCompareHttpResponse(null, expected, pollDurationInSeconds, supplier);
    }

    public <T extends HttpResponse> void pollAndCompareHttpResponse(String message, Object expected, Integer pollDurationInSeconds, Supplier<T> supplier) {
        pollAndCompareHttpResponse(message, expected, pollDurationInSeconds, null, null, supplier);
    }

    public <T extends HttpResponse> void pollAndCompareHttpResponse(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Double exponentialBackOff,
                                                                    Supplier<T> supplier) {
        pollAndCompareHttpResponse(message, expected, pollDurationInSeconds, pollIntervalInMillis, exponentialBackOff, supplier,
                false, false, false);
    }

    public <T extends HttpResponse> void pollAndCompareHttpResponse(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Double exponentialBackOff,
                                                                    Supplier<T> supplier, boolean jsonBodyNonExtensibleObject, boolean jsonBodyNonExtensibleArray, boolean jsonBodyArrayStrictOrder) {
        pollAndCompareHttpResponse(message, expected, pollDurationInSeconds, pollIntervalInMillis, exponentialBackOff, supplier, jsonBodyNonExtensibleObject, jsonBodyNonExtensibleArray, jsonBodyArrayStrictOrder,
                false, false, false);
    }

    public <T extends HttpResponse> void pollAndCompareHttpResponse(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Double exponentialBackOff,
                                                                    Supplier<T> supplier, boolean jsonBodyNonExtensibleObject, boolean jsonBodyNonExtensibleArray, boolean jsonBodyArrayStrictOrder,
                                                                    boolean xmlBodyChildListLength, boolean xmlBodyChildListSequence, boolean xmlBodyElementNumAttributes) {
        genericCompare.pollAndCompareHttpResponse(message, expected, pollDurationInSeconds, pollIntervalInMillis, exponentialBackOff,
                supplier, jsonBodyNonExtensibleObject, jsonBodyNonExtensibleArray, jsonBodyArrayStrictOrder,
                xmlBodyChildListLength, xmlBodyChildListSequence, xmlBodyElementNumAttributes);
    }

    public <T extends HttpResponse> void negativePollAndCompareHttpResponse(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Double exponentialBackOff,
                                                                            boolean body, boolean status, boolean headers, boolean reason,
                                                                            Supplier<T> supplier, boolean jsonBodyNonExtensibleObject, boolean jsonBodyNonExtensibleArray, boolean jsonBodyArrayStrictOrder) {
        negativePollAndCompareHttpResponse(message, expected, pollDurationInSeconds, pollIntervalInMillis, exponentialBackOff, supplier,
                body, status, headers, reason, jsonBodyNonExtensibleObject, jsonBodyNonExtensibleArray, jsonBodyArrayStrictOrder,
                false, false, false);
    }

    public <T extends HttpResponse> void negativePollAndCompareHttpResponse(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Double exponentialBackOff,
                                                                            Supplier<T> supplier, boolean body, boolean status, boolean headers, boolean reason,
                                                                            boolean jsonBodyNonExtensibleObject, boolean jsonBodyNonExtensibleArray, boolean jsonBodyArrayStrictOrder,
                                                                            boolean xmlBodyChildListLength, boolean xmlBodyChildListSequence, boolean xmlBodyElementNumAttributes) {
        genericCompare.negativePollAndCompareHttpResponse(message, expected, pollDurationInSeconds, pollIntervalInMillis, exponentialBackOff, supplier,
                body, status, headers, reason, jsonBodyNonExtensibleObject, jsonBodyNonExtensibleArray, jsonBodyArrayStrictOrder,
                xmlBodyChildListLength, xmlBodyChildListSequence, xmlBodyElementNumAttributes);
    }
}
