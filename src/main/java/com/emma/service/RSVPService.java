package com.emma.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import com.emma.model.RSVP;
import com.emma.repository.RSVPRepository;
import com.emma.repository.EventRepository;

public class RSVPService {
    
    private final RSVPRepository rsvpRepository;
    private final EventRepository eventRepository;
    
    /**
     * Constructor with explicit dependencies
     * 
     * @param rsvpRepository The RSVP repository
     * @param eventRepository The Event repository
     */
    public RSVPService(RSVPRepository rsvpRepository, EventRepository eventRepository) {
        this.rsvpRepository = rsvpRepository;
        this.eventRepository = eventRepository;
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
        
        // Update event attendee count directly via repository
        incrementAttendeeCount(rsvp.getEventId());
        
        return rsvpId;
    }
    
    /**
     * Increments the attendee count for an event
     * 
     * @param eventId The ID of the event
     */
    private void incrementAttendeeCount(int eventId) {
        eventRepository.findById(eventId).ifPresent(event -> {
            event.setAttendeeCount(event.getAttendeeCount() + 1);
            eventRepository.save(event);
        });
    }
    
    /**
     * Decrements the attendee count for an event
     * 
     * @param eventId The ID of the event
     */
    private void decrementAttendeeCount(int eventId) {
        eventRepository.findById(eventId).ifPresent(event -> {
            if (event.getAttendeeCount() > 0) {
                event.setAttendeeCount(event.getAttendeeCount() - 1);
                eventRepository.save(event);
            }
        });
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
            decrementAttendeeCount(eventId);
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
    public List<RSVP> getRSVPsByEventId(int eventId) throws SQLException {
    return rsvpRepository.findByEventId(eventId);
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
            decrementAttendeeCount(rsvp.getEventId());
        }
    }
}