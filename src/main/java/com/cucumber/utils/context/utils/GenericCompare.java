package com.cucumber.utils.context.utils;

import com.cucumber.utils.clients.http.wrappers.HttpResponseWrapper;
import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.engineering.compare.Compare;
import com.cucumber.utils.engineering.poller.MethodPoller;
import com.cucumber.utils.exceptions.NegativeMatchAssertionError;
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
                        error.set(null);
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
                        error.set(null);
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

    <T extends HttpResponse> void negativeCompareHttpResponse(String message, Object expected, T actual, boolean body, boolean status, boolean headers, boolean reason,
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
            try {
                compare(enhancedMessage, expectedStatus, actualWrapper.getStatus());
                if (status) {
                    throw new NegativeMatchAssertionError("Equal HTTP Response statuses");
                }
            } catch (AssertionError e) {
                if (!status) {
                    throw e;
                }
            }
        }
        if (expectedReason != null) {
            try {
                compare(enhancedMessage, expectedReason, actualWrapper.getReasonPhrase());
                if (reason) {
                    throw new NegativeMatchAssertionError("Equal HTTP Response reason phrases");
                }
            } catch (AssertionError e) {
                if (!reason) {
                    throw e;
                }
            }
        }
        if (expectedHeaders != null) {
            try {
                compare(enhancedMessage, expectedHeaders, actualWrapper.getHeaders());
                if (headers) {
                    throw new NegativeMatchAssertionError("Equal HTTP Response headers");
                }
            } catch (AssertionError e) {
                if (!headers) {
                    throw e;
                }
            }
        }
        if (expectedEntity != null) {
            try {
                compare(enhancedMessage, expectedEntity, actualWrapper.getEntity(),
                        jsonBodyNonExtensibleObject, jsonBodyNonExtensibleArray, jsonBodyArrayStrictOrder,
                        xmlBodyChildListLength, xmlBodyChildListSequence, xmlBodyElementNumAttributes);
                if (body) {
                    throw new NegativeMatchAssertionError("Equal HTTP Response bodies");
                }
            } catch (AssertionError e) {
                if (!body) {
                    throw e;
                }
            }
        }
    }

    <T extends HttpResponse> void negativePollAndCompareHttpResponse(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Double exponentialBackOff,
                                                                     Supplier<T> supplier, boolean body, boolean status, boolean headers, boolean reason,
                                                                     boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder,
                                                                     boolean xmlChildListLength, boolean xmlChildListSequence, boolean xmlElementNumAttributes) {
        AtomicReference<AssertionError> error = new AtomicReference<>();
        new MethodPoller<T>()
                .duration(pollDurationInSeconds, pollIntervalInMillis)
                .exponentialBackOff(exponentialBackOff)
                .method(supplier)
                .until(p -> {
                    try {
                        negativeCompareHttpResponse(message, expected, p, body, status, headers, reason,
                                jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder,
                                xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes);
                        error.set(null);
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
}
