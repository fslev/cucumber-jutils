package com.cucumber.utils.clients.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlClient {
    private static int MAX_ROWS = 100;
    private Logger log = LogManager.getLogger();
    private String url;
    private String user;
    private String pwd;
    private String driverClassName;

    public SqlClient(String url, String user, String pwd, String driverClassName) {
        this.url = url;
        this.user = user;
        this.pwd = pwd;
        this.driverClassName = driverClassName;
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Map<String, String>> executeQuery(String sql) {
        log.debug("---- SQL QUERY REQUEST ----");
        log.debug("Driver: {}", driverClassName);
        log.debug("Database url: {}", url);
        log.debug("SQL query: {}", sql);
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<Map<String, String>> tableData = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url, user, pwd);
            pst = conn.prepareStatement(sql);
            pst.setMaxRows(MAX_ROWS);
            rs = pst.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();
            while (rs.next()) {
                Map<String, String> rowData = new HashMap<>();
                for (int i = 1; i <= columns; i++) {
                    Object value = rs.getObject(i);
                    rowData.put(md.getColumnLabel(i), value != null ? value.toString() : null);
                }
                tableData.add(rowData);
            }
            return tableData;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
                log.debug("SQL result: {}", tableData);
                log.debug("-----------------------");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int executeUpdate(String sql) {
        log.debug("---- SQL UPDATE REQUEST ----");
        log.debug("Driver: {}", driverClassName);
        log.debug("Database url: {}", url);
        log.debug("SQL update: {}", sql);
        Connection conn = null;
        Statement st = null;
        int affected = 0;
        try {
            conn = DriverManager.getConnection(url, user, pwd);
            st = conn.prepareStatement(sql);
            affected = st.executeUpdate(sql);
            return affected;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (conn != null) {
                    conn.close();
                }
                log.debug("SQL affected rows: {}", affected);
                log.debug("-----------------------");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


