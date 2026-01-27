package com.flipfit.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    // This method allows other classes to get the connection
    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/flipfit_db"; // Match your DB name exactly
        String user = "root";
        String password = "purae9If!@#%"; 

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
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