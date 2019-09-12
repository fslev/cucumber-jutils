package com.cucumber.utils.context.utils;

import cucumber.runtime.java.guice.ScenarioScoped;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.Before;

@ScenarioScoped
public class ScenarioUtils {
    private Scenario scenario;

    @Before
    public void init(Scenario scenario) {
        this.scenario = scenario;
    }

    public void log(String msg, Object... args) {
        scenario.write(String.format(msg, args));
    }

    public Scenario getScenario() {
        return scenario;
    }
}
