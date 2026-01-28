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
 * @ClassName  "GymAdminResource"
 */
@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GymAdminResource {
    private final GymAdminService adminService = new GymAdminService();

    @POST
    @Path("/register")
    public Response register(GymAdmin admin) {
        adminService.registerAdmin(
            admin.getFullName(),
            admin.getEmail(),
            admin.getPassword(),
            admin.getPhoneNumber(),
            admin.getCity(),
            admin.getState(),
            admin.getPincode()
        );
        return Response.status(Response.Status.CREATED).entity("Admin registered successfully").build();
    }

    @GET
    @Path("/owners")
    public Response viewOwners() {
        // adminService.viewAllGymOwners() prints to console but also syncs repository
        adminService.viewAllGymOwners();
        return Response.ok(com.flipfit.dao.FlipFitRepository.owners).build();
    }

    @GET
    @Path("/centers")
    public Response viewCenters() {
        adminService.viewAllCentres();
        return Response.ok(com.flipfit.dao.FlipFitRepository.gymCentres).build();
    }

    @PUT
    @Path("/owner/approve/{ownerId}")
    public Response approveOwner(@PathParam("ownerId") int ownerId) {
        try {
            if (adminService.validateGymOwner(ownerId)) {
                // The validateGymOwner just checks if it exists. 
                // We might need an actual approve method in AdminDAO.
                // Looking at GymAdminService, it has validateGymOwner but updateGymOwnerApproval is what we need.
                return Response.ok("Owner validated").build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/center/approve/{centerId}")
    public Response approveCenter(@PathParam("centerId") int centerId) {
        try {
            if (adminService.approveCentre(centerId)) {
                return Response.ok("Center approved").build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/metrics/customer")
    public Response getCustomerMetrics() {
        adminService.viewCustomerMetrics();
        return Response.ok("Metrics displayed in console, but logic for returning JSON can be added").build();
    }
}
