package com.cucumber.utils.context.stepdefs;

import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;

@ScenarioScoped
public class CommonSteps {

    /**
     * Cucumber native JSON reporting plugin does not support comments
     * Therefore, the step from bellow simulates comments
     *
     * @param s
     */
    @Given("^#[^><](.+)$")
    public void comment(String s) {
    }

    /**
     * Add collapsible support to Cucumber maven reporting plugin
     *
     * @param s
     */
    @Given("^#>>(.+)$")
    public void startCollapsible(String s) {
    }

    @Given("^#<<$")
    public void endCollapsible() {
    }

    @Given("Wait {}s")
    public void wait(int until) throws InterruptedException {
        Thread.sleep(until * 1000);
    }
}