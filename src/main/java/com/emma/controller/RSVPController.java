package com.emma.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.emma.model.Event;
import com.emma.model.RSVP;
import com.emma.service.EventService;
import com.emma.service.RSVPService;
import com.emma.service.ServiceFactory;
import com.emma.util.DBConnectionPool;

public class RSVPController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private RSVPService rsvpService;
    private EventService eventService;
    
    public RSVPController() {
        super();
        // Use the ServiceFactory to get service instances
        this.rsvpService = ServiceFactory.getRsvpService();
        this.eventService = ServiceFactory.getEventService();
    }
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Double-check that services are initialized
        if (rsvpService == null) {
            rsvpService = ServiceFactory.getRsvpService();
        }
        if (eventService == null) {
            eventService = ServiceFactory.getEventService();
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        try {
            if (action == null) {
                response.sendRedirect(request.getContextPath() + "/events/list");
                return;
            }
            
            switch (action) {
                case "/add":
                    addRSVP(request, response);
                    break;
                case "/cancel":
                    cancelRSVP(request, response);
                    break;
                case "/manage":
                    manageRSVPs(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/events/list");
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    private void addRSVP(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/users/login");
            return;
        }
        
        int eventId = Integer.parseInt(request.getParameter("eventId"));
        
        // Check if already RSVP'd
        if (rsvpService.hasUserRSVPed(userId, eventId)) {
            response.sendRedirect(request.getContextPath() + "/events/details?id=" + eventId);
            return;
        }
        
        // Create RSVP
        RSVP rsvp = new RSVP(0, userId, eventId);
        rsvpService.createRSVP(rsvp);
        
        response.sendRedirect(request.getContextPath() + "/events/details?id=" + eventId);
    }
    
    private void cancelRSVP(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/users/login");
            return;
        }
        
        int eventId = Integer.parseInt(request.getParameter("eventId"));
        
        // Check if RSVP exists
        if (!rsvpService.hasUserRSVPed(userId, eventId)) {
            response.sendRedirect(request.getContextPath() + "/events/details?id=" + eventId);
            return;
        }
        
        // Delete RSVP
        rsvpService.deleteRSVP(userId, eventId);
        
        response.sendRedirect(request.getContextPath() + "/events/details?id=" + eventId);
    }
    
    private void manageRSVPs(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        // Get the logged-in user
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/users/login");
            return;
        }
        
        // Get the event ID
        int eventId = Integer.parseInt(request.getParameter("eventId"));
        
        // Get the event
        Event event = eventService.getEvent(eventId);
        
        if (event == null) {
            // Event not found
            response.sendRedirect(request.getContextPath() + "/events/list");
            return;
        }
        
        // Check if the user is the organizer of the event
        if (event.getOrganizerId() != userId) {
            // If not the organizer, redirect to event details
            response.sendRedirect(request.getContextPath() + "/events/details?id=" + eventId);
            return;
        }
        
        // Get RSVPs for this event
        List<RSVP> rsvps = new ArrayList<>();
        // Also create a map to store usernames by user ID
        Map<Integer, String> usernames = new HashMap<>();
        
        try {
            Connection conn = DBConnectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT r.*, u.username FROM rsvp r " +
                "JOIN user u ON r.user_id = u.id " +
                "WHERE r.event_id = ?"
            );
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                RSVP rsvp = new RSVP();
                rsvp.setId(rs.getInt("id"));
                int rsvpUserId = rs.getInt("user_id");
                rsvp.setUserId(rsvpUserId);
                rsvp.setEventId(rs.getInt("event_id"));
                
                // Store username in the map
                usernames.put(rsvpUserId, rs.getString("username"));
                
                // Set status (default to "Confirmed" if null)
                String status = rs.getString("status");
                rsvp.setStatus(status != null ? status : "Confirmed");
                
                // Set response date if available
                if (rs.getTimestamp("responded_at") != null) {
                    rsvp.setRespondedAt(rs.getTimestamp("responded_at"));
                }
                
                // Set notes if available
                if (rs.getString("notes") != null) {
                    rsvp.setNotes(rs.getString("notes"));
                }
                
                rsvps.add(rsvp);
            }
            
            // Close resources
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.err.println("Error getting RSVPs: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Set attributes for the JSP
        request.setAttribute("event", event);
        request.setAttribute("rsvps", rsvps);
        request.setAttribute("usernames", usernames); // Pass usernames separately
        
        // Forward to the manage JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/rsvp/manage.jsp");
        dispatcher.forward(request, response);
    }
}