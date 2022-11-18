package com.cucumber.utils.context.vars;

import com.cucumber.utils.context.vars.internal.ScenarioVarsSubstitutor;
import io.jtest.utils.common.SpELParser;

public class ScenarioVarsParser {

    public static Object parse(String source, ScenarioVars scenarioVars) {
        Object result = ScenarioVarsSubstitutor.replace(source, scenarioVars);
        return result instanceof String ? SpELParser.parseQuietly((String) result) : result;
    }
}
