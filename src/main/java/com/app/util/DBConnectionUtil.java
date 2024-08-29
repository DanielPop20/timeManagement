package com.app.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.InputStream;

public class DBConnectionUtil {
    private static final Properties dbProperties = new Properties();

    static {
        try (InputStream input = DBConnectionUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find db.properties");
            }
            dbProperties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load database properties", e);
        }
    }

    public static Connection getConnection() {
        Connection connection;
        try {
            String url = dbProperties.getProperty("db.url");
            String user = dbProperties.getProperty("db.username");
            String password = dbProperties.getProperty("db.password");

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
