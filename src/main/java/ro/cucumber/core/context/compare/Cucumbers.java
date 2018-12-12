package ro.cucumber.core.context.compare;

import ro.cucumber.core.context.compare.adapters.HttpResponseAdapter;
import ro.cucumber.core.context.config.CustomInjectorSource;
import ro.cucumber.core.context.props.PlaceholderFiller;
import ro.cucumber.core.context.props.ScenarioProps;
import ro.cucumber.core.engineering.compare.Compare;
import ro.cucumber.core.engineering.poller.MethodPoller;
import ro.cucumber.core.engineering.utils.ResourceUtils;
import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;

public class Cucumbers {

    private Cucumbers() {
    }

    public static String read(String relativeFilePath) {
        return new PlaceholderFiller(ResourceUtils.read(relativeFilePath)).fill().toString();
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
        if (expectedStatus != null) {
            compareInternal(message, expectedStatus, actualAdapter.getStatus(), nonExtensibleObject, nonExtensibleArray);
        }
        if (expectedReason != null) {
            compareInternal(message, expectedReason, actualAdapter.getReasonPhrase(), nonExtensibleObject, nonExtensibleArray);
        }
        if (expectedHeaders != null) {
            compareInternal(message, expectedHeaders, actualAdapter.getHeaders(), nonExtensibleObject, nonExtensibleArray);
        }
        if (expectedEntity != null) {
            compareInternal(message, expectedEntity, actualAdapter.getEntity(), nonExtensibleObject, nonExtensibleArray);
        }
    }

    private static void compareInternal(String message, Object expected, Object actual, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        Map<String, String> placeholdersAndValues = new Compare(message, expected, actual, nonExtensibleObject, nonExtensibleArray).compare();
        ScenarioProps scenarioProps = getScenarioProps();
        placeholdersAndValues.forEach(scenarioProps::put);
    }

    private static ScenarioProps getScenarioProps() {
        return CustomInjectorSource.getContextInjector().getInstance(ScenarioProps.class);
    }
}
