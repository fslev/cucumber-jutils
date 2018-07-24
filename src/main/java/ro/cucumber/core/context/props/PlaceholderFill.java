package ro.cucumber.core.context.props;

import ro.cucumber.core.context.config.CustomInjectorSource;
import ro.cucumber.core.engineering.placeholder.GlobalPlaceholderFill;
import ro.cucumber.core.engineering.placeholder.ScenarioPlaceholderFill;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlaceholderFill {

    private String target;
    private ScenarioProps scenarioProps = getScenarioProps();

    public PlaceholderFill(String target) {
        this.target = target;
    }

    private static ScenarioProps getScenarioProps() {
        return CustomInjectorSource.getContextInjector().getInstance(ScenarioProps.class);
    }

    public Object getResult() {
        ScenarioPlaceholderFill scenarioFill = new ScenarioPlaceholderFill(target);
        Set<String> placeholders = scenarioFill.searchForPlaceholders();
        if (!placeholders.isEmpty()) {
            String element = placeholders.iterator().next();
            if ((ScenarioPlaceholderFill.PLACEHOLDER_START + element +
                    ScenarioPlaceholderFill.PLACEHOLDER_END).equals(target)) {
                return scenarioProps.get(target);
            }
        }
        return getFilledStringWithGlobalValues(getFilledStringWithScenarioValues(target));
    }

    private static String getFilledStringWithGlobalValues(String str) {
        GlobalPlaceholderFill globalFill = new GlobalPlaceholderFill(str);
        Set<String> placeholders = globalFill.searchForPlaceholders();
        Map<String, String> values = new HashMap();
        placeholders.forEach((String name) -> {
            String val = GlobalProps.get(name);
            if (val != null) {
                values.put(name, val);
            }
        });
        return globalFill.fill(values);
    }

    private String getFilledStringWithScenarioValues(String str) {
        ScenarioPlaceholderFill scenarioFill = new ScenarioPlaceholderFill(str);
        Set<String> placeholders = scenarioFill.searchForPlaceholders();
        Map<String, String> values = new HashMap();
        placeholders.forEach((String name) -> {
            Object val = scenarioProps.get(name);
            if (val != null) {
                values.put(name, val.toString());
            }
        });
        return scenarioFill.fill(values);
    }
}
