package com.cucumber.utils.context;

import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;

@ScenarioScoped
public class ScenarioUtils {
    private final static Logger LOG = LogManager.getLogger();
    private Scenario scenario;

    @Before(order = Integer.MIN_VALUE)
    public void init(Scenario scenario) {
        this.scenario = scenario;
        LOG.info("Prepared scenario: [{}]", scenario.getName());
    }

    @After(order = Integer.MIN_VALUE)
    public void finish(Scenario scenario) {
        if (Status.PASSED.equals(scenario.getStatus())) {
            LOG.info("{} | Scenario [{}]", scenario.getStatus(), scenario.getName());
        } else
            LOG.info("{} | Scenario [{}] \n {}:{}", scenario.getStatus(), scenario.getName(),
                    scenario.getUri(), scenario.getLine());
    }

    public void log(String msg, Object... args) {
        scenario.log(ParameterizedMessage.format(msg, args));
    }

    public Scenario getScenario() {
        return scenario;
    }
}
