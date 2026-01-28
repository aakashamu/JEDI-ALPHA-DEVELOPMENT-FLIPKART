package com.flipfit.bean;
/**
 * The Class Role.
 *
 * @author Ananya
 * @ClassName  "Role"
 */
public class Role {
    private int roleId;
    private String roleName;
  /**
   * Role.
   *
   * @return the public
   */
    public Role() {
    }
  /**
   * Role.
   *
   * @param roleId the roleId
   * @param roleName the roleName
   * @return the public
   */
    public Role(int roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }
  /**
   * Get Role Id.
   *
   * @return the int
   */
    public int getRoleId() {
        return roleId;
    }
  /**
   * Set Role Id.
   *
   * @param roleId the roleId
   */
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
  /**
   * Get Role Name.
   *
   * @return the String
   */
    public String getRoleName() {
        return roleName;
    }
  /**
   * Set Role Name.
   *
   * @param roleName the roleName
   */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
