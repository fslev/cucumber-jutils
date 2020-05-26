package com.cucumber.utils.context.props;

import com.cucumber.utils.context.props.internal.ScenarioPropsSpelSubstitutor;
import com.cucumber.utils.context.props.internal.ScenarioPropsSubstitutor;

import java.util.Optional;

public class ScenarioPropsParser {

    public static Object parse(String source, ScenarioProps scenarioProps) {
        Object o = ScenarioPropsSubstitutor.replace(source, scenarioProps);
        if (o instanceof String) {
            return ScenarioPropsSpelSubstitutor.parse(o.toString());
        }
        return o;
    }
}
