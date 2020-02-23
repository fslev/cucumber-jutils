package com.cucumber.utils.context.utils;

import com.cucumber.utils.clients.http.wrappers.HttpResponseWrapper;
import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.engineering.compare.Compare;
import com.cucumber.utils.engineering.poller.MethodPoller;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

final class GenericCompare {
    private ScenarioProps scenarioProps;

    GenericCompare(ScenarioProps scenarioProps) {
        this.scenarioProps = scenarioProps;
    }

    void compare(String message, Object expected, Object actual) {
        Map<String, String> placeholdersAndValues = new Compare(message, expected, actual,
                false, false, false, false, false, false).compare();
        placeholdersAndValues.forEach(scenarioProps::put);
    }

    void compare(String message, Object expected, Object actual,
                 boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder,
                 boolean xmlChildListLength, boolean xmlChildListSequence, boolean xmlElementNumAttributes) {
        Map<String, String> placeholdersAndValues = new Compare(message, expected, actual,
                jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder,
                xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes).compare();
        placeholdersAndValues.forEach(scenarioProps::put);
    }

    void pollAndCompare(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Double exponentialBackOff,
                        Supplier<Object> supplier, boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder,
                        boolean xmlChildListLength, boolean xmlChildListSequence, boolean xmlElementNumAttributes) {
        AtomicReference<AssertionError> error = new AtomicReference<>();
        new MethodPoller<>()
                .duration(pollDurationInSeconds, pollIntervalInMillis)
                .exponentialBackOff(exponentialBackOff)
                .method(supplier)
                .until(p -> {
                    try {
                        compare(message, expected, p, jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder,
                                xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes);
                        return true;
                    } catch (AssertionError e) {
                        error.set(e);
                        return false;
                    }
                }).poll();
        if (error.get() != null) {
            throw error.get();
        }
    }

    <T extends HttpResponse> void pollAndCompareHttpResponse(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Double exponentialBackOff,
                                                             Supplier<T> supplier, boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder,
                                                             boolean xmlChildListLength, boolean xmlChildListSequence, boolean xmlElementNumAttributes) {
        AtomicReference<AssertionError> error = new AtomicReference<>();
        new MethodPoller<T>()
                .duration(pollDurationInSeconds, pollIntervalInMillis)
                .exponentialBackOff(exponentialBackOff)
                .method(supplier)
                .until(p -> {
                    try {
                        compareHttpResponse(message, expected, p, jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder,
                                xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes);
                        return true;
                    } catch (AssertionError e) {
                        error.set(e);
                        return false;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).poll();
        if (error.get() != null) {
            throw error.get();
        }
    }

    <T extends HttpResponse> void compareHttpResponse(String message, Object expected, T actual,
                                                      boolean jsonBodyNonExtensibleObject, boolean jsonBodyNonExtensibleArray, boolean jsonBodyArrayStrictOrder,
                                                      boolean xmlBodyChildListLength, boolean xmlBodyChildListSequence, boolean xmlBodyElementNumAttributes) throws IOException {
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
            compare(enhancedMessage, expectedStatus, actualWrapper.getStatus());
        }
        if (expectedReason != null) {
            compare(enhancedMessage, expectedReason, actualWrapper.getReasonPhrase());
        }
        if (expectedHeaders != null) {
            compare(enhancedMessage, expectedHeaders, actualWrapper.getHeaders());
        }
        if (expectedEntity != null) {
            compare(enhancedMessage, expectedEntity, actualWrapper.getEntity(),
                    jsonBodyNonExtensibleObject, jsonBodyNonExtensibleArray, jsonBodyArrayStrictOrder,
                    xmlBodyChildListLength, xmlBodyChildListSequence, xmlBodyElementNumAttributes);
        }
    }
}
