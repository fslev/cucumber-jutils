package ro.cucumber.core.features.stepdefs.symbols;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;

import static org.junit.Assert.assertEquals;

@ScenarioScoped
public class SymbolsSteps {

    private String str;

    @Given("The string with global symbols \"{cstring}\"")
    public void stringWithGlobalSymbols(String str) {
        this.str = str;
    }

    @Given("The string with scenario symbols \"{cstring}\"")
    public void stringWithScenarioSymbols(String str) {
        this.str = str;
    }

    @Then("Check parsed string equals \"{cstring}\"")
    public void check(String str) {
        assertEquals(str, this.str);
    }
}
