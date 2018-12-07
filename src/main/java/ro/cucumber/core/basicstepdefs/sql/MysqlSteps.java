package ro.cucumber.core.basicstepdefs.sql;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import ro.cucumber.core.clients.database.mysql.MysqlClient;
import ro.cucumber.core.context.compare.Cucumbers;
import ro.cucumber.core.engineering.utils.ResourceUtils;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@ScenarioScoped
public class MysqlSteps {

    private MysqlClient client;
    private Properties dataSource;
    private List<Map<String, String>> result;

    @Given("MYSQL data source from file path {cstring}")
    public void setDataSource(String filePath) throws IOException {
        result = null;
        dataSource = ResourceUtils.readProps(filePath);
        MysqlClient.Builder builder = new MysqlClient.Builder();
        builder.driver(dataSource.getProperty("driver").trim())
                .user(dataSource.getProperty("username")).pwd(dataSource.getProperty("password"))
                .url(dataSource.getProperty("url"));
        client = builder.build();
    }

    @Then("MYSQL execute query \"{cstring}\" and compare result with")
    public void executeQuery(String query, List expected) {
        result = client.executeQuery(query);
        Cucumbers.compare(expected, result, false, true);
    }
}
