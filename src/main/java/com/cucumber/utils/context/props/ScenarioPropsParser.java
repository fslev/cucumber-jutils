package com.cucumber.utils.context.props;

import com.cucumber.utils.context.props.internal.ScenarioPropsSubstitutor;
import io.jtest.utils.common.SpELParser;

public class ScenarioPropsParser {

    public static Object parse(String source, ScenarioProps scenarioProps) {
        Object o = ScenarioPropsSubstitutor.replace(source, scenarioProps);
        if (o instanceof String) {
            return SpELParser.parse(o.toString());
        }
        return o;
    }
}
