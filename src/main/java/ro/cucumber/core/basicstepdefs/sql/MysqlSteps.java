package ro.cucumber.core.basicstepdefs.sql;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import io.cucumber.datatable.DataTable;

@ScenarioScoped
public class MysqlSteps {
    private Scenario scenario;

    @Before
    public void initScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    @Then("^MYSQL compare result with$")
    public void setAddress(DataTable dataTable) {
        System.out.println(dataTable.asLists());
    }
}
