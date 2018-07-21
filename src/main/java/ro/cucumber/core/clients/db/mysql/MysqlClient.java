package ro.cucumber.core.clients.db.mysql;

import java.sql.*;

public class MysqlClient {
    private String url;
    private String user;
    private String pwd;
    private Connection conn;

    private MysqlClient(Builder builder) {
        this.url = builder.url;
        this.user = builder.user;
        this.pwd = builder.pwd;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.conn = DriverManager.getConnection(url, user, pwd);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet executeQuery(String sql) {
        try {
            Statement st = conn.createStatement();
            return st.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        try {
            this.conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static class Builder {
        private String url;
        private String user;
        private String pwd;

        public Builder url(String url, String user, String pwd) {
            this.url = url;
            this.user = user;
            this.pwd = pwd;
            return this;
        }

        public MysqlClient build() {
            return new MysqlClient(this);
        }
    }
}


