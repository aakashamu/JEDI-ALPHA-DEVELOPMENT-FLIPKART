package com.flipfit.rest;

import com.flipfit.business.GymAdminService;
import com.flipfit.bean.GymAdmin;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * The Class GymAdminResource.
 *
 * @author Ananya
 * @ClassName "GymAdminResource"
 */
@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GymAdminResource {
    private final GymAdminService adminService = new GymAdminService();

    /**
     * Registers a new gym admin.
     *
     * @param admin the admin details (name, email, password, contact, address)
     * @return 201 on success, 500 on error
     */
    @POST
    @Path("/register")
    public Response register(GymAdmin admin) {
        try {
            adminService.registerAdmin(
                    admin.getFullName(),
                    admin.getEmail(),
                    admin.getPassword(),
                    admin.getPhoneNumber(),
                    admin.getCity(),
                    admin.getState(),
                    admin.getPincode());
            return Response.status(Response.Status.CREATED).entity("Admin registered successfully").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Error registering admin: " + e.getMessage()).build();
        }
    }

    /**
     * Returns list of gym owners (pending/approved) for the authenticated admin.
     *
     * @param email    admin email
     * @param password admin password
     * @return 200 with list of owners, 401 if auth missing
     */
    @GET
    @Path("/owners")
    public Response viewOwners(@QueryParam("email") String email, @QueryParam("password") String password) {
        if (email == null || password == null)
            return Response.status(401).entity("Auth fail").build();
        return Response.ok(adminService.viewAllGymOwners(email, password)).build();
    }

    /**
     * Returns list of centres for the authenticated admin.
     *
     * @param email    admin email
     * @param password admin password
     * @return 200 with list of centres, 401 if auth missing
     */
    @GET
    @Path("/centers")
    public Response viewCenters(@QueryParam("email") String email, @QueryParam("password") String password) {
        if (email == null || password == null)
            return Response.status(401).entity("Auth fail").build();
        return Response.ok(adminService.viewAllCentres(email, password)).build();
    }

    /**
     * Approves or validates a gym owner.
     *
     * @param ownerId  the owner user ID
     * @param email    admin email
     * @param password admin password
     * @return 200 on success, 404 if not found, 400 on error
     */
    @PUT
    @Path("/owner/approve/{ownerId}")
    public Response approveOwner(@PathParam("ownerId") int ownerId, @QueryParam("email") String email,
            @QueryParam("password") String password) {
        try {
            if (adminService.validateGymOwner(ownerId, email, password)) {
                return Response.ok("Owner validated").build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Approves a centre.
     *
     * @param centerId the centre ID
     * @param email    admin email
     * @param password admin password
     * @return 200 on success, 404 if not found, 400 on error
     */
    @PUT
    @Path("/center/approve/{centerId}")
    public Response approveCenter(@PathParam("centerId") int centerId, @QueryParam("email") String email,
            @QueryParam("password") String password) {
        try {
            if (adminService.approveCentre(centerId, email, password)) {
                return Response.ok("Center approved").build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Returns customer metrics for the authenticated admin (e.g. displayed in console).
     *
     * @param email    admin email
     * @param password admin password
     * @return 200 with metrics message, 401 if auth missing
     */
    @GET
    @Path("/metrics/customer")
    public Response getCustomerMetrics(@QueryParam("email") String email, @QueryParam("password") String password) {
        if (email == null || password == null)
            return Response.status(401).entity("Auth fail").build();
        adminService.viewCustomerMetrics(email, password);
        return Response.ok("Metrics displayed in console").build();
    }
}
