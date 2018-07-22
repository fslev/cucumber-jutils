package ro.cucumber.core.context.comparator;

import ro.cucumber.core.context.config.CustomInjectorSource;
import ro.cucumber.core.context.props.ScenarioProps;
import ro.cucumber.core.engineering.matchers.Matcher;

import java.util.Map;

public class Comparator {

    public static void compare(Object expected, Object actual) {
        Matcher matcher = new Matcher(expected, actual);
        Map<String, String> assignProps = matcher.match();
        ScenarioProps props = getScenarioProps();
        assignProps.forEach((k, v) -> props.put(k, v));
    }

    private static ScenarioProps getScenarioProps() {
        return CustomInjectorSource.getContextInjector().getInstance(ScenarioProps.class);
    }
}
