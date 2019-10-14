package com.cucumber.utils.context.stepdefs.sql;

import com.cucumber.utils.clients.database.SqlClient;
import com.cucumber.utils.context.utils.Cucumbers;
import com.cucumber.utils.context.utils.ScenarioUtils;
import com.cucumber.utils.engineering.utils.ResourceUtils;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.logging.log4j.message.ParameterizedMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@ScenarioScoped
public class SqlSteps {

    @Inject
    private Cucumbers cucumbers;
    @Inject
    private ScenarioUtils logger;
    private SqlClient client;
    private Properties dataSource;
    private List<Map<String, String>> result;

    @Given("SQL data source from file path \"{}\"")
    public void setDataSource(String filePath) throws IOException {
        this.dataSource = ResourceUtils.readProps(filePath);
        this.client = new SqlClient(dataSource.getProperty("url"), dataSource.getProperty("username"),
                dataSource.getProperty("password"), dataSource.getProperty("driver").trim());
    }

    @Then("SQL execute query \"{}\"")
    public void executeQuery(String query) throws SQLException {
        logger.log("Execute query '{}'", query);
        try {
            this.client.connect();
            this.client.prepareStatement(query);
            this.client.executeQuery();
        } finally {
            this.client.close();
        }
    }

    @Then("SQL execute query \"{}\" and compare result with {}")
    public void executeQueryAndCompareWithJson(String query, List<Map<String, String>> expected) throws SQLException {
        executeQueryAndCompare(query, expected);
    }

    @Then("SQL execute query \"{}\" and compare result with")
    public void executeQueryAndCompareWithTable(String query, List expected) throws SQLException {
        executeQueryAndCompare(query, expected);
    }

    public void executeQueryAndCompare(String query, Object expected) throws SQLException {
        logger.log("Execute query '{}' and compare with: {}", query, expected);
        try {
            this.client.connect();
            this.client.prepareStatement(query);
            this.result = client.executeQueryAndGetRsAsList();
            cucumbers.compare(expected, result, false, true);
        } finally {
            this.client.close();
        }
    }

    @Then("SQL execute query \"{}\" and poll for {int}s while comparing result with")
    public void executeQueryAndPollAndCompare(String query, int pollDuration, List expected) throws SQLException {
        logger.log("Execute query '{}', poll {}s while comparing with: {}", query, pollDuration, expected);
        try {
            this.client.connect();
            this.client.prepareStatement(query);
            cucumbers.pollAndCompare(expected, pollDuration, () -> client.executeQueryAndGetRsAsList(), true, true);
        } finally {
            this.client.close();
        }
    }

    @Then("SQL execute update \"{}\"")
    public void executeUpdate(String sql) throws SQLException {
        logger.log("Execute update '{}'", sql);
        try {
            this.client.connect();
            this.client.prepareStatement(sql);
            this.client.executeUpdate();
        } finally {
            this.client.close();
        }
    }

    @Then("SQL INSERT into table \"{}\" the following data")
    public void insertDataInsideTable(String table, List data) throws SQLException {
        logger.log("Insert into table '{}', data: {}", table, data);
        try {
            this.client.connect();
            String sql = "INSERT INTO " + table + " ({}) values ({})";
            ((List<Map<String, String>>) data).forEach(map -> {
                StringBuilder columns = new StringBuilder();
                StringBuilder values = new StringBuilder();
                map.forEach((k, v) -> {
                    columns.append(columns.length() != 0 ? "," : "").append(k);
                    values.append(values.length() != 0 ? "," : "").append(v.equals("null") ? null : "'" + v + "'");
                });
                try {
                    this.client.prepareStatement(ParameterizedMessage.format(sql, new Object[]{columns, values}));
                    this.client.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } finally {
            this.client.close();
        }
    }

    @Then("SQL UPDATE table \"{}\" WHERE \"{}\" with the following data")
    public void updateDataFromTable(String table, String cond, List data) throws SQLException {
        logger.log("Update table '{}', with condition '{}', the following data: {}", table, cond, data);
        try {
            this.client.connect();
            String sql = "UPDATE " + table + " SET {} WHERE " + cond;
            StringBuilder assignmentValues = new StringBuilder();
            ((List<Map<String, String>>) data).forEach(map -> {
                map.forEach((k, v) -> assignmentValues.append(assignmentValues.length() != 0 ? "," : "")
                        .append(k).append("=").append(v.equals("null") ? null : "'" + v + "'"));
                try {
                    this.client.prepareStatement(ParameterizedMessage.format(sql, new Object[]{assignmentValues}));
                    this.client.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } finally {
            this.client.connect();
        }
    }
}
