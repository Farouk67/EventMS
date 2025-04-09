package com.emma.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.emma.model.Event;
import com.emma.service.EventService;
import com.emma.service.ServiceFactory;
import com.google.gson.Gson;


public class EventController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private EventService eventService;
    
    public EventController() {
        super();
        // Use the ServiceFactory to get the EventService instance
        eventService = ServiceFactory.getEventService();
        System.out.println("EventController constructor - eventService initialized: " + (eventService != null));
    }
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Double-check that the service is initialized
        if (eventService == null) {
            eventService = ServiceFactory.getEventService();
            System.out.println("EventController init - eventService initialized: " + (eventService != null));
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        // Debug logging
        System.out.println("EventController received request: " + request.getRequestURI());
        System.out.println("Path info: " + action);
        System.out.println("Query string: " + request.getQueryString());
        
        try {
            if (action == null) {
                listEvents(request, response);
            } else {
                // Update the switch statement in doGet to include the new endpoint
switch (action) {
    case "/list":
        listEvents(request, response);
        break;
    case "/new":
        showNewForm(request, response);
        break;
    case "/insert":
        insertEvent(request, response);
        break;
    case "/delete":
        deleteEvent(request, response);
        break;
    case "/edit":
        showEditForm(request, response);
        break;
    case "/update":
        updateEvent(request, response);
        break;
    case "/details":
        showEventDetails(request, response);
        break;
    case "/byType":
        listEventsByType(request, response);
        break;
    case "/api/upcoming":
        getUpcomingEvents(request, response);
        break;
    case "/predict-type":
        predictEventType(request, response);
        break;
    case "/test":
        testEndpoint(request, response);
        break;
    default:
        listEvents(request, response);
        break;
}
            }
        } catch (Exception ex) {
            // Log the exception
            System.err.println("Error in EventController: " + ex.getMessage());
            ex.printStackTrace();
            
            // Set error message and forward to error page
            request.setAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());
            
            // Check if error.jsp exists before forwarding
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/common/error.jsp");
            if (dispatcher == null) {
                // If not found, try a direct HTML response
                response.setContentType("text/html");
                response.getWriter().println("<html><body><h1>Error</h1><p>An unexpected error occurred: " + 
                                             ex.getMessage() + "</p><p><a href='" + 
                                             request.getContextPath() + "'>Return to home</a></p></body></html>");
                return;
            }
            
            dispatcher.forward(request, response);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    private void testEndpoint(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("<html><body>");
        response.getWriter().println("<h1>Test Endpoint Working</h1>");
        response.getWriter().println("<p>This confirms your EventController is properly handling requests.</p>");
        response.getWriter().println("</body></html>");
    }
    
    private void listEvents(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        System.out.println("Executing listEvents method");
        
        try {
            List<Event> events = eventService.getAllEvents();
            System.out.println("Retrieved " + (events != null ? events.size() : "null") + " events");
            
            request.setAttribute("events", events);
            
            // Also get all event types for navigation
            List<String> eventTypes = eventService.getAllEventTypes();
            System.out.println("Retrieved " + (eventTypes != null ? eventTypes.size() : "null") + " event types");
            
            request.setAttribute("eventTypes", eventTypes);
            
            // Check if the JSP exists
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/events/list.jsp");
            if (dispatcher == null) {
                System.err.println("list.jsp not found in expected location");
                response.setContentType("text/html");
                response.getWriter().println("<html><body><h1>Error</h1><p>Required JSP file not found.</p></body></html>");
                return;
            }
            
            dispatcher.forward(request, response);
        } catch (Exception e) {
            System.err.println("Error in listEvents method: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    private void listEventsByType(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        System.out.println("Executing listEventsByType method");
        
        String eventType = request.getParameter("type");
        System.out.println("Event type parameter: " + eventType);
        
        List<Event> events = eventService.getEventsByType(eventType);
        request.setAttribute("events", events);
        request.setAttribute("currentType", eventType);
        
        // Also get all event types for navigation
        List<String> eventTypes = eventService.getAllEventTypes();
        request.setAttribute("eventTypes", eventTypes);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/events/list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        System.out.println("Executing showNewForm method");
        
        List<String> eventTypes = eventService.getAllEventTypes();
        request.setAttribute("eventTypes", eventTypes);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/events/create.jsp");
        dispatcher.forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        System.out.println("Executing showEditForm method");
        
        int id = Integer.parseInt(request.getParameter("id"));
        Event existingEvent = eventService.getEvent(id);
        
        List<String> eventTypes = eventService.getAllEventTypes();
        request.setAttribute("eventTypes", eventTypes);
        
        request.setAttribute("event", existingEvent);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/events/edit.jsp");
        dispatcher.forward(request, response);
    }
    
    private void showEventDetails(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        System.out.println("Executing showEventDetails method");
        
        try {
            // Validate ID parameter
            String idParam = request.getParameter("id");
            System.out.println("Event ID parameter: " + idParam);
            
            if (idParam == null || idParam.trim().isEmpty()) {
                handleError(request, response, "Event ID is missing or empty. Please provide a valid event ID.");
                return;
            }
            
            int id;
            try {
                id = Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                handleError(request, response, "Invalid event ID format. Please provide a numeric ID.");
                return;
            }
            
            // Get event from service
            Event event = null;
            try {
                event = eventService.getEvent(id);
                System.out.println("Retrieved event: " + (event != null ? event.getName() : "null"));
            } catch (Exception e) {
                handleError(request, response, "Error retrieving event: " + e.getMessage());
                return;
            }
            
            // Check if event exists
            if (event == null) {
                handleError(request, response, "Event not found. The event with ID " + id + " does not exist.");
                return;
            }
            
            // Set event in request and forward to JSP
            request.setAttribute("event", event);
            
            // Check if user has RSVPed to this event
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("userId") != null) {
                Integer userId = (Integer) session.getAttribute("userId");
                boolean hasRSVPed = false;
                
                // Get RSVP service to check
                try {
                    hasRSVPed = ServiceFactory.getRsvpService().hasUserRSVPed(userId, id);
                } catch (Exception e) {
                    System.err.println("Error checking RSVP status: " + e.getMessage());
                    // Continue without setting RSVP status
                }
                
                request.setAttribute("hasRSVPed", hasRSVPed);
            }
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/events/view.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error in showEventDetails: " + e.getMessage());
            e.printStackTrace();
            
            // Handle unexpected errors
            handleError(request, response, "An unexpected error occurred: " + e.getMessage());
        }
    }
    
    private void insertEvent(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ParseException {
        System.out.println("Executing insertEvent method");
        
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String dateStr = request.getParameter("date");
        String location = request.getParameter("location");
        String description = request.getParameter("description");
        int capacity = Integer.parseInt(request.getParameter("capacity"));

        // Data validation
        validateEventData(name, dateStr, location);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateStr);

        HttpSession session = request.getSession();
        int userId = (Integer) session.getAttribute("userId");

        // Create event with the correct constructor parameters
        Event newEvent = new Event(0, name, description, type, location, date, 0, capacity, false, 0.0, userId);
        eventService.createEvent(newEvent);
        response.sendRedirect("list");
    }
    
    private void updateEvent(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ParseException {
        System.out.println("Executing updateEvent method");
        
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String dateStr = request.getParameter("date");
        String location = request.getParameter("location");
        String description = request.getParameter("description");
        int capacity = Integer.parseInt(request.getParameter("capacity"));

        // Data validation
        validateEventData(name, dateStr, location);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateStr);

        // Get the current attendance count to preserve it
        Event existingEvent = eventService.getEvent(id);
        int attendees = existingEvent.getAttendeeCount();
        Integer organizerId = existingEvent.getOrganizerId();

        // Create event with the correct constructor parameters
        Event event = new Event(id, name, description, type, location, date, attendees, capacity, false, 0.0, organizerId);
        eventService.updateEvent(event);
        response.sendRedirect("list");
    }
    
    private void deleteEvent(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        System.out.println("Executing deleteEvent method");
        
        int id = Integer.parseInt(request.getParameter("id"));
        eventService.deleteEvent(id);
        response.sendRedirect("list");
    }
    
    private void getUpcomingEvents(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        System.out.println("Executing getUpcomingEvents method");
        
        try {
            List<Event> upcomingEvents = eventService.getUpcomingEvents();
            System.out.println("Retrieved " + (upcomingEvents != null ? upcomingEvents.size() : "null") + " upcoming events");
            
            // Log each event for debugging
            if (upcomingEvents != null) {
                for (Event event : upcomingEvents) {
                    System.out.println("Event ID: " + event.getId() + ", Name: " + event.getName());
                }
            }
            
            // Convert to JSON
            Gson gson = new Gson();
            String jsonEvents = gson.toJson(upcomingEvents);
            
            // Set response headers
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            // Write JSON response
            response.getWriter().write(jsonEvents);
        } catch (Exception ex) {
            // Log the error
            System.err.println("Error in getUpcomingEvents: " + ex.getMessage());
            ex.printStackTrace();
            
            // Send error response
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Failed to fetch upcoming events\"}");
        }
    }
    
    private void validateEventData(String name, String dateStr, String location) throws ParseException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Event name cannot be empty");
        }
        
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Event location cannot be empty");
        }
        
        // Validate date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        sdf.parse(dateStr);
    }
    
    
    /**
     * Helper method to handle errors in a consistent way
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage) 
            throws ServletException, IOException {
        // Log the error
        System.err.println("Event Controller Error: " + errorMessage);
        
        // Set error message in request
        request.setAttribute("errorMessage", errorMessage);
        
        // First try to use a common error JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/common/error.jsp");
        
        if (dispatcher == null) {
            // If error.jsp is not found, try a different path
            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/common/error-500.jsp");
            
            if (dispatcher == null) {
                // If that still doesn't work, send a direct HTML response
                response.setContentType("text/html");
                response.getWriter().println("<html><body><h1>Error</h1><p>" + 
                                             errorMessage + "</p><p><a href='" + 
                                             request.getContextPath() + "'>Return to home</a></p></body></html>");
                return;
            }
        }
        
        // Forward to the appropriate error page
        dispatcher.forward(request, response);
    }
    private void predictEventType(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    try {
        // Get form parameters
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        
        System.out.println("Predicting event type for - Name: " + name + ", Location: " + location);
        
        // Create temporary event
        Event tempEvent = new Event();
        tempEvent.setName(name);
        tempEvent.setDescription(description);
        tempEvent.setLocation(location);
        
        // Get prediction using the EventService
        String predictedType = eventService.getRecommendedEventType(tempEvent);
        
        // Return JSON response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"predictedType\": \"" + predictedType + "\"}");
    } catch (Exception e) {
        System.err.println("Error in predictEventType: " + e.getMessage());
        e.printStackTrace();
        
        // Return error JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"Failed to predict event type\", \"message\": \"" + e.getMessage() + "\"}");
    }
}

}