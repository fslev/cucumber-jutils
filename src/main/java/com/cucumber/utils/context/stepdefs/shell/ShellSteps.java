package com.cucumber.utils.context.stepdefs.shell;

import com.cucumber.utils.clients.shell.ShellClient;
import com.cucumber.utils.context.utils.Cucumbers;
import com.cucumber.utils.context.utils.ScenarioUtils;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Then;

@ScenarioScoped
public class ShellSteps {

    @Inject
    private Cucumbers cucumbers;
    @Inject
    private ScenarioUtils logger;
    private final ShellClient shellClient = new ShellClient();

    @Then("SHELL execute command \"{}\" and check response=\"{}\"")
    public void executeAndCompare(String cmd, String expected) {
        logger.log("    Execute cmd:\n{}\n    And compare response with:\n{}", cmd, expected);
        String actual = shellClient.command("bash", "-c", cmd).trim();
        cucumbers.compare(expected, actual);
    }
}
