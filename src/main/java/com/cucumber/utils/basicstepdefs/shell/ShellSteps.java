package com.cucumber.utils.basicstepdefs.shell;

import com.cucumber.utils.clients.shell.ShellClient;
import com.cucumber.utils.context.compare.Cucumbers;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class ShellSteps {

    private ShellClient shellClient = new ShellClient();

    @Then("SHELL execute command \"{cstring}\" and check response=\"{cstring}\"")
    public void executeAndCompare(String cmd, String expected) {
        String actual = shellClient.command(cmd).trim();
        Cucumbers.compare(expected, actual);
    }
}
