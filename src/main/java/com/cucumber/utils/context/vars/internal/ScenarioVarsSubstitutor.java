package com.cucumber.utils.context.vars.internal;

import com.cucumber.utils.context.vars.ScenarioVars;
import io.jtest.utils.common.StringParser;

import java.util.List;
import java.util.regex.Pattern;

public class ScenarioVarsSubstitutor {

    public static final String PREFIX = "#[";
    public static final String SUFFIX = "]";

    private static final Pattern captureGroupPattern = Pattern.compile(Pattern.quote(PREFIX) + "(.*?)" + Pattern.quote(SUFFIX),
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

    public static Object replace(String source, ScenarioVars scenarioVars) {
        List<String> placeholderNames = StringParser.captureValues(source, captureGroupPattern);
        if (placeholderNames.isEmpty()) {
            return source;
        }
        return StringParser.replacePlaceholders(placeholderNames, source, PREFIX, SUFFIX, scenarioVars::get,
                k -> scenarioVars.get(k) != null || scenarioVars.containsName(k));
    }
}