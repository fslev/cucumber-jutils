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
        compare(expected, actual, false, false);
    }

    public static void compare(Object expected, Object actual, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        try {
            compareHttpResponse(expected, actual, nonExtensibleObject, nonExtensibleArray);
            return;
        } catch (IOException e) {
        }
        compareInternal(expected, actual, nonExtensibleObject, nonExtensibleArray);
    }

    public static void pollAndCompare(Object expected, Supplier<Object> supplier, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        pollAndCompare(expected, null, null, supplier, nonExtensibleObject, nonExtensibleArray);
    }

    public static void pollAndCompare(Object expected, Supplier<Object> supplier) {
        pollAndCompare(expected, null, null, supplier);
    }

    public static void pollAndCompare(Object expected, int pollDurationInSeconds, Supplier<Object> supplier, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        pollAndCompare(expected, pollDurationInSeconds, null, supplier, nonExtensibleObject, nonExtensibleArray);
    }

    public static void pollAndCompare(Object expected, int pollDurationInSeconds, Supplier<Object> supplier) {
        pollAndCompare(expected, pollDurationInSeconds, null, supplier);
    }

    public static void pollAndCompare(Object expected, long pollIntervalInMillis, Supplier<Object> supplier, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        pollAndCompare(expected, null, pollIntervalInMillis, supplier, nonExtensibleObject, nonExtensibleArray);
    }

    public static void pollAndCompare(Object expected, long pollIntervalInMillis, Supplier<Object> supplier) {
        pollAndCompare(expected, null, pollIntervalInMillis, supplier);
    }

    public static void pollAndCompare(Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Supplier<Object> supplier) {
        pollAndCompare(expected, pollDurationInSeconds, pollIntervalInMillis, supplier, false, false);
    }

    public static void pollAndCompare(Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Supplier<Object> supplier, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        Object result = new MethodPoller<>()
                .duration(pollDurationInSeconds, pollIntervalInMillis)
                .method(supplier)
                .until(p -> {
                    try {
                        compare(expected, p);
                        return true;
                    } catch (AssertionError e) {
                        return false;
                    }
                }).poll();
        compare(expected, result, nonExtensibleObject, nonExtensibleArray);
    }

    private static void compareHttpResponse(Object expected, Object actual, boolean nonExtensibleObject, boolean nonExtensibleArray) throws IOException {
        HttpResponseAdapter actualAdapter = new HttpResponseAdapter(actual);
        HttpResponseAdapter expectedAdapter = new HttpResponseAdapter(expected);
        Integer expectedStatus = expectedAdapter.getStatus();
        String expectedReason = expectedAdapter.getReasonPhrase();
        Map<String, String> expectedHeaders = expectedAdapter.getHeaders();
        Object expectedEntity = expectedAdapter.getEntity();
        if (expectedStatus != null) {
            compareInternal(expectedStatus, actualAdapter.getStatus(), nonExtensibleObject, nonExtensibleArray);
        }
        if (expectedReason != null) {
            compareInternal(expectedReason, actualAdapter.getReasonPhrase(), nonExtensibleObject, nonExtensibleArray);
        }
        if (expectedHeaders != null) {
            compareInternal(expectedHeaders, actualAdapter.getHeaders(), nonExtensibleObject, nonExtensibleArray);
        }
        if (expectedEntity != null) {
            compareInternal(expectedEntity, actualAdapter.getEntity(), nonExtensibleObject, nonExtensibleArray);
        }
    }

    private static void compareInternal(Object expected, Object actual, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        Map<String, String> placeholdersAndValues = new Compare(expected, actual, nonExtensibleObject, nonExtensibleArray).compare();
        ScenarioProps scenarioProps = getScenarioProps();
        placeholdersAndValues.forEach(scenarioProps::put);
    }

    private static ScenarioProps getScenarioProps() {
        return CustomInjectorSource.getContextInjector().getInstance(ScenarioProps.class);
    }
}
