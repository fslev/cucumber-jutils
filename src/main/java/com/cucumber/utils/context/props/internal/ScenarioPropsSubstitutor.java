package com.cucumber.utils.context.props.internal;

import com.cucumber.utils.context.props.ScenarioProps;
import io.jtest.utils.StringParser;

import java.util.List;
import java.util.regex.Pattern;

public class ScenarioPropsSubstitutor {

    public static final String PREFIX = "#[";
    public static final String SUFFIX = "]";

    private static final Pattern captureGroupPattern = Pattern.compile(Pattern.quote(PREFIX) + "(.*?)" + Pattern.quote(SUFFIX),
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

    public static Object replace(String source, ScenarioProps scenarioProps) {
        List<String> placeholderNames = StringParser.captureValues(source, captureGroupPattern);
        if (placeholderNames.isEmpty()) {
            return source;
        }
        return StringParser.replacePlaceholders(placeholderNames, source, PREFIX, SUFFIX, scenarioProps::get,
                k -> scenarioProps.get(k) != null || scenarioProps.containsKey(k));
    }
}