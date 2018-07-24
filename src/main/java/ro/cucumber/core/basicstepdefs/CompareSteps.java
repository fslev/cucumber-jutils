package ro.cucumber.core.basicstepdefs;

import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import ro.cucumber.core.context.compare.CompareCucumbers;
import ro.cucumber.core.context.config.CustomDataTable;

@ScenarioScoped
public class CompareSteps {
    @Then("compare {cstring} with {cstring}")
    public void compareWithString(Object expected, String actual) {
        CompareCucumbers.compare(expected, actual);
    }

    @Then("compare {cstring} with")
    public void compareWithDocString(Object expected, String actual) {
        CompareCucumbers.compare(expected, actual);
    }

    @Then("compare {cstring} against table")
    public void compareWithDataTable(Object expected, CustomDataTable actual) {
        CompareCucumbers.compare(expected, actual);
    }
}
