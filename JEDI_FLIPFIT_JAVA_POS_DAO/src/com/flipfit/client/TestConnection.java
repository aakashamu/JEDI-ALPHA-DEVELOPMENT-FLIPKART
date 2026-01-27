package com.flipfit.client;

import com.flipfit.utils.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {
    // This method allows other classes to get the connection
    public static Connection getConnection() {
        try {
            return DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("SUCCESS: Connected to the database!");
        } else {
            System.out.println("ERROR: Connection failed.");
        }
    }
}