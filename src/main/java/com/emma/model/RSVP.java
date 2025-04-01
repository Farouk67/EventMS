package com.emma.model;

import java.util.Date;

/**
 * Represents an RSVP (Répondez s'il vous plaît) response for an event.
 */
public class RSVP {
    private int id;
    private int userId;
    private int eventId;
    private String status; // "yes", "no", "maybe"
    private Date respondedAt;
    private String notes;
    
    // User and Event objects for relationship mapping
    private User user;
    private Event event;
    
    // Constructors
    public RSVP() {
    }
    
    public RSVP(int id, int userId, int eventId, String status) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.status = status;
        this.respondedAt = new Date();
    }
    
    public RSVP(int id, int userId, int eventId, String status, String notes) {
        this(id, userId, eventId, status);
        this.notes = notes;
    }
    
    public RSVP(int i, Integer userId2, int eventId2) {
        //TODO Auto-generated constructor stub
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
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.userId = user.getId();
        }
    }
    
    public Event getEvent() {
        return event;
    }
    
    public void setEvent(Event event) {
        this.event = event;
        if (event != null) {
            this.eventId = event.getId();
        }
    }
    
    @Override
    public String toString() {
        return "RSVP{" +
                "id=" + id +
                ", userId=" + userId +
                ", eventId=" + eventId +
                ", status='" + status + '\'' +
                ", respondedAt=" + respondedAt +
                '}';
    }
}