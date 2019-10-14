package com.cucumber.utils.engineering.compare.comparators;

import com.cucumber.utils.engineering.placeholders.ScenarioPropertiesGenerator;
import org.apache.commons.text.StringEscapeUtils;
import ro.skyah.comparator.JsonComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

public class CustomJsonComparator implements JsonComparator {

    private Map<String, String> generatedProperties = new HashMap<>();
    private Map<String, String> generatedFieldProperties = new HashMap<>();
    private List<Map<String, String>> depletedFieldPropertyList = new ArrayList<>();

    public boolean compareValues(Object expected, Object actual) {
        String actualString = actual.toString();
        ScenarioPropertiesGenerator generator = new ScenarioPropertiesGenerator(expected.toString(), actualString);

        boolean hasPropertiesToGenerate = !generator.getProperties().isEmpty();
        String parsedExpected = hasPropertiesToGenerate ? generator.getParsedTarget() : expected.toString();
        String parsedExpectedQuoted = hasPropertiesToGenerate ? generator.getParsedTarget(true) : expected.toString();

        try {
            Pattern pattern = Pattern.compile(parsedExpectedQuoted);
            if (pattern.matcher(actualString).matches()) {
                if (hasPropertiesToGenerate) {
                    this.generatedProperties.putAll(escapeJsonPropertyValues(generator.getProperties()));
                }
                return true;
            } else {
                return false;
            }
        } catch (PatternSyntaxException e) {
            if (parsedExpected.equals(actual.toString())) {
                if (hasPropertiesToGenerate) {
                    this.generatedProperties.putAll(escapeJsonPropertyValues(generator.getProperties()));
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean compareFields(String expected, String actual) {

        ScenarioPropertiesGenerator generator = new ScenarioPropertiesGenerator(expected, actual);
        Map<String, String> fieldGeneratedProperties = generator.getProperties();
        String parsedExpected = !fieldGeneratedProperties.isEmpty() ? generator.getParsedTarget() : expected;
        String parsedExpectedQuoted = !fieldGeneratedProperties.isEmpty() ? generator.getParsedTarget(true) : expected;
        try {
            Pattern pattern = Pattern.compile(parsedExpectedQuoted);
            if (pattern.matcher(actual).matches()) {
                if (!fieldGeneratedProperties.isEmpty()) {
                    if (areFieldPropertiesDepleted(fieldGeneratedProperties)
                            || generatedFieldPropertiesContainAllKeysWithDifferentValues(fieldGeneratedProperties)) {
                        return false;
                    }
                    this.generatedProperties.putAll(fieldGeneratedProperties);
                    this.generatedFieldProperties.putAll(fieldGeneratedProperties);
                }
                return true;
            } else {
                return false;
            }
        } catch (PatternSyntaxException e) {
            if (parsedExpected.equals(actual)) {
                if (!fieldGeneratedProperties.isEmpty()) {
                    if (areFieldPropertiesDepleted(fieldGeneratedProperties)
                            || generatedFieldPropertiesContainAllKeysWithDifferentValues(fieldGeneratedProperties)) {
                        return false;
                    }
                    this.generatedProperties.putAll(fieldGeneratedProperties);
                    this.generatedFieldProperties.putAll(fieldGeneratedProperties);
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean generatedFieldPropertiesContainAllKeysWithDifferentValues(Map<String, String> target) {
        for (Map.Entry<String, String> entry : target.entrySet()) {
            if (generatedFieldProperties.get(entry.getKey()) == null || generatedFieldProperties.get(entry.getKey()).equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    public boolean areFieldPropertiesDepleted(Map<String, String> target) {
        for (Map<String, String> depletedFieldProperty : depletedFieldPropertyList) {
            for (Map.Entry<String, String> targetEntry : target.entrySet()) {
                if (depletedFieldProperty.get(targetEntry.getKey()).equals(targetEntry.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Map<String, String> getGeneratedFieldProperties() {
        return generatedFieldProperties;
    }

    public List<Map<String, String>> getDepletedFieldPropertyList() {
        return depletedFieldPropertyList;
    }

    public Map<String, String> getGeneratedProperties() {
        return generatedProperties;
    }

    private Map<String, String> escapeJsonPropertyValues(Map<String, String> properties) {
        return properties.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> StringEscapeUtils.escapeJson(e.getValue())));
    }
}
