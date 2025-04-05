package com.emma.service;

import java.util.Date;
import java.util.List;
import com.emma.model.RSVP;
import com.emma.repository.RSVPRepository;

public class RSVPService {
    
    private RSVPRepository rsvpRepository;
    private EventService eventService;
    
    /**
     * Default constructor - does not initialize EventService
     */
    public RSVPService() {
        this.rsvpRepository = new RSVPRepository();
    }
    
    /**
     * Sets the EventService - to be used after construction to avoid circular dependency
     * 
     * @param eventService The EventService to use
     */
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }
    
    /**
     * Checks if a user has RSVPed to an event
     * 
     * @param userId The user ID
     * @param eventId The event ID
     * @return true if the user has RSVPed, false otherwise
     */
    public boolean hasUserRSVPed(Integer userId, int eventId) {
        return findRSVPByUserAndEvent(userId, eventId) != null;
    }
    
    /**
     * Creates a new RSVP
     * 
     * @param rsvp The RSVP to create
     * @return The ID of the created RSVP
     */
    public int createRSVP(RSVP rsvp) {
        if (rsvp == null || rsvp.getUserId() <= 0 || rsvp.getEventId() <= 0) {
            throw new IllegalArgumentException("Invalid RSVP data");
        }
        
        // Set response time to now
        rsvp.setRespondedAt(new Date());
        
        // Save RSVP
        int rsvpId = rsvpRepository.save(rsvp);
        
        // Update event attendee count
        if (eventService != null) {
            eventService.incrementAttendeeCount(rsvp.getEventId());
        }
        
        return rsvpId;
    }
    
    /**
     * Deletes an RSVP
     * 
     * @param userId The user ID
     * @param eventId The event ID
     */
    public void deleteRSVP(Integer userId, int eventId) {
        RSVP rsvp = findRSVPByUserAndEvent(userId, eventId);
        if (rsvp != null) {
            rsvpRepository.delete(rsvp.getId());
            if (eventService != null) {
                eventService.decrementAttendeeCount(eventId);
            }
        }
    }
    
    /**
     * Find RSVP by ID
     * 
     * @param id The RSVP ID
     * @return The RSVP with the given ID, or null if not found
     */
    public RSVP findRSVPById(int id) {
        return rsvpRepository.findById(id).orElse(null);
    }
    
    /**
     * Find RSVPs by user ID
     * 
     * @param userId The user ID
     * @return List of RSVPs for the given user
     */
    public List<RSVP> findRSVPsByUserId(Integer userId) {
        return rsvpRepository.findByUserId(userId);
    }
    
    /**
     * Find RSVPs by event ID
     * 
     * @param eventId The event ID
     * @return List of RSVPs for the given event
     */
    public List<RSVP> findRSVPsByEventId(int eventId) {
        return rsvpRepository.findByEventId(eventId);
    }
    
    /**
     * Find RSVP by user ID and event ID
     * 
     * @param userId The user ID
     * @param eventId The event ID
     * @return The RSVP, or null if not found
     */
    public RSVP findRSVPByUserAndEvent(Integer userId, int eventId) {
        return rsvpRepository.findByUserIdAndEventId(userId, eventId).orElse(null);
    }
    
    /**
     * Update an existing RSVP
     * 
     * @param rsvp The RSVP with updated information
     */
    public void update(RSVP rsvp) {
        if (rsvp == null || rsvp.getId() <= 0) {
            throw new IllegalArgumentException("Invalid RSVP data");
        }
        
        // Update response time
        rsvp.setRespondedAt(new Date());
        
        rsvpRepository.update(rsvp);
    }
    
    /**
     * Count RSVPs for an event
     * 
     * @param eventId The event ID
     * @return The number of RSVPs for the event
     */
    public int countRSVPsByEventId(int eventId) {
        return rsvpRepository.countByEventId(eventId);
    }
    
    /**
     * Delete an RSVP by ID
     * 
     * @param id The RSVP ID to delete
     */
    public void deleteRSVP(int id) {
        RSVP rsvp = findRSVPById(id);
        if (rsvp != null) {
            rsvpRepository.delete(id);
            if (eventService != null) {
                eventService.decrementAttendeeCount(rsvp.getEventId());
            }
        }
    }
}