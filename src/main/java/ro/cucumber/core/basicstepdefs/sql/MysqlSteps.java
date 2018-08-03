package ro.cucumber.core.basicstepdefs.sql;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.BeforeStep;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import ro.cucumber.core.clients.database.mysql.MysqlClient;
import ro.cucumber.core.context.compare.CompareCucumbers;
import ro.cucumber.core.context.config.CustomDataTable;
import java.io.IOException;
import java.util.Properties;

@ScenarioScoped
public class MysqlSteps {

    private MysqlClient client;
    private Properties dataSource;
    private CustomDataTable result;
    private Scenario scenario;

    @Before
    public void init(Scenario scenario) {
        this.scenario = scenario;
    }

    @Given("MYSQL data source {cstring}")
    public void setDataSource(String filePath) throws IOException {
        result = null;
        dataSource = new Properties();
        dataSource
                .load(this.getClass().getClassLoader().getResourceAsStream("database/" + filePath));
        MysqlClient.Builder builder = new MysqlClient.Builder();
        builder.driver(dataSource.getProperty("driver").trim())
                .user(dataSource.getProperty("username")).pwd(dataSource.getProperty("password"))
                .url(dataSource.getProperty("url"));
        client = builder.build();
    }

    @Then("MYSQL execute query {cstring} and compare result with")
    public void executeQuery(String query, CustomDataTable expected) {
        scenario.write("Executing query " + query);
        result = client.executeQuery(query);
        CompareCucumbers.compare(expected, result);
    }
}
