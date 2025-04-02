package com.emma.webservice;

import com.emma.model.Event;
import com.emma.service.EventService;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * RESTful Web Service for Event operations
 */
@Path("/events")
public class EventWebService {

    private EventService eventService = new EventService();
    private Gson gson = new Gson();

    /**
     * Get all events
     * @return JSON response with all events
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEvents() {
        try {
            List<Event> events = eventService.getAllEvents();
            return Response.status(Response.Status.OK)
                    .entity(gson.toJson(events))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Get events by type
     * @param typeId the event type ID
     * @return JSON response with filtered events
     */
    @GET
    @Path("/type/{typeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventsByType(@PathParam("typeId") int typeId) {
        try {
            // Fixed: Changed from int to String parameter
            List<Event> events = eventService.getEventsByType(String.valueOf(typeId));
            return Response.status(Response.Status.OK)
                    .entity(gson.toJson(events))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Get an event by ID
     * @param id the event ID
     * @return JSON response with event details
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvent(@PathParam("id") int id) {
        try {
            // Fixed: Using getEvent instead of getEventById
            Event event = eventService.getEvent(id);
            if (event == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Event not found\"}")
                        .build();
            }
            return Response.status(Response.Status.OK)
                    .entity(gson.toJson(event))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Create a new event
     * @param eventJson JSON representation of the event
     * @return JSON response with created event
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEvent(String eventJson, @Context HttpServletRequest request) {
        try {
            // Check if user is authenticated
            if (request.getSession().getAttribute("userId") == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"User must be logged in to create events\"}")
                        .build();
            }

            Event event = gson.fromJson(eventJson, Event.class);
            event.setOrganizerId((Integer) request.getSession().getAttribute("userId"));
            
            // Fixed: Store event and get ID separately
            eventService.createEvent(event);
            int newEventId = event.getId();
            Event createdEvent = eventService.getEvent(newEventId);
            
            return Response.status(Response.Status.CREATED)
                    .entity(gson.toJson(createdEvent))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Update an existing event
     * @param id the event ID
     * @param eventJson JSON representation of the updated event
     * @return JSON response with updated event
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEvent(@PathParam("id") int id, String eventJson, @Context HttpServletRequest request) {
        try {
            // Check if user is authenticated
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            if (userId == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"User must be logged in to update events\"}")
                        .build();
            }

            // Fixed: Using getEvent instead of getEventById
            Event existingEvent = eventService.getEvent(id);
            if (existingEvent == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Event not found\"}")
                        .build();
            }

            // Fixed: Using Integer.equals() instead of primitive comparison
            if (existingEvent.getOrganizerId() == null || !existingEvent.getOrganizerId().equals(userId)) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("{\"error\": \"Only the event organizer can update this event\"}")
                        .build();
            }

            Event updatedEvent = gson.fromJson(eventJson, Event.class);
            updatedEvent.setId(id);
            updatedEvent.setOrganizerId(userId);
            
            eventService.updateEvent(updatedEvent);
            
            return Response.status(Response.Status.OK)
                    .entity(gson.toJson(updatedEvent))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Delete an event
     * @param id the event ID
     * @return confirmation response
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEvent(@PathParam("id") int id, @Context HttpServletRequest request) {
        try {
            // Check if user is authenticated
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            if (userId == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"User must be logged in to delete events\"}")
                        .build();
            }

            // Fixed: Using getEvent instead of getEventById
            Event existingEvent = eventService.getEvent(id);
            if (existingEvent == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Event not found\"}")
                        .build();
            }

            // Fixed: Using Integer.equals() instead of primitive comparison
            if (existingEvent.getOrganizerId() == null || !existingEvent.getOrganizerId().equals(userId)) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("{\"error\": \"Only the event organizer can delete this event\"}")
                        .build();
            }

            eventService.deleteEvent(id);
            
            return Response.status(Response.Status.OK)
                    .entity("{\"message\": \"Event deleted successfully\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Search events
     * @return JSON response with filtered events
     */
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchEvents(
            @QueryParam("keyword") String keyword,
            @QueryParam("location") String location,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @QueryParam("type") Integer typeId) {
        
        try {
            // Fixed: Converting parameters to match method signature
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = startDate != null ? dateFormat.parse(startDate) : null;
            
            List<Event> events = eventService.searchEvents(keyword, location, endDate, parsedDate);
            return Response.status(Response.Status.OK)
                    .entity(gson.toJson(events))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Predict event type based on event details
     * @param eventJson JSON representation of the event
     * @return JSON response with predicted event type
     */
    @POST
    @Path("/predict-type")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response predictEventType(String eventJson) {
        try {
            Event event = gson.fromJson(eventJson, Event.class);
            // Fixed: Use an alternative method or implement it
            String predictedType = eventService.getRecommendedEventType(event);
            
            return Response.status(Response.Status.OK)
                    .entity("{\"predictedType\": \"" + predictedType + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}