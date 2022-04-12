package com.cucumber.utils.context.steps.sql;

import com.cucumber.utils.context.ScenarioUtils;
import com.cucumber.utils.context.vars.ScenarioVars;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.jtest.utils.clients.database.SqlClient;
import io.jtest.utils.common.ResourceUtils;
import io.jtest.utils.matcher.ObjectMatcher;
import io.jtest.utils.matcher.condition.MatchCondition;
import org.apache.logging.log4j.message.ParameterizedMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@ScenarioScoped
public class SqlSteps {

    @Inject
    private ScenarioVars scenarioVars;
    @Inject
    private ScenarioUtils logger;
    private SqlClient client;

    @Given("[sql-util] Load data source from file path \"{}\"")
    public void setDataSource(String filePath) throws IOException {
        Properties dataSource = ResourceUtils.readProps(filePath);
        this.client = new SqlClient(dataSource.getProperty("url"), dataSource.getProperty("username"),
                dataSource.getProperty("password"), dataSource.getProperty("driver").trim());
    }

    @Then("[sql-util] Execute query {} and check result={}")
    public void executeQueryAndMatchWithJson(String query, List<Map<String, Object>> expected) throws SQLException {
        executeQueryAndMatch(query, expected);
    }

    @Then("[sql-util] Execute query {} and check result is")
    public void executeQueryAndMatchWithTable(String query, List<Map<String, Object>> expected) throws SQLException {
        executeQueryAndMatch(query, expected);
    }

    public void executeQueryAndMatch(String query, Object expected) throws SQLException {
        logger.log("Execute query '{}' and match with: {}", query, expected);
        try {
            this.client.connect();
            this.client.prepareStatement(query);
            List<Map<String, Object>> result = client.executeQueryAndGetRsAsList();
            scenarioVars.putAll(ObjectMatcher.match(null, expected, result, MatchCondition.JSON_NON_EXTENSIBLE_OBJECT, MatchCondition.JSON_NON_EXTENSIBLE_ARRAY));
        } finally {
            this.client.close();
        }
    }

    @Then("[sql-util] Execute query {} and check {}s until result is")
    public void executeQueryAndMatch(String query, Integer pollingTimeoutSeconds, List<Map<String, Object>> expected) throws SQLException {
        logger.log("Execute query '{}' and check for {} until result = {}", query, pollingTimeoutSeconds, expected);
        try {
            this.client.connect();
            this.client.prepareStatement(query);
            scenarioVars.putAll(ObjectMatcher.match(null, expected, () -> {
                        try {
                            return client.executeQueryAndGetRsAsList();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }, Duration.ofSeconds(pollingTimeoutSeconds),
                    null, null, MatchCondition.JSON_NON_EXTENSIBLE_OBJECT, MatchCondition.JSON_NON_EXTENSIBLE_ARRAY));
        } finally {
            this.client.close();
        }
    }

    @Then("[sql-util] Execute update {}")
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

    @Then("[sql-util] INSERT into table {} the following data")
    public void insertDataInsideTable(String table, List<Map<String, Object>> data) throws SQLException {
        logger.log("Insert into table '{}', data: {}", table, data);
        try {
            this.client.connect();
            String sql = "INSERT INTO " + table + " ({}) values ({})";
            data.forEach(map -> {
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

    @Then("[sql-util] UPDATE table {} WHERE {} with the following data")
    public void updateDataFromTable(String table, String cond, List<Map<String, Object>> data) throws SQLException {
        logger.log("Update table '{}', with condition '{}', the following data: {}", table, cond, data);
        try {
            this.client.connect();
            String sql = "UPDATE " + table + " SET {} WHERE " + cond;
            StringBuilder assignmentValues = new StringBuilder();
            data.forEach(map -> {
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
            this.client.close();
        }
    }
}
