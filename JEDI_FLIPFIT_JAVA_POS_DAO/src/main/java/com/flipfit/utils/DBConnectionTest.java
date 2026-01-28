package com.flipfit.utils;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionTest {

    public static void main(String[] args) {
        System.out.println("Testing Database Connection...\n");
        
        try {
            // Try with additional connection properties
            java.util.Properties props = new java.util.Properties();
            props.setProperty("user", "root");
            props.setProperty("password", "idae7ooG!@#%");
            props.setProperty("serverTimezone", "UTC");
            props.setProperty("allowPublicKeyRetrieval", "true");
            props.setProperty("useSSL", "false");
            
            java.sql.Connection conn = java.sql.DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/FlipFitDB", props);
            
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ SUCCESS! Database connection established.");
                System.out.println("✓ Connected to: jdbc:mysql://localhost:3306/FlipFitDB");
                System.out.println("✓ Database: " + conn.getCatalog());
                System.out.println("✓ Database is ready to use!");
                
                conn.close();
                System.out.println("\n✓ Connection closed successfully.");
            } else {
                System.out.println("✗ FAILED! Connection is null or closed.");
            }
        } catch (SQLException e) {
            System.out.println("✗ FAILED! Database Connection Error:");
            System.out.println("  Error: " + e.getMessage());
            System.out.println("\nTroubleshooting steps:");
            System.out.println("1. Ensure MySQL Server is running");
            System.out.println("2. Verify database 'FlipFitDB' exists");
            System.out.println("3. Check username 'root' and password are correct");
            System.out.println("4. Ensure MySQL JDBC driver is in the classpath");
            e.printStackTrace();
        }
    }
}
