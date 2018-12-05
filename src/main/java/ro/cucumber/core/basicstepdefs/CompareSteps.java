package ro.cucumber.core.basicstepdefs;

import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import ro.cucumber.core.context.compare.Cucumbers;
import ro.cucumber.core.context.config.CustomDataTable;
import ro.cucumber.core.engineering.utils.ResourceUtils;

@ScenarioScoped
public class CompareSteps {
    @Then("COMPARE {cstring} with {cstring}")
    public void compareWithString(Object expected, Object actual) {
        Cucumbers.compare(expected, actual);
    }

    @Then("COMPARE {cstring} from file path {cstring}")
    public void compareWithContentFromFilepath(Object expected, String filePath) {
        Cucumbers.compare(expected, ResourceUtils.read(filePath));
    }

    @Then("COMPARE {cstring} with")
    public void compareWithDocString(Object expected, String actual) {
        Cucumbers.compare(expected, actual);
    }

    @Then("COMPARE {cstring} against table")
    public void compareWithDataTable(Object expected, CustomDataTable actual) {
        Cucumbers.compare(expected, actual);
    }
}
