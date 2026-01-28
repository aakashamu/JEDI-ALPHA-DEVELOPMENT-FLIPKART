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
 * @ClassName  "GymCustomerResource"
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
            customer.getPincode()
        );
        return Response.status(Response.Status.CREATED).entity("Customer registered successfully").build();
    }

    @GET
    @Path("/profile/{email}")
    public Response viewProfile(@PathParam("email") String email) {
        // Use an instance of UserDAO
        com.flipfit.dao.UserDAO userDAO = new com.flipfit.dao.UserDAO();
        com.flipfit.bean.User user = userDAO.getUserDetails(email);
        if (user instanceof com.flipfit.bean.GymCustomer) {
            return Response.ok(user).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/centers")
    public Response viewCenters() {
        return Response.ok(customerService.viewCentres()).build();
    }

    @GET
    @Path("/bookings/{customerId}")
    public Response viewBookings(@PathParam("customerId") int customerId) {
        com.flipfit.dao.BookingDAOImpl bookingDAO = new com.flipfit.dao.BookingDAOImpl();
        return Response.ok(bookingDAO.getCustomerBookings(customerId)).build();
    }

    @POST
    @Path("/book")
    public Response bookSlot(@QueryParam("availabilityId") int availabilityId, @QueryParam("email") String email) {
        try {
            // We set the logged in user so the service can pick it up
            com.flipfit.business.UserService.setCurrentLoggedInUser(email);
            com.flipfit.bean.Booking booking = customerService.bookSlot(availabilityId);
            return Response.status(Response.Status.CREATED).entity(booking).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/booking/{id}")
    public Response cancelBooking(@PathParam("id") int bookingId, @QueryParam("email") String email) {
        try {
            com.flipfit.business.UserService.setCurrentLoggedInUser(email);
            if (customerService.cancelBooking(bookingId)) {
                return Response.ok("Booking cancelled").build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    @GET
    @Path("/slots/{centreId}")
    public Response viewAvailableSlots(@PathParam("centreId") int centreId) {
        return Response.ok(customerService.viewAvailableSlots(centreId)).build();
    }

    @GET
    @Path("/waitlist/{bookingId}")
    public Response checkWaitlist(@PathParam("bookingId") int bookingId) {
        com.flipfit.business.WaitListService waitlistService = new com.flipfit.business.WaitListService();
        int position = waitlistService.getWaitListPosition(bookingId);
        if (position > 0) {
            return Response.ok("Your position in waitlist is: " + position).build();
        } else {
            return Response.ok("Booking ID " + bookingId + " is not in the waitlist.").build();
        }
    }
}

