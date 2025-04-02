package com.emma.model;

import java.util.Date;
import java.sql.Timestamp;

/**
 * Model class representing an Event
 */
public class Event {
    private int id;
    private String name;
    private String description;
    private String type; // For compatibility with UserDAO
    private String location;
    private Date date; // For compatibility with UserDAO
    private Date eventDate; // For compatibility with EventDAO
    private int attendeeCount;
    private int capacity;
    private boolean registrationRequired;
    private double ticketPrice;
    private Integer organizerId; // For compatibility with UserDAO
    private Integer createdBy; // For compatibility with EventDAO
    private Integer eventTypeId;
    private String eventTypeName;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Default constructor
    public Event() {
        this.attendeeCount = 0;
    }
    
    // Constructor with basic information
    public Event(String name, String description, String type, String location, Date date) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.location = location;
        this.date = date;
        this.eventDate = date; // Sync both date fields
        this.attendeeCount = 0;
    }
    
    // Constructor for UserDAO compatibility
    public Event(int id, String name, String description, String type, String location, 
                Date date, int attendeeCount, int capacity, boolean registrationRequired, 
                double ticketPrice, Integer organizerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.location = location;
        this.date = date;
        this.eventDate = date; // Sync both date fields
        this.attendeeCount = attendeeCount;
        this.capacity = capacity;
        this.registrationRequired = registrationRequired;
        this.ticketPrice = ticketPrice;
        this.organizerId = organizerId;
        this.createdBy = organizerId; // Sync both organizer fields
    }
    
    // Constructor for EventDAO compatibility
    public Event(int id, String name, String description, Date eventDate, 
                String location, Integer createdBy, Integer eventTypeId, int capacity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.eventDate = eventDate;
        this.date = eventDate; // Sync both date fields
        this.location = location;
        this.createdBy = createdBy;
        this.organizerId = createdBy; // Sync both organizer fields
        this.eventTypeId = eventTypeId;
        this.capacity = capacity;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
    
    // Original getters and setters
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
        this.eventDate = date; // Keep both date fields in sync
    }
    
    public int getAttendeeCount() {
        return attendeeCount;
    }
    
    public void setAttendeeCount(int attendeeCount) {
        this.attendeeCount = attendeeCount;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    public boolean isRegistrationRequired() {
        return registrationRequired;
    }
    
    public void setRegistrationRequired(boolean registrationRequired) {
        this.registrationRequired = registrationRequired;
    }
    
    public double getTicketPrice() {
        return ticketPrice;
    }
    
    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
    
    public Integer getOrganizerId() {
        return organizerId;
    }
    
    public void setOrganizerId(Integer organizerId) {
        this.organizerId = organizerId;
        this.createdBy = organizerId; // Keep both organizer fields in sync
    }
    
    // New getters and setters for EventDAO compatibility
    
    public Date getEventDate() {
        return eventDate;
    }
    
    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
        this.date = eventDate; // Keep both date fields in sync
    }
    
    public Integer getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
        this.organizerId = createdBy; // Keep both organizer fields in sync
    }
    
    public Integer getEventTypeId() {
        return eventTypeId;
    }
    
    public void setEventTypeId(Integer eventTypeId) {
        this.eventTypeId = eventTypeId;
    }
    
    public String getEventTypeName() {
        return eventTypeName;
    }
    
    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Event [id=" + id + ", name=" + name + ", type=" + type + ", location=" + location 
                + ", date=" + date + ", attendeeCount=" + attendeeCount + ", organizerId=" + organizerId + "]";
    }
}