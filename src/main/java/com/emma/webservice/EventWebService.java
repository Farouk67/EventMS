package com.emma.webservice;

import com.emma.model.Event;
import com.emma.service.EventService;
import com.emma.service.ServiceFactory;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    @Path("/upcoming")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUpcomingEvents() {
        try {
            List<Event> upcomingEvents = eventService.getUpcomingEvents();
            
            // Option 1: Just return the list directly
            return Response.status(Response.Status.OK)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(gson.toJson(upcomingEvents))
                    .build();
            
            /* Option 2: If you want to use the custom JSON format with JsonObject and JsonArray:
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
            
            return Response.status(Response.Status.OK)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(gson.toJson(responseJson))
                    .build();
            */
            
        } catch (Exception e) {
            // Log the error
            System.err.println("Error fetching upcoming events: " + e.getMessage());
            e.printStackTrace();
            
            // Return an error response
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\": \"Failed to fetch upcoming events\"}")
                    .build();
        }
    }
}