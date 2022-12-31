package com.cucumber.utils.context.vars;

import com.cucumber.utils.context.vars.internal.ScenarioVarsSubstitutor;
import com.cucumber.utils.context.vars.internal.SpELParser;

public class ScenarioVarsParser {

    private ScenarioVarsParser() {

    }

    public static Object parse(String source, ScenarioVars scenarioVars) {
        Object result = ScenarioVarsSubstitutor.replace(source, scenarioVars);
        return result instanceof String ? SpELParser.parseQuietly((String) result) : result;
    }
}
