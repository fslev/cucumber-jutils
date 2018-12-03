package ro.cucumber.core.context.compare;

import ro.cucumber.core.context.config.CustomInjectorSource;
import ro.cucumber.core.context.props.ScenarioProps;
import ro.cucumber.core.engineering.compare.Compare;
import ro.cucumber.core.engineering.poller.MethodPoller;
import java.util.Map;
import java.util.function.Supplier;

public class Cucumbers {

    private Cucumbers() {
    }

    public static void compare(Object expected, Object actual) {
        Map<String, String> placeholdersAndValues = new Compare(expected, actual).compare();
        ScenarioProps scenarioProps = getScenarioProps();
        placeholdersAndValues.forEach(scenarioProps::put);
    }

    public static void pollAndCompare(Object expected, Supplier<Object> supplier) {
        pollAndCompare(expected, null, null, supplier);
    }

    public static void pollAndCompare(Object expected, int pollDurationInSeconds, Supplier<Object> supplier) {
        pollAndCompare(expected, pollDurationInSeconds, null, supplier);
    }

    public static void pollAndCompare(Object expected, long pollIntervalInMillis, Supplier<Object> supplier) {
        pollAndCompare(expected, null, pollIntervalInMillis, supplier);
    }

    public static void pollAndCompare(Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Supplier<Object> supplier) {
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
        compare(expected, result);
    }

    private static ScenarioProps getScenarioProps() {
        return CustomInjectorSource.getContextInjector().getInstance(ScenarioProps.class);
    }
}
