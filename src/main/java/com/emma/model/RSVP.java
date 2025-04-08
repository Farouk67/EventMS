package com.emma.model;

import java.util.Date;

/**
 * Model class for RSVP entity
 */
public class RSVP {
    private int id;
    private int userId;
    private int eventId;
    private String status;
    private Date respondedAt;
    private String notes;
    
    /**
     * Default constructor
     */
    public RSVP() {
    }
    
    /**
     * Constructor with essential fields
     * 
     * @param id The RSVP ID
     * @param userId The user ID
     * @param eventId The event ID
     */
    public RSVP(int id, int userId, int eventId) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.status = "attending";
        this.respondedAt = new Date();
    }
    
    /**
     * Constructor with all fields
     * 
     * @param id The RSVP ID
     * @param userId The user ID
     * @param eventId The event ID
     * @param status The RSVP status
     * @param respondedAt The response time
     * @param notes Any additional notes
     */
    public RSVP(int id, int userId, int eventId, String status, Date respondedAt, String notes) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.status = status;
        this.respondedAt = respondedAt;
        this.notes = notes;
    }
    
    // Getters and Setters
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getEventId() {
        return eventId;
    }
    
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Date getRespondedAt() {
        return respondedAt;
    }
    
    public void setRespondedAt(Date respondedAt) {
        this.respondedAt = respondedAt;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @Override
    public String toString() {
        return "RSVP [id=" + id + ", userId=" + userId + ", eventId=" + eventId + 
               ", status=" + status + ", respondedAt=" + respondedAt + "]";
    }
}