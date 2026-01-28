package com.flipfit.rest;

import com.flipfit.business.GymOwnerService;
import com.flipfit.bean.GymOwner;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * The Class GymOwnerResource.
 *
 * @author Ananya
 * @ClassName  "GymOwnerResource"
 */
@Path("/owner")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GymOwnerResource {
    private final GymOwnerService ownerService = new GymOwnerService();

    @POST
    @Path("/register")
    public Response register(GymOwner owner) {
        ownerService.registerOwner(
            owner.getFullName(),
            owner.getEmail(),
            owner.getPassword(),
            owner.getPhoneNumber(),
            owner.getCity(),
            owner.getState(),
            owner.getPincode(),
            owner.getPAN(),
            owner.getAadhaar(),
            owner.getGSTIN()
        );
        return Response.status(Response.Status.CREATED).entity("Owner registered successfully").build();
    }

    @POST
    @Path("/center")
    public Response addCenter(com.flipfit.bean.GymCentre centre, @QueryParam("email") String email) {
        com.flipfit.business.UserService.setCurrentLoggedInUser(email);
        ownerService.addCentre(centre);
        return Response.status(Response.Status.CREATED).entity("Center added successfully").build();
    }

    @GET
    @Path("/centers/{ownerId}")
    public Response viewMyCenters(@PathParam("ownerId") int ownerId, @QueryParam("email") String email) {
        com.flipfit.business.UserService.setCurrentLoggedInUser(email);
        return Response.ok(ownerService.viewMyCentres()).build();
    }
    @POST
    @Path("/slot")
    public Response addSlot(@QueryParam("centreId") int centreId, @QueryParam("numSlots") int numSlots, @QueryParam("capacity") int capacity, @QueryParam("email") String email) {
        com.flipfit.business.UserService.setCurrentLoggedInUser(email);
        ownerService.setupSlotsForExistingCentre(centreId, numSlots, capacity);
        return Response.status(Response.Status.CREATED).entity("Slots added successfully").build();
    }
}
