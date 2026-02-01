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

    @GET
    @Path("/owners")
    public Response viewOwners(@QueryParam("email") String email, @QueryParam("password") String password) {
        if (email == null || password == null)
            return Response.status(401).entity("Auth fail").build();
        return Response.ok(adminService.viewAllGymOwners(email, password)).build();
    }

    @GET
    @Path("/centers")
    public Response viewCenters(@QueryParam("email") String email, @QueryParam("password") String password) {
        if (email == null || password == null)
            return Response.status(401).entity("Auth fail").build();
        return Response.ok(adminService.viewAllCentres(email, password)).build();
    }

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

    @GET
    @Path("/metrics/customer")
    public Response getCustomerMetrics(@QueryParam("email") String email, @QueryParam("password") String password) {
        if (email == null || password == null)
            return Response.status(401).entity("Auth fail").build();
        adminService.viewCustomerMetrics(email, password);
        return Response.ok("Metrics displayed in console").build();
    }
}
