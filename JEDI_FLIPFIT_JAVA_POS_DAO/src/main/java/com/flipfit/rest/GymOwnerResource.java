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
 * @ClassName "GymOwnerResource"
 */
@Path("/owner")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GymOwnerResource {
    private final GymOwnerService ownerService = new GymOwnerService();

    /**
     * Registers a new gym owner (with PAN, Aadhaar, GSTIN).
     *
     * @param owner the owner details
     * @return 201 on success
     */
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
                owner.getGSTIN());
        return Response.status(Response.Status.CREATED).entity("Owner registered successfully").build();
    }

    /**
     * Adds a new centre for the authenticated owner.
     *
     * @param centre   the centre details
     * @param email    owner email
     * @param password owner password
     * @return 201 on success, 401 if credentials missing
     */
    @POST
    @Path("/center")
    public Response addCenter(com.flipfit.bean.GymCentre centre,
            @QueryParam("email") String email,
            @QueryParam("password") String password) {
        if (email == null || password == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Credentials required").build();
        }
        ownerService.addCentre(centre, email, password);
        return Response.status(Response.Status.CREATED).entity("Center added successfully (if credentials valid)")
                .build();
    }

    /**
     * Returns centres owned by the authenticated owner.
     *
     * @param ownerId  the owner user ID (path)
     * @param email    owner email
     * @param password owner password
     * @return 200 with list of centres, 401 if credentials missing
     */
    @GET
    @Path("/centers/{ownerId}")
    public Response viewMyCenters(@PathParam("ownerId") int ownerId,
            @QueryParam("email") String email,
            @QueryParam("password") String password) {
        if (email == null || password == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Credentials required").build();
        }
        // Validate if the ownerId matches the authenticated email if strict
        // But for now, just authenticate
        return Response.ok(ownerService.viewMyCentres(email, password)).build();
    }

    /**
     * Adds slots for an existing centre.
     *
     * @param centreId  the centre ID
     * @param numSlots  number of slots to create
     * @param capacity  capacity per slot
     * @param email     owner email
     * @param password  owner password
     * @return 201 on success, 401 if credentials missing
     */
    @POST
    @Path("/slot")
    public Response addSlot(@QueryParam("centreId") int centreId,
            @QueryParam("numSlots") int numSlots,
            @QueryParam("capacity") int capacity,
            @QueryParam("email") String email,
            @QueryParam("password") String password) {
        if (email == null || password == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Credentials required").build();
        }
        ownerService.setupSlotsForExistingCentre(centreId, numSlots, capacity, email, password);
        return Response.status(Response.Status.CREATED).entity("Slots setup process initiated").build();
    }
}
