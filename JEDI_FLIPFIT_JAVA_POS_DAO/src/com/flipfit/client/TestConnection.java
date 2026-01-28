package com.flipfit.client;

import com.flipfit.utils.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
/**
 * The Class TestConnection.
 *
 * @author Ananya
 * @ClassName  "TestConnection"
 */
public class TestConnection {
  /**
   * Get Connection.
   *
   * @return the Connection
   */
    public static Connection getConnection() {
        try {
            return DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
  /**
   * Main.
   *
   * @param args the args
   */
    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("SUCCESS: Connected to the database!");
        } else {
            System.out.println("ERROR: Connection failed.");
        }
    }
}