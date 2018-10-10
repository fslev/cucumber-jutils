package ro.cucumber.core.features.stepdefs.noexp;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import static org.junit.Assert.assertEquals;

@ScenarioScoped
public class NoExpressionSteps {

    private int a;
    private int b;


    @Given("number a=(\\d+)")
    public void setNumerOne(int a) {
        this.a = a;
    }

    @And("number b=(\\d+)")
    public void setNumerTwo(int b) {
        this.b = b;
    }

    @Then("^the sum is (\\d+)$")
    public void checkSum(int result) {
        assertEquals(result, this.a + this.b);
    }

    @Then("^the product is (\\d+)$")
    public void checkProduct(int result) {
        assertEquals(result, this.a * this.b);
    }
}
