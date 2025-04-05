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

import com.emma.model.Event;
import com.emma.service.EventService;


public class SearchController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private EventService eventService;
    
    public SearchController() {
        super();
        eventService = new EventService();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Handle search form display
            if (request.getParameter("q") == null && request.getParameter("type") == null &&
                request.getParameter("location") == null && request.getParameter("date") == null) {
                
                // Get all event types for the search form
                List<String> eventTypes = eventService.getAllEventTypes();
                request.setAttribute("eventTypes", eventTypes);
                
                // Get all locations for the search form
                List<String> locations = eventService.getAllLocations();
                request.setAttribute("locations", locations);
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/events/search.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            // Process search request
            searchEvents(request, response);
            
        } catch (SQLException | ParseException ex) {
            throw new ServletException(ex);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    private void searchEvents(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException, ParseException {
        String keyword = request.getParameter("q");
        String type = request.getParameter("type");
        String location = request.getParameter("location");
        String dateStr = request.getParameter("date");
        
        Date date = null;
        if (dateStr != null && !dateStr.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                // Invalid date format, ignore date filter
            }
        }
        
        List<Event> searchResults = eventService.searchEvents(keyword, type, location, date);
        request.setAttribute("searchResults", searchResults);
        
        // For form filters
        List<String> eventTypes = eventService.getAllEventTypes();
        request.setAttribute("eventTypes", eventTypes);
        
        List<String> locations = eventService.getAllLocations();
        request.setAttribute("locations", locations);
        
        // Set search parameters for maintaining state
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedType", type);
        request.setAttribute("selectedLocation", location);
        request.setAttribute("selectedDate", dateStr);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/events/search.jsp");
        dispatcher.forward(request, response);
    }
}