package com.cucumber.utils.context.props;

import com.cucumber.utils.context.props.internal.SpelParser;
import com.cucumber.utils.context.props.internal.ScenarioPropsSubstitutor;

public class ParamParser {

    public static Object parse(String source, ScenarioProps scenarioProps) {
        Object o = ScenarioPropsSubstitutor.replace(source, scenarioProps);
        System.out.println("--------------------");
        if (o instanceof String) {
            return SpelParser.parse(o.toString());
        }
        return o;
    }
}
