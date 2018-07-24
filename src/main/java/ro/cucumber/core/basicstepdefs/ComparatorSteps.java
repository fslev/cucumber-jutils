package ro.cucumber.core.basicstepdefs;

import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import ro.cucumber.core.context.compare.CompareCucumbers;

@ScenarioScoped
public class ComparatorSteps {
    @Then("compare {cstring} with {cstring}")
    public void compareWithString(Object expected, String actual) {
        CompareCucumbers.compare(expected, actual);
    }

    @Then("compare {cstring} with")
    public void compareWithDocString(Object expected, String actual) {
        CompareCucumbers.compare(expected, actual);
    }

    @Then("compare {cstring} against table")
    public void compareWithDataTable(Object expected, Object actual) {
        System.out.println(expected);
        System.out.println(actual);
        CompareCucumbers.compare(expected, actual);
    }
}
