package com.cucumber.utils.context.vars;

import com.cucumber.utils.context.vars.internal.ScenarioVarsSubstitutor;
import io.jtest.utils.common.SpELParser;
import io.jtest.utils.common.StringParser;

import java.util.List;

public class ScenarioVarsParser {

    public static Object parse(String source, ScenarioVars scenarioVars) {
        if (source != null && !source.isEmpty()) {
            if (source.contains(SpELParser.PREFIX) && source.contains(SpELParser.SUFFIX)) {
                List<String> expressions = SpELParser.extractExpressions(source);
                if (!expressions.isEmpty()) {
                    Object result = StringParser.replacePlaceholders(expressions, source, SpELParser.PREFIX, SpELParser.SUFFIX,
                            exp -> {
                                Object parsedExp = ScenarioVarsSubstitutor.replace(exp, scenarioVars);
                                return parsedExp instanceof String ?
                                        SpELParser.parseExpression(SpELParser.unescapeDelimiters(parsedExp.toString())) : parsedExp;
                            }, exp -> {
                                Object parsedExp = ScenarioVarsSubstitutor.replace(exp, scenarioVars);
                                return parsedExp instanceof String ? SpELParser.isExpressionValid(parsedExp.toString()) : false;
                            });
                    return result instanceof String ? ScenarioVarsSubstitutor.replace(result.toString(), scenarioVars) : result;
                }
            }
            return ScenarioVarsSubstitutor.replace(source, scenarioVars);
        }
        return source;
    }
}
