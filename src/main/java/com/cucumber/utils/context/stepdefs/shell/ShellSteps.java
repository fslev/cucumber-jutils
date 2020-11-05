package com.cucumber.utils.context.stepdefs.shell;

import com.cucumber.utils.context.ScenarioUtils;
import com.cucumber.utils.context.props.ScenarioProps;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Then;
import io.jtest.utils.clients.shell.ShellClient;
import io.jtest.utils.matcher.ObjectMatcher;

@ScenarioScoped
public class ShellSteps {

    @Inject
    private ScenarioProps scenarioProps;
    @Inject
    private ScenarioUtils logger;
    private final ShellClient shellClient = new ShellClient();

    @Then("SHELL execute command \"{}\" and check response=\"{}\"")
    public void executeAndCompare(String cmd, String expected) {
        logger.log("    Execute cmd:\n{}\n    And compare response with:\n{}", cmd, expected);
        String actual = shellClient.command("bash", "-c", cmd).trim();
        scenarioProps.putAll(ObjectMatcher.match(null, expected, actual));
    }
}
