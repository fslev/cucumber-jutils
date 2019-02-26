package com.cucumber.utils.basicstepdefs.sql;

import com.cucumber.utils.clients.database.SqlClient;
import com.cucumber.utils.context.compare.Cucumbers;
import com.cucumber.utils.engineering.utils.ResourceUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;

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
        this.dataSource = ResourceUtils.readProps(filePath);
        SqlClient.Builder builder = new SqlClient.Builder();
        builder.driver(dataSource.getProperty("driver").trim())
                .user(dataSource.getProperty("username")).pwd(dataSource.getProperty("password"))
                .url(dataSource.getProperty("url"));
        this.client = builder.build();
    }

    @Then("SQL execute query \"{cstring}\"")
    public void executeQuery(String query) {
        this.client.executeQuery(query);
    }

    @Then("SQL execute query \"{cstring}\" and compare result with")
    public void executeQueryAndCompare(String query, List expected) {
        this.result = client.executeQuery(query);
        Cucumbers.compare(expected, result, false, true);
    }

    @Then("SQL execute query \"{cstring}\" and poll for {int}s while comparing result with")
    public void executeQueryAndPollAndCompare(String query, int pollDuration, List expected) {
        Cucumbers.pollAndCompare(expected, () -> client.executeQuery(query), false, true);
    }

    @Then("SQL execute update \"{cstring}\"")
    public void executeUpdate(String sql) {
        this.client.executeUpdate(sql);
    }

    @Then("SQL INSERT into table \"{cstring}\" the following data")
    public void insertDataInsideTable(String table, List data) {
        String sql = "INSERT INTO " + table + " (%s) values (%s)";
        ((List<Map<String, String>>) data).forEach(map -> {
            StringBuilder columns = new StringBuilder();
            StringBuilder values = new StringBuilder();
            map.forEach((k, v) -> {
                columns.append(columns.length() != 0 ? "," : "").append(k);
                values.append(values.length() != 0 ? "," : "").append(v.equals("null") ? null : "'" + v + "'");
            });
            this.client.executeUpdate(String.format(sql, columns.toString(), values.toString()));
        });
    }

    @Then("SQL UPDATE table \"{cstring}\" WHERE \"{cstring}\" with the following data")
    public void updateDataFromTable(String table, String cond, List data) {
        String sql = "UPDATE " + table + " SET %s WHERE " + cond;
        StringBuilder assignmentValues = new StringBuilder();
        ((List<Map<String, String>>) data).forEach(map -> {
            map.forEach((k, v) -> assignmentValues.append(assignmentValues.length() != 0 ? "," : "")
                    .append(k).append("=").append(v.equals("null") ? null : "'" + v + "'"));
            this.client.executeUpdate(String.format(sql, assignmentValues.toString()));
        });
    }
}
