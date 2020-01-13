package com.cucumber.utils.context.utils;

import cucumber.runtime.java.guice.ScenarioScoped;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.Before;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;

@ScenarioScoped
public class ScenarioUtils {
    private Logger log = LogManager.getLogger();
    private Scenario scenario;

    @Before(order = Integer.MIN_VALUE)
    public void init(Scenario scenario) {
        this.scenario = scenario;
        log.info("PREPARED scenario [{}]", scenario.getName());
    }

    public void log(String msg, Object... args) {
        scenario.write(ParameterizedMessage.format(msg, args));
    }

    public Scenario getScenario() {
        return scenario;
    }
}
