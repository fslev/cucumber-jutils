package com.cucumber.utils.context.steps.shell;

import com.cucumber.utils.context.ScenarioUtils;
import com.cucumber.utils.context.vars.ScenarioVars;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Then;
import io.jtest.utils.clients.shell.ShellClient;
import io.jtest.utils.matcher.ObjectMatcher;
import io.jtest.utils.matcher.condition.MatchCondition;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@ScenarioScoped
public class ShellSteps {

    private final ShellClient shellClient = new ShellClient();

    @Inject
    private ScenarioVars scenarioVars;
    @Inject
    private ScenarioUtils scenarioUtils;

    @Then("[shell-util] Execute {} and check response={}")
    public void executeAndMatch(String cmd, String expected) {
        executeAndMatch(cmd, null, expected, new HashSet<>());
    }

    @Then("[shell-util] Execute {} and check response is")
    public void executeAndMatch(String cmd, StringBuilder expected) {
        executeAndMatch(cmd, null, expected.toString(), new HashSet<>());
    }

    @Then("[shell-util] Execute {} and check {}s until response is")
    public void executeAndMatch(String cmd, Integer pollingTimeoutSeconds, StringBuilder expected) {
        executeAndMatch(cmd, pollingTimeoutSeconds, expected.toString(), new HashSet<>());
    }

    @Then("[shell-util] Execute {} and check {}s until response={} using matchConditions={}")
    public void executeAndMatch(String cmd, Integer pollingTimeoutSeconds, String expected, Set<MatchCondition> matchConditions) {
        scenarioUtils.log("[CMD: ------------------\n{}\n\n---------------------- EXPECTED ----------------------\n{}\n",
                cmd, expected);
        AtomicReference<String> output = new AtomicReference<>();
        try {
            if (pollingTimeoutSeconds == null) {
                output.set(shellClient.execute(cmd));
                scenarioVars.putAll(ObjectMatcher.match(null, expected, output.get(), matchConditions.toArray(new MatchCondition[0])));
            } else {
                scenarioVars.putAll(ObjectMatcher.match(null, expected, () -> {
                    output.set(shellClient.execute(cmd));
                    return output.get();
                }, Duration.ofSeconds(pollingTimeoutSeconds), null, null, matchConditions.toArray(new MatchCondition[0])));
            }
        } finally {
            scenarioUtils.log("\n----------------------- ACTUAL -----------------------\n{}", output.get());
        }
    }
}
