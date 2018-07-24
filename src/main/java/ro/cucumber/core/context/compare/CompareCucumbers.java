package ro.cucumber.core.context.compare;

import ro.cucumber.core.context.config.CustomInjectorSource;
import ro.cucumber.core.context.props.ScenarioProps;
import ro.cucumber.core.engineering.compare.Compare;

import java.util.Map;

public class CompareCucumbers {

    public static void compare(Object expected, Object actual) {
        Compare compare = new Compare(expected, actual);
        Map<String, String> assignProps = compare.compare();
        ScenarioProps props = getScenarioProps();
        assignProps.forEach((k, v) -> props.put(k, v));
    }

    private static ScenarioProps getScenarioProps() {
        return CustomInjectorSource.getContextInjector().getInstance(ScenarioProps.class);
    }
}
