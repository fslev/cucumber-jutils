package ro.cucumber.core.features.stepdefs.placeholders;

import com.google.inject.Inject;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import ro.cucumber.core.context.props.ScenarioProps;

import static org.junit.Assert.assertEquals;

@ScenarioScoped
public class PlaceholderParserSteps {

    private String str;

    @Inject
    private ScenarioProps scenarioProps;

    @Given("The string with global placeholders \"{cstring}\"")
    public void stringWithGlobalSymbols(String str) {
        this.str = str;
    }

    @Given("The string with scenario placeholders \"{cstring}\"")
    public void stringWithScenarioSymbols(String str) {
        this.str = str;
    }

    @Then("Check filled string equals \"{cstring}\"")
    public void check(String str) {
        assertEquals(str, this.str);
    }
}
