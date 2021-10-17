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
        List<String> variableNames = StringParser.captureValues(source, captureGroupPattern);
        if (variableNames.isEmpty()) {
            return source;
        }
        return StringParser.replacePlaceholders(variableNames, source, PREFIX, SUFFIX, scenarioVars::get,
                k -> scenarioVars.get(k) != null || scenarioVars.containsVariable(k));
    }
}