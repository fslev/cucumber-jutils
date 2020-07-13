package com.cucumber.utils.engineering.match.comparators.json;

import com.cucumber.utils.engineering.match.StringMatcher;
import com.cucumber.utils.engineering.match.condition.MatchCondition;
import com.cucumber.utils.exceptions.InvalidTypeException;
import org.apache.commons.text.StringEscapeUtils;
import ro.skyah.comparator.JsonComparator;

import java.util.*;
import java.util.stream.Collectors;

public class CustomJsonComparator implements JsonComparator {

    private final Map<String, Object> valueProperties = new HashMap<>();
    private final Map<String, Object> fieldProperties = new HashMap<>();
    private final List<Map<String, Object>> depletedFieldPropertyList = new ArrayList<>();
    private final Set<MatchCondition> matchConditions;

    public CustomJsonComparator(Set<MatchCondition> matchConditions) {
        this.matchConditions = matchConditions;
    }

    public boolean compareValues(Object expected, Object actual) {
        try {
            this.valueProperties.putAll(escapeJsonPropertyValues(new StringMatcher(null, expected, actual, matchConditions).match()));
            return true;
        } catch (AssertionError | InvalidTypeException e) {
            return false;
        }
    }

    public boolean compareFields(String expected, String actual) {
        try {
            Map<String, Object> properties = new StringMatcher(null, expected, actual, matchConditions).match();
            if (!properties.isEmpty()) {
                if (areFieldPropertiesDepleted(properties)
                        || generatedFieldPropertiesContainAllKeysWithDifferentValues(properties)) {
                    return false;
                }
                this.valueProperties.putAll(properties);
                this.fieldProperties.putAll(properties);
            }
            return true;
        } catch (AssertionError | InvalidTypeException e) {
            return false;
        }
    }

    public boolean generatedFieldPropertiesContainAllKeysWithDifferentValues(Map<String, Object> target) {
        for (Map.Entry<String, Object> entry : target.entrySet()) {
            if (fieldProperties.get(entry.getKey()) == null || fieldProperties.get(entry.getKey()).equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    public boolean areFieldPropertiesDepleted(Map<String, Object> target) {
        for (Map<String, Object> depletedFieldProperty : depletedFieldPropertyList) {
            for (Map.Entry<String, Object> targetEntry : target.entrySet()) {
                if (depletedFieldProperty.get(targetEntry.getKey()).equals(targetEntry.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Map<String, Object> getFieldProperties() {
        return fieldProperties;
    }

    public List<Map<String, Object>> getDepletedFieldPropertyList() {
        return depletedFieldPropertyList;
    }

    public Map<String, Object> getValueProperties() {
        return valueProperties;
    }

    private Map<String, Object> escapeJsonPropertyValues(Map<String, Object> properties) {
        return properties.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> StringEscapeUtils.escapeJson(e.getValue().toString())));
    }
}
