package com.flipfit.dao;

import com.flipfit.bean.Role;
import java.util.List;

public interface FlipFitRoleDAOInterface {
    void addRole(Role role);

    Role getRoleById(int roleId);

    Role getRoleByName(String roleName);

    List<Role> getAllRoles();

    void updateRole(Role role);

    void deleteRole(int roleId);
}