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
    }
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Double-check that the service is initialized
        if (eventService == null) {
            eventService = ServiceFactory.getEventService();
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        try {
            if (action == null) {
                listEvents(request, response);
            } else {
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
                    default:
                        listEvents(request, response);
                        break;
                }
            }
        } catch (SQLException | ParseException ex) {
            throw new ServletException(ex);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    private void listEvents(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        List<Event> events = eventService.getAllEvents();
        request.setAttribute("events", events);
        
        // Also get all event types for navigation
        List<String> eventTypes = eventService.getAllEventTypes();
        request.setAttribute("eventTypes", eventTypes);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/event-list.jsp");
        dispatcher.forward(request, response);
    }
    
    private void listEventsByType(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        String eventType = request.getParameter("type");
        List<Event> events = eventService.getEventsByType(eventType);
        request.setAttribute("events", events);
        request.setAttribute("currentType", eventType);
        
        // Also get all event types for navigation
        List<String> eventTypes = eventService.getAllEventTypes();
        request.setAttribute("eventTypes", eventTypes);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/event-list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        List<String> eventTypes = eventService.getAllEventTypes();
        request.setAttribute("eventTypes", eventTypes);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/event-form.jsp");
        dispatcher.forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Event existingEvent = eventService.getEvent(id);
        
        List<String> eventTypes = eventService.getAllEventTypes();
        request.setAttribute("eventTypes", eventTypes);
        
        request.setAttribute("event", existingEvent);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/event-form.jsp");
        dispatcher.forward(request, response);
    }
    
    private void showEventDetails(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Event event = eventService.getEvent(id);
        request.setAttribute("event", event);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/event-details.jsp");
        dispatcher.forward(request, response);
    }
    
    private void insertEvent(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ParseException {
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
        int id = Integer.parseInt(request.getParameter("id"));
        eventService.deleteEvent(id);
        response.sendRedirect("list");
    }
    
    private void getUpcomingEvents(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            List<Event> upcomingEvents = eventService.getUpcomingEvents();
            
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
}