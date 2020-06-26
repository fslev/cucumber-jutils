package com.cucumber.utils.context.props.internal;


import com.cucumber.utils.engineering.utils.RegexUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ScenarioPropsGenerator {

    public static final String PREFIX = "~[";
    public static final String SUFFIX = "]";

    private static final Pattern captureGroupPattern = Pattern.compile(Pattern.quote(PREFIX) + "(.*?)" + Pattern.quote(SUFFIX),
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

    private final String source;
    private final Object target;
    private String standalonePlaceholderName;
    private final Map<String, Object> properties;

    public ScenarioPropsGenerator(String source, Object target) {
        this.source = source;
        this.target = target;
        this.properties = new HashMap<>();
    }

    public ScenarioPropsGenerator match() {
        if (source == null || source.isEmpty()) {
            return this;
        }
        standalonePlaceholderName = StringParser.captureStandalonePlaceholderName(source, PREFIX, SUFFIX, captureGroupPattern);
        if (standalonePlaceholderName != null) {
            properties.put(standalonePlaceholderName, target);
            return this;
        }
        List<String> placeholderNames = StringParser.captureValues(source, captureGroupPattern);
        if (placeholderNames.isEmpty()) {
            return this;
        }
        List<String> capturedValues = StringParser.captureValues(target.toString(), patternWithPlaceholdersAsCaptureGroups(source, placeholderNames));
        for (int i = 0; i < capturedValues.size(); i++) {
            if (i < placeholderNames.size()) {
                properties.put(placeholderNames.get(i), capturedValues.get(i));
            }
        }
        return this;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Object getSubstitutedSource() {
        if (source == null || source.isEmpty() || properties.isEmpty()) {
            return source;
        }
        return StringParser.replacePlaceholders(source, PREFIX, SUFFIX, captureGroupPattern, properties::get, properties::containsKey);
    }

    public String getSourceWithQuotedSubstitutes() {
        if (source == null || source.isEmpty() || standalonePlaceholderName != null || properties.isEmpty()) {
            return source;
        }
        return StringParser.replacePlaceholders(source, PREFIX, SUFFIX, captureGroupPattern,
                k -> {
                    Object val = properties.get(k);
                    return val != null ? Pattern.quote(val.toString()) : null;
                }, properties::containsKey).toString();
    }

    public String getStandalonePlaceholderName() {
        return standalonePlaceholderName;
    }

    private static Pattern patternWithPlaceholdersAsCaptureGroups(String source, List<String> placeholderNames) {
        String s = source;
        boolean validSourceRegex = RegexUtils.isRegex(source);
        for (String key : placeholderNames) {
            s = s.replace(PREFIX + key + SUFFIX, validSourceRegex ? "(.*)" : "\\E(.*)\\Q");
        }
        return Pattern.compile(validSourceRegex ? s : "\\Q" + s + "\\E", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
    }
}
