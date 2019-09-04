package com.cucumber.utils.context.stepdefs.shell;

import com.cucumber.utils.clients.shell.ShellClient;
import com.cucumber.utils.context.utils.Cucumbers;
import com.google.inject.Inject;
import cucumber.runtime.java.guice.ScenarioScoped;
import io.cucumber.java.en.Then;

@ScenarioScoped
public class ShellSteps {

    @Inject
    private Cucumbers cucumbers;
    private ShellClient shellClient = new ShellClient();

    @Then("SHELL execute command \"{}\" and check response=\"{}\"")
    public void executeAndCompare(String cmd, String expected) {
        String actual = shellClient.command("bash", "-c", cmd).trim();
        cucumbers.compare(expected, actual);
    }
}
