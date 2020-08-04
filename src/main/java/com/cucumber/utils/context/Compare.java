package com.cucumber.utils.context;

import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.engineering.match.HttpResponseMatcher;
import com.cucumber.utils.engineering.match.ObjectMatcher;
import com.cucumber.utils.engineering.match.condition.MatchCondition;
import com.cucumber.utils.engineering.poller.MethodPoller;
import com.cucumber.utils.exceptions.InvalidTypeException;
import org.apache.http.HttpResponse;

import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

final class Compare {
    private final ScenarioProps scenarioProps;

    Compare(ScenarioProps scenarioProps) {
        this.scenarioProps = scenarioProps;
    }

    void compare(String message, Object expected, Object actual, MatchCondition... matchConditions) {
        scenarioProps.putAll(ObjectMatcher.match(message, expected, actual, new HashSet<>(Arrays.asList(matchConditions))));
    }

    void pollAndCompare(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Double exponentialBackOff,
                        Supplier<Object> supplier, MatchCondition... matchConditions) {
        AtomicReference<AssertionError> error = new AtomicReference<>();
        new MethodPoller<>()
                .duration(pollDurationInSeconds, pollIntervalInMillis)
                .exponentialBackOff(exponentialBackOff)
                .method(supplier)
                .until(p -> {
                    try {
                        compare(message, expected, p, matchConditions);
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

    <T extends HttpResponse> void compareHttpResponse(String message, Object expected, T actual, MatchCondition... matchConditions) throws InvalidTypeException {
        scenarioProps.putAll(new HttpResponseMatcher(message, expected, actual, new HashSet<>(Arrays.asList(matchConditions))).match());
    }

    <T extends HttpResponse> void pollAndCompareHttpResponse(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Double exponentialBackOff,
                                                             Supplier<T> supplier, MatchCondition... matchConditions) {
        AtomicReference<AssertionError> error = new AtomicReference<>();
        new MethodPoller<T>()
                .duration(pollDurationInSeconds, pollIntervalInMillis)
                .exponentialBackOff(exponentialBackOff)
                .method(supplier)
                .until(p -> {
                    try {
                        compareHttpResponse(message, expected, p, matchConditions);
                        error.set(null);
                        return true;
                    } catch (AssertionError e) {
                        error.set(e);
                        return false;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).poll();
        if (error.get() != null) {
            throw error.get();
        }
    }
}