package com.flipfit.business;

import com.flipfit.bean.Role;
import com.flipfit.dao.FlipFitRoleDAO;
import com.flipfit.dao.FlipFitRoleDAOInterface;
import java.util.List;
/**
 * The Class RoleService.
 *
 * @author Ananya
 * @ClassName  "RoleService"
 */
public class RoleService implements RoleInterface {

    private FlipFitRoleDAOInterface roleDAO = new FlipFitRoleDAO();
  /**
   * Create Role.
   *
   * @param role the role
   */
    @Override
    public void createRole(Role role) {
        roleDAO.addRole(role);
    }
  /**
   * Get Role By Id.
   *
   * @param roleId the roleId
   * @return the Role
   */
    @Override
    public Role getRoleById(int roleId) {
        return roleDAO.getRoleById(roleId);
    }
  /**
   * Get Role By Name.
   *
   * @param roleName the roleName
   * @return the Role
   */
    @Override
    public Role getRoleByName(String roleName) {
        return roleDAO.getRoleByName(roleName);
    }
  /**
   * Get All Roles.
   *
   * @return the List<Role>
   */
    @Override
    public List<Role> getAllRoles() {
        return roleDAO.getAllRoles();
    }
  /**
   * Update Role.
   *
   * @param role the role
   */
    @Override
    public void updateRole(Role role) {
        roleDAO.updateRole(role);
    }
  /**
   * Delete Role.
   *
   * @param roleId the roleId
   */
    @Override
    public void deleteRole(int roleId) {
        roleDAO.deleteRole(roleId);
    }
}
