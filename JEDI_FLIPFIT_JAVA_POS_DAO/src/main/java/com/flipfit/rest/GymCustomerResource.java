package com.flipfit.rest;

import com.flipfit.business.GymCustomerService;
import com.flipfit.bean.GymCustomer;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * The Class GymCustomerResource.
 *
 * @author Ananya
 * @ClassName "GymCustomerResource"
 */
@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GymCustomerResource {
    private final GymCustomerService customerService = new GymCustomerService();

    /**
     * Registers a new gym customer.
     *
     * @param customer the customer details (name, email, password, contact, address)
     * @return 201 on success
     */
    @POST
    @Path("/register")
    public Response register(GymCustomer customer) {
        customerService.registerCustomer(
                customer.getFullName(),
                customer.getEmail(),
                customer.getPassword(),
                customer.getPhoneNumber(),
                customer.getCity(),
                customer.getState(),
                customer.getPincode());
        return Response.status(Response.Status.CREATED).entity("Customer registered successfully").build();
    }

    /**
     * Returns list of centres visible to the authenticated customer.
     *
     * @param email    customer email
     * @param password customer password
     * @return 200 with list of centres, 401 if auth missing
     */
    @GET
    @Path("/centers")
    public Response viewCenters(@QueryParam("email") String email, @QueryParam("password") String password) {
        if (email == null || password == null)
            return Response.status(401).entity("Auth required").build();
        return Response.ok(customerService.viewCentres(email, password)).build();
    }

    /**
     * Returns the customer's bookings.
     *
     * @param email    customer email
     * @param password customer password
     * @return 200 with list of bookings, 401 if auth missing
     */
    @GET
    @Path("/bookings")
    public Response viewBookings(@QueryParam("email") String email, @QueryParam("password") String password) {
        if (email == null || password == null)
            return Response.status(401).entity("Auth required").build();
        return Response.ok(customerService.viewBookedSlots(email, password)).build();
    }

    /**
     * Books a slot for the authenticated customer.
     *
     * @param slotId   the slot/availability ID to book
     * @param email    customer email
     * @param password customer password
     * @return 200 with booking details, 400 on failure, 401 if auth missing
     */
    @POST
    @Path("/book")
    public Response bookSlot(@QueryParam("slotId") int slotId, @QueryParam("email") String email,
            @QueryParam("password") String password) {
        if (email == null || password == null)
            return Response.status(401).entity("Auth required").build();
        try {
            return Response.ok(customerService.bookSlot(slotId, email, password)).build();
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    /**
     * Cancels a booking for the authenticated customer.
     *
     * @param bookingId the booking ID to cancel
     * @param email     customer email
     * @param password  customer password
     * @return 200 on success, 400 on failure, 401 if auth missing
     */
    @DELETE
    @Path("/cancel/{bookingId}")
    public Response cancelBooking(@PathParam("bookingId") int bookingId, @QueryParam("email") String email,
            @QueryParam("password") String password) {
        if (email == null || password == null)
            return Response.status(401).entity("Auth required").build();
        try {
            customerService.cancelBooking(bookingId, email, password);
            return Response.ok("Booking cancelled").build();
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    /**
     * Returns available slots for a centre.
     *
     * @param centreId the centre ID
     * @return 200 with list of available slots
     */
    @GET
    @Path("/slots/{centreId}")
    public Response viewSlots(@PathParam("centreId") int centreId) {
        // Assuming public view for slots, or add auth if needed.
        // Keeping public for now as per minimal change, but
        // GymCustomerService.viewAvailableSlots is missing in my view?
        // I will add it to service.
        return Response.ok(customerService.viewAvailableSlots(centreId)).build();
    }
}
