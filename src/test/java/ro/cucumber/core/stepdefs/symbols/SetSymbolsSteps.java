package ro.cucumber.core.stepdefs.symbols;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.runtime.java.guice.ScenarioScoped;
import ro.cucumber.core.context.ScenarioProps;
import com.google.inject.Inject;

@ScenarioScoped
public class SetSymbolsSteps {
    @Inject
    private ScenarioProps scenarioProps;

    private Scenario scenario;

    @Before
    public void initScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    @Given("Set scenario props with key {string} and value {string}")
    public void setScenarioProps(String key, String val) {
        scenarioProps.put(key, val);
        scenario.write("Scenario values were set");
    }
}
