package com.cucumber.utils.context.steps.shell;

import com.cucumber.utils.context.ScenarioUtils;
import com.cucumber.utils.context.vars.ScenarioVars;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Then;
import io.jtest.utils.clients.shell.ShellClient;
import io.jtest.utils.matcher.ObjectMatcher;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

@ScenarioScoped
public class ShellSteps {

    private final ShellClient shellClient = new ShellClient();

    @Inject
    private ScenarioVars scenarioVars;
    @Inject
    private ScenarioUtils scenarioUtils;

    @Then("[shell-util] Execute {} and check response={}")
    public void executeAndMatch(String[] commands, String expected) {
        executeAndMatch(commands, null, expected);
    }

    @Then("[shell-util] Execute {} and check response is")
    public void executeAndMatch(String[] commands, StringBuilder expected) {
        executeAndMatch(commands, null, expected.toString());
    }

    @Then("[shell-util] Execute {} and check {}s until response is")
    public void executeAndMatch(String[] commands, Integer pollingTimeoutSeconds, StringBuilder expected) {
        executeAndMatch(commands, pollingTimeoutSeconds, expected.toString());
    }

    @Then("[shell-util] Execute {} and check {}s until response={}")
    public void executeAndMatch(String[] commands, Integer pollingTimeoutSeconds, String expected) {
        scenarioUtils.log("CMD: -------------------------------------------------" + System.lineSeparator() + "{}" +
                System.lineSeparator() + System.lineSeparator() +
                "---------------------- EXPECTED ----------------------" +
                System.lineSeparator() + "{}" + System.lineSeparator(), Arrays.asList(commands), expected);
        AtomicReference<String> output = new AtomicReference<>();
        try {
            if (pollingTimeoutSeconds == null) {
                output.set(shellClient.execute(commands));
                scenarioVars.putAll(ObjectMatcher.match(null, expected, output.get()));
            } else {
                scenarioVars.putAll(ObjectMatcher.match(null, expected, () -> {
                    output.set(shellClient.execute(commands));
                    return output.get();
                }, Duration.ofSeconds(pollingTimeoutSeconds), null, null));
            }
        } finally {
            scenarioUtils.log(System.lineSeparator() + "----------------------- ACTUAL -----------------------" +
                    System.lineSeparator() + "{}", output.get());
        }
    }
}
