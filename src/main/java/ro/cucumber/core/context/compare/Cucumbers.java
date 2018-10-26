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
        Compare compare = new Compare(expected, actual);
        Map<String, String> assignValues = compare.compare();
        ScenarioProps scenarioProps = getScenarioProps();
        assignValues.forEach(scenarioProps::put);
    }

    public static void compareWhilePolling(Object expected, Supplier<Object> supplier) {
        compareWhilePolling(expected, null, null, supplier);
    }

    public static void compareWhilePolling(Object expected, int pollDurationInSeconds, Supplier<Object> supplier) {
        compareWhilePolling(expected, pollDurationInSeconds, null, supplier);
    }

    public static void compareWhilePolling(Object expected, long pollIntervalMillis, Supplier<Object> supplier) {
        compareWhilePolling(expected, null, pollIntervalMillis, supplier);
    }

    public static void compareWhilePolling(Object expected, Integer pollDurationInSeconds, Long pollIntervalMillis, Supplier<Object> supplier) {
        Object result = new MethodPoller<>()
                .duration(pollDurationInSeconds, pollIntervalMillis)
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
