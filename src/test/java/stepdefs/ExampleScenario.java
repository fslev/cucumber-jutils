package stepdefs;

import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import ro.cucumber.poc.AbstractHttpScenario;

public class ExampleScenario extends AbstractHttpScenario {

    @Override
    @When("^set headers")
    public void setHeaders(DataTable table) {
        super.setHeaders(table);
    }

}
