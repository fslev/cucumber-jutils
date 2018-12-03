package ro.cucumber.core.context.props;

import ro.cucumber.core.context.config.CustomInjectorSource;
import ro.cucumber.core.engineering.placeholders.GlobalPlaceholderFiller;
import ro.cucumber.core.engineering.placeholders.ScenarioPlaceholderFiller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlaceholderFiller {

    private String target;
    private ScenarioProps scenarioProps = getScenarioProps();

    public PlaceholderFiller(String target) {
        this.target = target;
    }

    private static ScenarioProps getScenarioProps() {
        return CustomInjectorSource.getContextInjector().getInstance(ScenarioProps.class);
    }

    public Object fill() {
        String standaloneSymbol = getStandaloneScenarioPlaceholder();
        if (standaloneSymbol != null) {
            Object val = scenarioProps.get(standaloneSymbol);
            return val != null ? val : target;
        }
        return getFilledStringWithEnvironmentValues(getFilledStringWithScenarioValues(target));
    }

    private static String getFilledStringWithEnvironmentValues(String str) {
        GlobalPlaceholderFiller parser = new GlobalPlaceholderFiller(str);
        Set<String> symbolNames = parser.searchForPlaceholders();
        Map<String, String> values = new HashMap();
        symbolNames.forEach((String name) -> {
            String val = EnvProps.get(name);
            if (val != null) {
                values.put(name, val);
            }
        });
        return parser.fill(values);
    }

    private String getFilledStringWithScenarioValues(String str) {
        ScenarioPlaceholderFiller parser = new ScenarioPlaceholderFiller(str);
        Set<String> symbolNames = parser.searchForPlaceholders();
        Map<String, String> values = new HashMap();
        symbolNames.forEach((String name) -> {
            Object val = scenarioProps.get(name);
            if (val != null) {
                values.put(name, val.toString());
            }
        });
        return parser.fill(values);
    }

    private String getStandaloneScenarioPlaceholder() {
        ScenarioPlaceholderFiller parser = new ScenarioPlaceholderFiller(target);
        Set<String> symbolNames = parser.searchForPlaceholders();
        if (!symbolNames.isEmpty()) {
            String element = symbolNames.iterator().next();
            if ((ScenarioPlaceholderFiller.PLACEHOLDER_START + element + ScenarioPlaceholderFiller.PLACEHOLDER_END)
                    .equals(target)) {
                return element;
            }
        }
        return null;
    }
}
