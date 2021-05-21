package com.cucumber.utils.context.stepdefs.shell;

import com.cucumber.utils.context.ScenarioUtils;
import com.cucumber.utils.context.props.ScenarioProps;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Then;
import io.jtest.utils.clients.shell.ShellClient;
import io.jtest.utils.matcher.ObjectMatcher;
import io.jtest.utils.matcher.condition.MatchCondition;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@ScenarioScoped
public class ShellSteps {

    @Inject
    private ScenarioProps scenarioProps;
    @Inject
    private ScenarioUtils logger;
    private final ShellClient shellClient = new ShellClient();

    @Then("SHELL execute command \"{}\" and check response=\"{}\"")
    public void executeAndMatch(String cmd, String expected) {
        executeAndMatch(cmd, null, expected, new HashSet<>());
    }

    @Then("SHELL execute command \"{}\" and check response is")
    public void executeAndMatch(String cmd, StringBuilder expected) {
        executeAndMatch(cmd, null, expected.toString(), new HashSet<>());
    }

    @Then("SHELL execute command \"{}\" and check {}s until response=\"{}\" using matchConditions={}")
    public void executeAndMatch(String cmd, Integer pollingTimeoutSeconds, String expected, Set<MatchCondition> matchConditions) {
        logger.log("BASH CMD_>\n{}\n\n--------- EXPECTED --------\n{}\n", cmd, expected);
        AtomicReference<String> wrapper = new AtomicReference<>();
        try {
            if (pollingTimeoutSeconds == null || pollingTimeoutSeconds == 0) {
                wrapper.set(shellClient.execute("bash", "-c", cmd));
                scenarioProps.putAll(ObjectMatcher.match(null, expected, wrapper.get(), matchConditions.toArray(new MatchCondition[0])));
            } else {
                scenarioProps.putAll(ObjectMatcher.match(null, expected, () -> {
                    wrapper.set(shellClient.execute("bash", "-c", cmd));
                    return wrapper.get();
                }, pollingTimeoutSeconds, null, null, matchConditions.toArray(new MatchCondition[0])));
            }
        } finally {
            logger.log("\n------ ACTUAL OUTPUT ------\n{}", wrapper.get());
        }
    }

    @Then("SHELL execute command \"{}\" and check {}s until response is")
    public void executeAndMatch(String cmd, Integer pollingTimeoutSeconds, StringBuilder expected) {
        executeAndMatch(cmd, pollingTimeoutSeconds, expected.toString(), new HashSet<>());
    }
}
