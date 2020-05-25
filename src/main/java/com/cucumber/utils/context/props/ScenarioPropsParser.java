package com.cucumber.utils.context.props;

import com.cucumber.utils.context.props.internal.ScenarioPropsSpelSubstitutor;
import com.cucumber.utils.context.props.internal.ScenarioPropsSubstitutor;

import java.util.Optional;

public class ScenarioPropsParser {

    public static Object parse(String source, ScenarioProps scenarioProps) {
        Object o = ScenarioPropsSubstitutor.replace(source, scenarioProps);
        Object[] obj = new Object[1];
        Optional.ofNullable(o).
                ifPresent(object -> {
                    obj[0] = ScenarioPropsSpelSubstitutor.replace(object.toString());
                });
        return obj[0];
    }
}
