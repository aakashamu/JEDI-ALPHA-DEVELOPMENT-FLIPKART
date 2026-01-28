package com.flipfit.business;

import com.flipfit.bean.Role;
import com.flipfit.dao.FlipFitRoleDAO;
import com.flipfit.dao.FlipFitRoleDAOInterface;
import java.util.List;

public class RoleService implements RoleInterface {

    private FlipFitRoleDAOInterface roleDAO = new FlipFitRoleDAO();

    @Override
    public void createRole(Role role) {
        roleDAO.addRole(role);
    }

    @Override
    public Role getRoleById(int roleId) {
        return roleDAO.getRoleById(roleId);
    }

    @Override
    public Role getRoleByName(String roleName) {
        return roleDAO.getRoleByName(roleName);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleDAO.getAllRoles();
    }

    @Override
    public void updateRole(Role role) {
        roleDAO.updateRole(role);
    }

    @Override
    public void deleteRole(int roleId) {
        roleDAO.deleteRole(roleId);
    }
}
