package com.cucumber.utils.context.vars;

import com.cucumber.utils.context.vars.internal.ScenarioVarsSubstitutor;
import io.jtest.utils.common.SpELParser;

public class ScenarioVarsParser {

    public static Object parse(String source, ScenarioVars scenarioVars) {
        Object o = ScenarioVarsSubstitutor.replace(source, scenarioVars);
        if (o instanceof String) {
            return SpELParser.parse(o.toString());
        }
        return o;
    }
}
