package com.emma.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

public class Event {
    private int id;
    private String name;
    private Date date;
    private String location;
    private String description;
    private String type;
    private int organizerId;
    private int attendeeCount;
    private int capacity;
    
    // Default constructor
    public Event(int id2, String name2, String type2, java.util.Date date2, java.util.Date date3, String location2, String description2, int capacity2, int attendees, int userId, String description3, boolean b) {
        // If necessary, initialize fields based on your project requirements
    }
    
    // Parameterized constructor
    public Event(int id, String name, Date date, String location, String description, String type, int organizerId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
        this.type = type;
        this.organizerId = organizerId;
        this.attendeeCount = 0;
        this.capacity = 100; // Default capacity
    }
    
    // Full constructor with attendee count and capacity
    public Event(int id, String name, Date date, String location, String description, String type, 
                 int organizerId, int attendeeCount, int capacity) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
        this.type = type;
        this.organizerId = organizerId;
        this.attendeeCount = attendeeCount;
        this.capacity = capacity;
    }
    
    // Getters and Setters
    
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
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
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
    
    public int getOrganizerId() {
        return organizerId;
    }
    
    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
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
    
    // Methods to manage attendees
    public boolean addAttendee() {
        if (attendeeCount < capacity) {
            attendeeCount++;
            return true;
        }
        return false; // Event is at capacity
    }
    
    public boolean removeAttendee() {
        if (attendeeCount > 0) {
            attendeeCount--;
            return true;
        }
        return false; // No attendees to remove
    }
    
    public boolean isAtCapacity() {
        return attendeeCount >= capacity;
    }
    
    public double getOccupancyRate() {
        return (double) attendeeCount / capacity * 100;
    }
    
    // toString, equals, and hashCode methods
    
    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", organizerId=" + organizerId +
                ", attendeeCount=" + attendeeCount +
                ", capacity=" + capacity +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id &&
                organizerId == event.organizerId &&
                Objects.equals(name, event.name) &&
                Objects.equals(date, event.date) &&
                Objects.equals(location, event.location) &&
                Objects.equals(type, event.type);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, location, type, organizerId);
    }
    
    // Methods for event validation
    
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() &&
                date != null &&
                location != null && !location.trim().isEmpty() &&
                description != null &&
                type != null && !type.trim().isEmpty();
    }
    
    public boolean isDateValid() {
        if (date == null) {
            return false;
        }
        // Check if the date is in the future
        Date currentDate = new Date(System.currentTimeMillis());
        return date.after(currentDate);
    }
    
    // Additional methods that might have been auto-generated with TODOs
    
    public String getFormattedDate() {
        return date != null ? date.toString() : "";
    }
    
    public String getShortDescription() {
        if (description == null || description.length() <= 100) {
            return description;
        }
        return description.substring(0, 97) + "...";
    }
    
    public boolean isOrganizedBy(int userId) {
        return organizerId == userId;
    }
    
    public boolean hasSpaceForAttendees(int count) {
        return (attendeeCount + count) <= capacity;
    }
    
    public void updateAttendeeCount(int newCount) {
        if (newCount >= 0 && newCount <= capacity) {
            this.attendeeCount = newCount;
        }
    }
    
    public boolean isSameType(String eventType) {
        return type != null && type.equalsIgnoreCase(eventType);
    }
    
    public boolean isInLocation(String searchLocation) {
        return location != null && location.toLowerCase().contains(searchLocation.toLowerCase());
    }

    public int getAttendees() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAttendees'");
    }

    public int getUserId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserId'");
    }

    public Timestamp getEventDate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEventDate'");
    }

    public int getEventTypeId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEventTypeId'");
    }

    public int getCreatedBy() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCreatedBy'");
    }

    public Timestamp getCreatedAt() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCreatedAt'");
    }

    public Timestamp getUpdatedAt() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUpdatedAt'");
    }

    public void setEventDate(Timestamp timestamp) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setEventDate'");
    }

    public void setCreatedBy(int int1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCreatedBy'");
    }

    public void setEventTypeId(int int1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setEventTypeId'");
    }

    public void setEventTypeName(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setEventTypeName'");
    }

    public void setCreatedAt(Timestamp timestamp) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCreatedAt'");
    }

    public void setUpdatedAt(Timestamp timestamp) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUpdatedAt'");
    }

    public String getEventTypeName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEventTypeName'");
    }

    // Removed duplicate method and unnecessary TODO methods
    
    // Removed getAttendees, getUserId, and event date-related methods
}
