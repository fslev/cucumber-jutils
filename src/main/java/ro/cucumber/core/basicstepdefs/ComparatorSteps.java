package ro.cucumber.core.basicstepdefs;

import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import ro.cucumber.core.context.comparator.Comparator;

import java.util.List;
import java.util.Map;

@ScenarioScoped
public class ComparatorSteps {
    @Then("compare {cstring} with {cstring}")
    public void compareWithString(Object expected, String actual) {
        Comparator.compare(expected, actual);
    }

    @Then("compare {cstring} with")
    public void compareWithDocString(Object expected, String actual) {
        Comparator.compare(expected, actual);
    }

    @Then("compare {cstring} against table")
    public void compareWithDataTable(Object expected, List<Map<String,String>> actual) {
        System.out.println(actual);
        Comparator.compare(expected, actual);
    }
}
