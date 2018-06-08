package annotation;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.junit.Assertions;
import org.junit.Assert;

public class Annotation {
    @Given("^I am on Facebook login page$")

    public void goToFacebook() {
        System.out.println("Given 1");
    }

    @When("^I enter username as \"(.*)\"$")
    public void enterUsername(String arg1) {
        System.out.println("Enter username " + arg1);
    }

    @When("^I enter password as \"(.*)\"$")
    public void enterPassword(String arg1) {
        System.out.println("Enter pass " + arg1);
        Assert.assertEquals("ZEST", arg1);
    }

    @Then("^Login should fail$")
    public void checkFail() {
        System.out.println("Test1 Pass");
    }

    @Then("^Relogin option should be available$")
    public void checkRelogin() {
        System.out.println("Test2 Pass");
    }
}
