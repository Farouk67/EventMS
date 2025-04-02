package com.emma.webservice;

import com.emma.model.RSVP;
import com.emma.model.Event;
import com.emma.service.RSVPService;
import com.emma.service.EventService;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * RESTful Web Service for RSVP operations
 */
@Path("/rsvps")
public class RSVPWebService {

    private RSVPService rsvpService = new RSVPService();
    private EventService eventService = new EventService();
    private Gson gson = new Gson();

    /**
     * Get all RSVPs for a user
     * @return JSON response with user's RSVPs
     */
    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserRSVPs(@Context HttpServletRequest request) {
        try {
            // Check if user is authenticated
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            if (userId == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"User must be logged in to view RSVPs\"}")
                        .build();
            }

            // Use the findRSVPsByUserId method from RSVPService
            List<RSVP> rsvps = rsvpService.findRSVPsByUserId(userId);
            return Response.status(Response.Status.OK)
                    .entity(gson.toJson(rsvps))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Get all RSVPs for an event
     * @param eventId the event ID
     * @return JSON response with event's RSVPs
     */
    @GET
    @Path("/event/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventRSVPs(@PathParam("eventId") int eventId, @Context HttpServletRequest request) {
        try {
            // Check if user is authenticated
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            if (userId == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"User must be logged in to view RSVPs\"}")
                        .build();
            }

            // Check if user is the organizer
            // Use the getEvent method from EventService
            Event event = eventService.getEvent(eventId);
            if (event == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Event not found\"}")
                        .build();
            }

            // Check if the user is the organizer of the event
            if (event.getOrganizerId() == null || !event.getOrganizerId().equals(userId)) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("{\"error\": \"Only the event organizer can view all RSVPs\"}")
                        .build();
            }

            // Use the findRSVPsByEventId method from RSVPService
            List<RSVP> rsvps = rsvpService.findRSVPsByEventId(eventId);
            return Response.status(Response.Status.OK)
                    .entity(gson.toJson(rsvps))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Create a new RSVP
     * @param rsvpJson JSON representation of the RSVP
     * @return JSON response with created RSVP
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRSVP(String rsvpJson, @Context HttpServletRequest request) {
        try {
            // Check if user is authenticated
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            if (userId == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"User must be logged in to create RSVPs\"}")
                        .build();
            }

            RSVP rsvp = gson.fromJson(rsvpJson, RSVP.class);
            rsvp.setUserId(userId);
            
            // Check if event exists
            Event event = eventService.getEvent(rsvp.getEventId());
            if (event == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Event not found\"}")
                        .build();
            }
            
            // Check if user already RSVPed for this event
            if (rsvpService.hasUserRSVPed(userId, rsvp.getEventId())) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"error\": \"User already RSVPed for this event\"}")
                        .build();
            }
            
            // Create the RSVP and get the ID
            int newRsvpId = rsvpService.createRSVP(rsvp);
            RSVP createdRSVP = rsvpService.findRSVPById(newRsvpId);
            
            return Response.status(Response.Status.CREATED)
                    .entity(gson.toJson(createdRSVP))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Update an existing RSVP
     * @param id the RSVP ID
     * @param rsvpJson JSON representation of the updated RSVP
     * @return JSON response with updated RSVP
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRSVP(@PathParam("id") int id, String rsvpJson, @Context HttpServletRequest request) {
        try {
            // Check if user is authenticated
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            if (userId == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"User must be logged in to update RSVPs\"}")
                        .build();
            }

            RSVP existingRSVP = rsvpService.findRSVPById(id);
            if (existingRSVP == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"RSVP not found\"}")
                        .build();
            }

            // Check if user owns this RSVP
            if (existingRSVP.getUserId() != userId) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("{\"error\": \"User can only update their own RSVPs\"}")
                        .build();
            }

            RSVP updatedRSVP = gson.fromJson(rsvpJson, RSVP.class);
            updatedRSVP.setId(id);
            updatedRSVP.setUserId(userId);
            updatedRSVP.setEventId(existingRSVP.getEventId()); // Cannot change event
            
            rsvpService.update(updatedRSVP);
            
            return Response.status(Response.Status.OK)
                    .entity(gson.toJson(updatedRSVP))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Delete an RSVP
     * @param id the RSVP ID
     * @return confirmation response
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRSVP(@PathParam("id") int id, @Context HttpServletRequest request) {
        try {
            // Check if user is authenticated
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            if (userId == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"User must be logged in to delete RSVPs\"}")
                        .build();
            }

            RSVP existingRSVP = rsvpService.findRSVPById(id);
            if (existingRSVP == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"RSVP not found\"}")
                        .build();
            }

            // Check if user owns this RSVP or is the event organizer
            Event event = eventService.getEvent(existingRSVP.getEventId());
            if (existingRSVP.getUserId() != userId && (event.getOrganizerId() == null || event.getOrganizerId() != userId)) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("{\"error\": \"User can only delete their own RSVPs or RSVPs for their events\"}")
                        .build();
            }

            rsvpService.deleteRSVP(id);
            
            return Response.status(Response.Status.OK)
                    .entity("{\"message\": \"RSVP deleted successfully\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Get RSVP count for an event
     * @param eventId the event ID
     * @return JSON response with RSVP count
     */
    @GET
    @Path("/count/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRSVPCount(@PathParam("eventId") int eventId) {
        try {
            int count = rsvpService.countRSVPsByEventId(eventId);
            return Response.status(Response.Status.OK)
                    .entity("{\"count\": " + count + "}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Check if a user has RSVPed for an event
     * @param eventId the event ID
     * @return JSON response with RSVP status
     */
    @GET
    @Path("/check/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkRSVP(@PathParam("eventId") int eventId, @Context HttpServletRequest request) {
        try {
            // Check if user is authenticated
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            if (userId == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"User must be logged in to check RSVP status\"}")
                        .build();
            }

            RSVP rsvp = rsvpService.findRSVPByUserAndEvent(userId, eventId);
            boolean hasRSVPed = (rsvp != null);
            
            return Response.status(Response.Status.OK)
                    .entity("{\"hasRSVPed\": " + hasRSVPed + ", \"rsvp\": " + (hasRSVPed ? gson.toJson(rsvp) : "null") + "}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}