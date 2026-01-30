package com.flipfit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * The Class DBConnection.
 *
 * @author Ananya
 * @ClassName "DBConnection"
 */
public class DBConnection {

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/FlipFitDB";
    private static final String USER = "root";
    private static final String PASSWORD = "Niu2Xei2!@#%";

    /**
     * Get Connection.
     *
     * @return the Connection
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Create properties for connection
            Properties props = new Properties();
            props.setProperty("user", USER);
            props.setProperty("password", PASSWORD);
            props.setProperty("serverTimezone", "UTC");
            props.setProperty("allowPublicKeyRetrieval", "true");
            props.setProperty("useSSL", "false");

            return DriverManager.getConnection(URL, props);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }
}
