package com.flipfit.rest;

import com.flipfit.business.UserService;
import com.flipfit.bean.User;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * The Class UserResource.
 *
 * @author Ananya
 * @ClassName  "UserResource"
 */
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    private final UserService userService = new UserService();

    /**
     * Authenticates a user by email and password.
     *
     * @param email    user email
     * @param password user password
     * @return 200 with user details if valid, 401 if invalid, 500 on error
     */
    @POST
    @Path("/login")
    public Response login(@QueryParam("email") String email, @QueryParam("password") String password) {
        try {
            boolean success = userService.login(email, password);
            if (success) {
                User user = UserService.getCurrentUser(email);
                return Response.ok(user).build();
            }
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid email or password").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Updates the user password after validating the old password.
     *
     * @param email       user email
     * @param oldPassword current password
     * @param newPassword new password to set
     * @return 200 on success, 400 if update fails
     */
    @POST
    @Path("/change-password")
    public Response changePassword(@QueryParam("email") String email, 
                                   @QueryParam("oldPassword") String oldPassword, 
                                   @QueryParam("newPassword") String newPassword) {
        if (userService.updatePassword(email, oldPassword, newPassword)) {
            return Response.ok("Password updated successfully").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update password").build();
    }

    /**
     * Returns all notifications for the given user.
     *
     * @param userId the user ID
     * @return 200 with list of notifications
     */
    @GET
    @Path("/notifications/{userId}")
    public Response getNotifications(@PathParam("userId") int userId) {
        com.flipfit.business.NotificationService notificationService = new com.flipfit.business.NotificationService();
        return Response.ok(notificationService.getUserNotifications(userId)).build();
    }
}
