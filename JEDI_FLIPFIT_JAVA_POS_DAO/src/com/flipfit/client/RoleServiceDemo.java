package com.flipfit.client;

import com.flipfit.bean.Role;
import com.flipfit.business.RoleService;
import java.util.List;
/**
 * The Class RoleServiceDemo.
 *
 * @author Ananya
 * @ClassName  "RoleServiceDemo"
 */
public class RoleServiceDemo {
  /**
   * Main.
   *
   * @param args the args
   */
    public static void main(String[] args) {
        System.out.println("========== Role Service Demo ==========\n");

        RoleService roleService = new RoleService();

        // 1. Create Role
        System.out.println("Test 1: Creating Roles");
        Role role1 = new Role();
        role1.setRoleName("SuperAdmin");
        roleService.createRole(role1);
        System.out.println("Created role: SuperAdmin");

        Role role2 = new Role();
        role2.setRoleName("Guest");
        roleService.createRole(role2);
        System.out.println("Created role: Guest");

        // 2. Get All Roles
        System.out.println("\nTest 2: Get All Roles");
        List<Role> roles = roleService.getAllRoles();
        for (Role role : roles) {
            System.out.println("Role ID: " + role.getRoleId() + ", Name: " + role.getRoleName());
        }

        // 3. Get Role by Name
        System.out.println("\nTest 3: Get Role by Name 'SuperAdmin'");
        Role fetchedRole = roleService.getRoleByName("SuperAdmin");
        if (fetchedRole != null) {
            System.out.println("Found Role: " + fetchedRole.getRoleId() + " - " + fetchedRole.getRoleName());

            // 4. Update Role
            System.out.println("\nTest 4: Update Role Name");
            fetchedRole.setRoleName("SystemAdmin");
            roleService.updateRole(fetchedRole);
            System.out.println("Updated role name to SystemAdmin");

            // Verify update
            Role updatedRole = roleService.getRoleById(fetchedRole.getRoleId());
            if (updatedRole != null) {
                System.out.println("Verified Update: " + updatedRole.getRoleName());
            }

            // 5. Delete Role
            System.out.println("\nTest 5: Delete Role");
            roleService.deleteRole(fetchedRole.getRoleId());
            System.out.println("Deleted Role ID: " + fetchedRole.getRoleId());
        } else {
            System.out.println("Role 'SuperAdmin' not found.");
        }

        System.out.println("\n========== Demo Complete ==========");
    }
}
