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
     * Default constructor.
     */
    public Role() {
    }

    /**
     * Constructor with role id and name.
     *
     * @param roleId   the role id
     * @param roleName the role name
     */
    public Role(int roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }
    /**
     * Gets the role id.
     *
     * @return the role id
     */
    public int getRoleId() {
        return roleId;
    }
    /**
     * Sets the role id.
     *
     * @param roleId the role id
     */
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    /**
     * Gets the role name.
     *
     * @return the role name
     */
    public String getRoleName() {
        return roleName;
    }
    /**
     * Sets the role name.
     *
     * @param roleName the role name
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
