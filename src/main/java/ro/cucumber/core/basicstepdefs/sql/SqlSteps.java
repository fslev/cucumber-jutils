package ro.cucumber.core.basicstepdefs.sql;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import ro.cucumber.core.clients.database.SqlClient;
import ro.cucumber.core.context.compare.Cucumbers;
import ro.cucumber.core.engineering.utils.ResourceUtils;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@ScenarioScoped
public class SqlSteps {

    private SqlClient client;
    private Properties dataSource;
    private List<Map<String, String>> result;

    @Given("SQL data source from file path \"{cstring}\"")
    public void setDataSource(String filePath) throws IOException {
        dataSource = ResourceUtils.readProps(filePath);
        SqlClient.Builder builder = new SqlClient.Builder();
        builder.driver(dataSource.getProperty("driver").trim())
                .user(dataSource.getProperty("username")).pwd(dataSource.getProperty("password"))
                .url(dataSource.getProperty("url"));
        client = builder.build();
    }

    @Then("SQL execute query \"{cstring}\" and compare result with")
    public void executeQuery(String query, List expected) {
        result = client.executeQuery(query);
        Cucumbers.compare(expected, result, false, true);
    }
}
