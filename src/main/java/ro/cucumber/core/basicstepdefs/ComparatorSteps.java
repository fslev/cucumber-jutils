package ro.cucumber.core.basicstepdefs;

import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import ro.cucumber.core.context.comparator.Comparator;

@ScenarioScoped
public class ComparatorSteps {
    @Then("compare {cstring} with {cstring}")
    public void compareWithString(Object expected, Object actual) {
        Comparator.compare(expected, actual);
    }

    @Then("compare {cstring} with")
    public void compareWithDocString(Object expected, Object actual) {
        Comparator.compare(expected, actual);
    }
}
