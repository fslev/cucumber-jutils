package ro.cucumber.core.context.compare;

import ro.cucumber.core.context.config.CustomInjectorSource;
import ro.cucumber.core.context.props.ScenarioProps;
import ro.cucumber.core.engineering.compare.Compare;
import ro.cucumber.core.engineering.poller.MethodPoller;

import java.util.Map;
import java.util.function.Supplier;

public class CompareCucumbers {

    private CompareCucumbers() {}

    public static void compare(Object expected, Object actual) {
        Compare compare = new Compare(expected, actual);
        Map<String, String> assignValues = compare.compare();
        ScenarioProps scenarioProps = getScenarioProps();
        assignValues.forEach(scenarioProps::put);
    }

    public static void compareWithPolling(Object expected, Supplier<Object> supplier) {
        Object result = new MethodPoller<>().method(supplier)
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

    public static void compareWithPolling(Object expected, int pollDurationInSeconds, Supplier<Object> supplier) {
        Object result = new MethodPoller<>()
                .duration(pollDurationInSeconds)
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
