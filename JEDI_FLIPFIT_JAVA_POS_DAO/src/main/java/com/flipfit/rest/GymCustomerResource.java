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

    @GET
    @Path("/centers")
    public Response viewCenters(@QueryParam("email") String email, @QueryParam("password") String password) {
        if (email == null || password == null)
            return Response.status(401).entity("Auth required").build();
        return Response.ok(customerService.viewCentres(email, password)).build();
    }

    @GET
    @Path("/bookings")
    public Response viewBookings(@QueryParam("email") String email, @QueryParam("password") String password) {
        if (email == null || password == null)
            return Response.status(401).entity("Auth required").build();
        return Response.ok(customerService.viewBookedSlots(email, password)).build();
    }

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
