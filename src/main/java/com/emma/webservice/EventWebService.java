package com.emma.webservice;

import com.emma.model.Event;
import com.emma.service.EventService;
import com.emma.service.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * RESTful Web Service for Event operations
 */
@Path("/events")
public class EventWebService {

    private final EventService eventService;
    private final Gson gson;

    /**
     * Constructor using ServiceFactory to get EventService
     */
    public EventWebService() {
        this.eventService = ServiceFactory.getEventService();
        this.gson = new Gson();
    }

    /**
     * Get upcoming events
     * @return JSON response with upcoming events
     */
    @GET
    @Path("/api/upcoming")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUpcomingEvents() {
        try {
            List<Event> upcomingEvents = eventService.getUpcomingEvents();
            
            // Create a custom JSON response
            JsonObject responseJson = new JsonObject();
            JsonArray eventsArray = new JsonArray();
            
            // Convert events to a more consistent JSON format
            for (Event event : upcomingEvents) {
                JsonObject eventJson = new JsonObject();
                
                // Safely add event properties
                eventJson.addProperty("id", event.getId());
                eventJson.addProperty("name", event.getName() != null ? event.getName() : "");
                eventJson.addProperty("description", event.getDescription() != null ? event.getDescription() : "");
                eventJson.addProperty("type", event.getType() != null ? event.getType() : "");
                eventJson.addProperty("location", event.getLocation() != null ? event.getLocation() : "");
                
                // Handle date formatting safely
                if (event.getEventDate() != null) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                        eventJson.addProperty("eventDate", sdf.format(event.getEventDate()));
                    } catch (Exception e) {
                        // If formatting fails, use a default value
                        eventJson.addProperty("eventDate", "");
                    }
                } else {
                    eventJson.addProperty("eventDate", "");
                }
                
                // Add other safe properties
                eventJson.addProperty("capacity", event.getCapacity());
                eventJson.addProperty("attendeeCount", event.getAttendeeCount());
                
                eventsArray.add(eventJson);
            }
            
            // Set the events array in the response
            responseJson.add("events", eventsArray);
            
            // Return response with 200 OK status
            return Response.status(Response.Status.OK)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(gson.toJson(responseJson))
                    .build();
        } catch (Exception e) {
            // Log the error
            System.err.println("Error fetching upcoming events: " + e.getMessage());
            e.printStackTrace();
            
            // Return an empty events array instead of an error
            JsonObject errorResponse = new JsonObject();
            errorResponse.add("events", new JsonArray());
            
            return Response.status(Response.Status.OK)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(gson.toJson(errorResponse))
                    .build();
        }
    }

    // Remaining methods stay the same
}