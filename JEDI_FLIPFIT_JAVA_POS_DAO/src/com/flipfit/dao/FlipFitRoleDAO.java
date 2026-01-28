package com.flipfit.dao;

import com.flipfit.bean.Role;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * The Class FlipFitRoleDAO.
 *
 * @author Ananya
 * @ClassName  "FlipFitRoleDAO"
 */
public class FlipFitRoleDAO implements FlipFitRoleDAOInterface {
    private Connection connection;
  /**
   * Flip Fit Role Dao.
   *
   * @return the public
   */
    public FlipFitRoleDAO() {
    }
  /**
   * Get Connection.
   *
   * @return the Connection
 * @throws SQLException 
   */
    private Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = com.flipfit.utils.DBConnection.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return connection;
    }
  /**
   * Flip Fit Role Dao.
   *
   * @param connection the connection
   * @return the public
   */
    public FlipFitRoleDAO(Connection connection) {
        this.connection = connection;
    }
  /**
   * Add Role.
   *
   * @param role the role
   */
    @Override
    public void addRole(Role role) {
        String sql = "INSERT INTO Role (roleName) VALUES (?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role.getRoleName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  /**
   * Get Role By Id.
   *
   * @param roleId the roleId
   * @return the Role
   */
    @Override
    public Role getRoleById(int roleId) {
        String sql = "SELECT * FROM Role WHERE roleId = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Role(rs.getInt("roleId"), rs.getString("roleName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
  /**
   * Get Role By Name.
   *
   * @param roleName the roleName
   * @return the Role
   */
    @Override
    public Role getRoleByName(String roleName) {
        String sql = "SELECT * FROM Role WHERE roleName = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roleName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Role(rs.getInt("roleId"), rs.getString("roleName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
  /**
   * Get All Roles.
   *
   * @return the List<Role>
   */
    @Override
    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM Role";
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                roles.add(new Role(rs.getInt("roleId"), rs.getString("roleName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }
  /**
   * Update Role.
   *
   * @param role the role
   */
    @Override
    public void updateRole(Role role) {
        String sql = "UPDATE Role SET roleName = ? WHERE roleId = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role.getRoleName());
            stmt.setInt(2, role.getRoleId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  /**
   * Delete Role.
   *
   * @param roleId the roleId
   */
    @Override
    public void deleteRole(int roleId) {
        String sql = "DELETE FROM Role WHERE roleId = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
