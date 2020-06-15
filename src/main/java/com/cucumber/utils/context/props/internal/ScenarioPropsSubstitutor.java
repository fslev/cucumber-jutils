package com.cucumber.utils.context.props.internal;

import com.cucumber.utils.context.props.ScenarioProps;

import java.util.regex.Pattern;

public class ScenarioPropsSubstitutor {

    public static final String PREFIX = "#[";
    public static final String SUFFIX = "]";

    private static final Pattern captureGroupPattern = Pattern.compile(Pattern.quote(PREFIX) + "(.*?)" + Pattern.quote(SUFFIX),
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

    public static Object replace(String source, ScenarioProps scenarioProps) {
        return StringParser.replacePlaceholders(source, PREFIX, SUFFIX, captureGroupPattern, scenarioProps::get, scenarioProps::containsKey);
    }
}