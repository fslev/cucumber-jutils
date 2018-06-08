package stepdefs;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertEquals;

public class Example1 {

    {
        System.out.println("-------- Instance 1 -----------");
    }

    @Given("^I am on Facebook login page$")
    public void goToFacebook() {
        throw new PendingException();
    }

    @When("^I enter username as$")
    public void enterUsername(String arg1) {
        System.out.println("Enter username " + arg1);
    }

    @When("^I enter password as \"(.*)\"$")
    public void enterPassword(String arg1) {
        System.out.println("Enter pass " + arg1);
        // Assert.assertEquals("ZEST", arg1);
    }

    @Then("^Login should fail$")
    public void checkFail() {
        assertEquals(1, 1);
        System.out.println("da");
    }

    @Then("^Relogin option should be available$")
    public void checkRelogin() {
        assertEquals(1, 1);
    }
}
