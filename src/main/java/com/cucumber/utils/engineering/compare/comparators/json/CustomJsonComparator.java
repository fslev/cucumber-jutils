package com.cucumber.utils.engineering.compare.comparators.json;

import com.cucumber.utils.engineering.compare.StringRegexCompare;
import org.apache.commons.text.StringEscapeUtils;
import ro.skyah.comparator.JsonComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomJsonComparator implements JsonComparator {

    private final Map<String, Object> valueProperties = new HashMap<>();
    private final Map<String, Object> fieldProperties = new HashMap<>();
    private final List<Map<String, Object>> depletedFieldPropertyList = new ArrayList<>();

    public boolean compareValues(Object expected, Object actual) {
        try {
            this.valueProperties.putAll(escapeJsonPropertyValues(new StringRegexCompare(expected, actual).compare()));
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    public boolean compareFields(String expected, String actual) {
        try {
            Map<String, Object> properties = new StringRegexCompare(expected, actual).compare();
            if (!properties.isEmpty()) {
                if (areFieldPropertiesDepleted(properties)
                        || generatedFieldPropertiesContainAllKeysWithDifferentValues(properties)) {
                    return false;
                }
                this.valueProperties.putAll(properties);
                this.fieldProperties.putAll(properties);
            }
            return true;
        } catch (AssertionError e) {
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
