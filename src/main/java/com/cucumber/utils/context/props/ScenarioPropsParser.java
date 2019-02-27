package com.cucumber.utils.context.props;

import com.cucumber.utils.engineering.placeholders.ScenarioPropertiesParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ScenarioPropsParser {

    private String target;
    private ScenarioProps scenarioProps = ScenarioProps.getScenarioProps();

    public ScenarioPropsParser(String target) {
        this.target = target;
    }

    public Object result() {
        String standaloneSymbol = getStandaloneScenarioPlaceholder();
        if (standaloneSymbol != null) {
            Object val = scenarioProps.get(standaloneSymbol);
            return val != null ? val : target;
        }
        return getParsedStringWithScenarioValues(target);
    }

    private String getParsedStringWithScenarioValues(String str) {
        ScenarioPropertiesParser parser = new ScenarioPropertiesParser(str);
        Set<String> propertyNames = parser.getPropertyNames();
        Map<String, String> values = new HashMap<>();
        propertyNames.forEach((String name) -> {
            Object val = scenarioProps.get(name);
            if (val != null) {
                values.put(name, val.toString());
            }
        });
        return parser.parse(values);
    }

    private String getStandaloneScenarioPlaceholder() {
        ScenarioPropertiesParser parser = new ScenarioPropertiesParser(target);
        Set<String> propertyNames = parser.getPropertyNames();
        if (!propertyNames.isEmpty()) {
            String placeholder = propertyNames.iterator().next();
            if ((ScenarioPropertiesParser.PLACEHOLDER_START + placeholder + ScenarioPropertiesParser.PLACEHOLDER_END)
                    .equals(target)) {
                return placeholder;
            }
        }
        return null;
    }
}
