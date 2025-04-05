package com.emma.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.emma.model.Event;
import com.emma.util.DatabaseUtil;

/**
 * Repository class to handle Event data operations
 */
public class EventRepository {
    
    // In-memory database to store events
    private static List<Event> events = new ArrayList<>();
    private static int nextId = 1;
    
    /**
     * Find all events in the repository
     * 
     * @return List of all events
     */
    public List<Event> findAll() {
        return new ArrayList<>(events);
    }

    public List<Event> findUpcomingEvents() {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<Event> upcomingEvents = new ArrayList<>();
    
    try {
        conn = DatabaseUtil.getConnection();
        String sql = "SELECT * FROM events WHERE event_date > CURRENT_DATE ORDER BY event_date LIMIT 6";
        
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        
        while (rs.next()) {
            Event event = new Event(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("type"),
                rs.getString("location"),
                rs.getDate("event_date"),
                rs.getInt("attendee_count"),
                rs.getInt("capacity"),
                false, // registration required
                0.0,  // ticket price
                rs.getInt("organizer_id")
            );
            upcomingEvents.add(event);
        }
    } catch (SQLException e) {
        // Log the error
        e.printStackTrace();
    } finally {
        DatabaseUtil.closeResources(conn, pstmt, rs);
    }
    
    return upcomingEvents;
}
    
    /**
     * Find an event by its ID
     * 
     * @param id The ID of the event to find
     * @return Optional containing the event if found, empty otherwise
     */
    public Optional<Event> findById(int id) {
        return events.stream()
                .filter(event -> event.getId() == id)
                .findFirst();
    }
    
    /**
     * Check if an event with the given ID exists
     * 
     * @param id The ID to check
     * @return true if the event exists, false otherwise
     */
    public boolean existsById(int id) {
        return events.stream().anyMatch(event -> event.getId() == id);
    }
    
    /**
     * Save an event (create or update)
     * 
     * @param event The event to save
     * @return The saved event
     */
    public Event save(Event event) {
        if (event.getId() <= 0) {
            // New event
            event.setId(nextId++);
            events.add(event);
        } else {
            // Update existing event
            deleteById(event.getId());
            events.add(event);
        }
        return event;
    }
    
    /**
     * Delete an event by its ID
     * 
     * @param id The ID of the event to delete
     */
    public void deleteById(int id) {
        events.removeIf(event -> event.getId() == id);
    }
    
    /**
     * Find events by type
     * 
     * @param type The event type to search for
     * @return List of events matching the given type
     */
    public List<Event> findByType(String type) {
        return events.stream()
                .filter(event -> type.equals(event.getType()))
                .collect(Collectors.toList());
    }
    
    /**
     * Find events by location
     * 
     * @param location The location to search for
     * @return List of events matching the given location
     */
    public List<Event> findByLocation(String location) {
        return events.stream()
                .filter(event -> location.equals(event.getLocation()))
                .collect(Collectors.toList());
    }
    
    /**
     * Find events by date
     * 
     * @param date The date to search for
     * @return List of events on the given date
     */
    public List<Event> findByDate(Date date) {
        return events.stream()
                .filter(event -> {
                    if (event.getDate() == null || date == null) {
                        return false;
                    }
                    return isSameDay(event.getDate(), date);
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Helper method to compare if two dates are the same day
     */
    private boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        cal1.setTime(date1);
        
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal2.setTime(date2);
        
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
               cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH) &&
               cal1.get(java.util.Calendar.DAY_OF_MONTH) == cal2.get(java.util.Calendar.DAY_OF_MONTH);
    }
}