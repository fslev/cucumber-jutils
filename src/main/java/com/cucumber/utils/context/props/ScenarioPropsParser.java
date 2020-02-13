package com.cucumber.utils.context.props;

import com.cucumber.utils.engineering.placeholders.ScenarioPropertiesParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ScenarioPropsParser {

    private String target;
    private ScenarioProps scenarioProps;

    public ScenarioPropsParser(ScenarioProps scenarioProps, String target) {
        this.target = target;
        this.scenarioProps = scenarioProps;
    }

    public Object result() {
        String standalonePropertyKey = getStandaloneScenarioPropertyKey();
        if (standalonePropertyKey != null) {
            Object val = scenarioProps.get(standalonePropertyKey);
            return val != null ? val : scenarioProps.containsKey(standalonePropertyKey) ? null : target;
        }
        return getParsedStringWithScenarioPropertyValues(target);
    }

    private String getParsedStringWithScenarioPropertyValues(String str) {
        ScenarioPropertiesParser parser = new ScenarioPropertiesParser(str);
        Set<String> propertyNames = parser.getPropertyKeys();
        Map<String, String> values = new HashMap<>();
        propertyNames.forEach((String name) -> {
            Object val = scenarioProps.get(name);
            if (val != null) {
                values.put(name, val.toString());
            }
        });
        return parser.parse(values);
    }

    private String getStandaloneScenarioPropertyKey() {
        ScenarioPropertiesParser parser = new ScenarioPropertiesParser(target);
        Set<String> propertyKeys = parser.getPropertyKeys();
        if (!propertyKeys.isEmpty()) {
            String placeholder = propertyKeys.iterator().next();
            if ((ScenarioPropertiesParser.SYMBOL_START + placeholder + ScenarioPropertiesParser.SYMBOL_END)
                    .equals(target)) {
                return placeholder;
            }
        }
        return null;
    }
}
