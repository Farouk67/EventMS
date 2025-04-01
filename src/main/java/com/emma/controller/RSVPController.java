package com.emma.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.emma.model.RSVP;
import com.emma.service.RSVPService;
import com.emma.service.EventService;

@WebServlet("/rsvp/*")
public class RSVPController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private RSVPService rsvpService;
    private EventService eventService;
    
    public RSVPController() {
        super();
        rsvpService = new RSVPService();
        eventService = new EventService();
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
            response.sendRedirect(request.getContextPath() + "/login");
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
        
        // Update event attendee count
        eventService.incrementAttendeeCount(eventId);
        
        response.sendRedirect(request.getContextPath() + "/events/details?id=" + eventId);
    }
    
    private void cancelRSVP(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
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
        
        // Update event attendee count
        eventService.decrementAttendeeCount(eventId);
        
        response.sendRedirect(request.getContextPath() + "/events/details?id=" + eventId);
    }
}