package com.cucumber.utils.context.utils;

import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.message.ParameterizedMessage;

@ScenarioScoped
public class ScenarioUtils {
    private Scenario scenario;

    @Before(order = Integer.MIN_VALUE)
    public void init(Scenario scenario) {
        this.scenario = scenario;
    }

    public void log(String msg, Object... args) {
        scenario.write(ParameterizedMessage.format(msg, args));
    }

    public Scenario getScenario() {
        return scenario;
    }
}
