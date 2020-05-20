package com.cucumber.utils.context.props;

import com.cucumber.utils.context.props.internal.ScenarioPropsSubstitutor;

public class ScenarioPropsParser {

    public static Object parse(String source, ScenarioProps scenarioProps) {
        return ScenarioPropsSubstitutor.replace(source, scenarioProps);
    }
}
