package com.emma.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.emma.model.RSVP;

/**
 * Repository for RSVP entities
 */
public class RSVPRepository {
    
    // In-memory storage for RSVPs (would be replaced with database in production)
    private static List<RSVP> rsvps = new ArrayList<>();
    private static int nextId = 1;
    
    /**
     * Find RSVP by ID
     * 
     * @param id The RSVP ID
     * @return Optional containing the RSVP if found
     */
    public Optional<RSVP> findById(int id) {
        return rsvps.stream()
                .filter(rsvp -> rsvp.getId() == id)
                .findFirst();
    }
    
    /**
     * Find RSVPs by user ID
     * 
     * @param userId The user ID
     * @return List of RSVPs for the user
     */
    public List<RSVP> findByUserId(Integer userId) {
        return rsvps.stream()
                .filter(rsvp -> rsvp.getUserId() == userId)
                .collect(Collectors.toList());
    }
    
    /**
     * Find RSVPs by event ID
     * 
     * @param eventId The event ID
     * @return List of RSVPs for the event
     */
    public List<RSVP> findByEventId(int eventId) {
        return rsvps.stream()
                .filter(rsvp -> rsvp.getEventId() == eventId)
                .collect(Collectors.toList());
    }
    
    /**
     * Find RSVP by user ID and event ID
     * 
     * @param userId The user ID
     * @param eventId The event ID
     * @return Optional containing the RSVP if found
     */
    public Optional<RSVP> findByUserIdAndEventId(Integer userId, int eventId) {
        return rsvps.stream()
                .filter(rsvp -> rsvp.getUserId() == userId && rsvp.getEventId() == eventId)
                .findFirst();
    }
    
    /**
     * Save a new RSVP
     * 
     * @param rsvp The RSVP to save
     * @return The ID of the saved RSVP
     */
    public int save(RSVP rsvp) {
        // For new RSVP
        if (rsvp.getId() <= 0) {
            rsvp.setId(nextId++);
            rsvps.add(rsvp);
        } else {
            // For existing RSVP
            update(rsvp);
        }
        return rsvp.getId();
    }
    
    /**
     * Update an existing RSVP
     * 
     * @param rsvp The RSVP to update
     */
    public void update(RSVP rsvp) {
        for (int i = 0; i < rsvps.size(); i++) {
            if (rsvps.get(i).getId() == rsvp.getId()) {
                rsvps.set(i, rsvp);
                break;
            }
        }
    }
    
    /**
     * Delete an RSVP
     * 
     * @param id The ID of the RSVP to delete
     */
    public void delete(int id) {
        rsvps.removeIf(rsvp -> rsvp.getId() == id);
    }
    
    /**
     * Count RSVPs for an event
     * 
     * @param eventId The event ID
     * @return The number of RSVPs for the event
     */
    public int countByEventId(int eventId) {
        return (int) rsvps.stream()
                .filter(rsvp -> rsvp.getEventId() == eventId)
                .count();
    }
}