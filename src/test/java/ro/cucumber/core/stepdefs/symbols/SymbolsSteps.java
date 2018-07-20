package ro.cucumber.core.stepdefs.symbols;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import static org.junit.Assert.assertEquals;

@ScenarioScoped
public class SymbolsSteps {

    private String str;

    @Given("The string with global symbols {symString}")
    public void setStringWithGlobalSymbols(String str) {
        this.str = str;
    }

    @Given("The string with scenario symbols {symString}")
    public void setStringWithScenarioSymbols(String str) {

        this.str = str;
    }

    @Then("Check parsed string equals {string}")
    public void check(String str) {
        assertEquals(str, this.str);
    }
}
