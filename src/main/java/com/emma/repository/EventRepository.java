package com.emma.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.emma.model.Event;
import com.emma.util.DBConnectionPool;

/**
 * Repository class to handle Event data operations
 */
public class EventRepository {
    
    /**
     * Find all events in the repository
     * 
     * @return List of all events
     */
    public List<Event> findAll() {
        List<Event> events = new ArrayList<>();
        
        try (Connection conn = DBConnectionPool.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM event")) {
            
            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all events: " + e.getMessage());
            e.printStackTrace();
        }
        
        return events;
    }

    /**
     * Find upcoming events (events with dates in the future)
     * 
     * @return List of upcoming events
     */
    public List<Event> findUpcomingEvents() {
        List<Event> upcomingEvents = new ArrayList<>();
        
        try (Connection conn = DBConnectionPool.getConnection()) {
            String sql = "SELECT * FROM event WHERE event_date > CURRENT_DATE ORDER BY event_date LIMIT 6";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                
                while (rs.next()) {
                    upcomingEvents.add(mapResultSetToEvent(rs));
                }
                
                System.out.println("Found " + upcomingEvents.size() + " upcoming events");
            }
        } catch (SQLException e) {
            System.err.println("Error finding upcoming events: " + e.getMessage());
            e.printStackTrace();
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
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM event WHERE id = ?")) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEvent(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding event by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
    /**
     * Check if an event with the given ID exists
     * 
     * @param id The ID to check
     * @return true if the event exists, false otherwise
     */
    public boolean existsById(int id) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT 1 FROM event WHERE id = ?")) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error checking if event exists: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Save an event (create or update)
     * 
     * @param event The event to save
     * @return The saved event
     */
    public Event save(Event event) {
        try (Connection conn = DBConnectionPool.getConnection()) {
            if (event.getId() <= 0) {
                // Insert new event
                String sql = "INSERT INTO event (name, description, event_date, location, created_by, " +
                             "event_type_id, attendee_count, capacity, registration_required, ticket_price) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setString(1, event.getName());
                    pstmt.setString(2, event.getDescription());
                    pstmt.setTimestamp(3, event.getEventDate() != null ? 
                            new java.sql.Timestamp(event.getEventDate().getTime()) : null);
                    pstmt.setString(4, event.getLocation());
                    
                    if (event.getCreatedBy() != null) {
                        pstmt.setInt(5, event.getCreatedBy());
                    } else {
                        pstmt.setNull(5, java.sql.Types.INTEGER);
                    }
                    
                    if (event.getEventTypeId() != null) {
                        pstmt.setInt(6, event.getEventTypeId());
                    } else {
                        pstmt.setNull(6, java.sql.Types.INTEGER);
                    }
                    
                    pstmt.setInt(7, event.getAttendeeCount());
                    pstmt.setInt(8, event.getCapacity());
                    pstmt.setBoolean(9, event.isRegistrationRequired());
                    pstmt.setDouble(10, event.getTicketPrice());
                    
                    pstmt.executeUpdate();
                    
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            event.setId(generatedKeys.getInt(1));
                        }
                    }
                }
            } else {
                // Update existing event
                String sql = "UPDATE event SET name = ?, description = ?, event_date = ?, location = ?, " +
                             "created_by = ?, event_type_id = ?, attendee_count = ?, capacity = ?, " +
                             "registration_required = ?, ticket_price = ? " +
                             "WHERE id = ?";
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, event.getName());
                    pstmt.setString(2, event.getDescription());
                    pstmt.setTimestamp(3, event.getEventDate() != null ? 
                            new java.sql.Timestamp(event.getEventDate().getTime()) : null);
                    pstmt.setString(4, event.getLocation());
                    
                    if (event.getCreatedBy() != null) {
                        pstmt.setInt(5, event.getCreatedBy());
                    } else {
                        pstmt.setNull(5, java.sql.Types.INTEGER);
                    }
                    
                    if (event.getEventTypeId() != null) {
                        pstmt.setInt(6, event.getEventTypeId());
                    } else {
                        pstmt.setNull(6, java.sql.Types.INTEGER);
                    }
                    
                    pstmt.setInt(7, event.getAttendeeCount());
                    pstmt.setInt(8, event.getCapacity());
                    pstmt.setBoolean(9, event.isRegistrationRequired());
                    pstmt.setDouble(10, event.getTicketPrice());
                    pstmt.setInt(11, event.getId());
                    
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving event: " + e.getMessage());
            e.printStackTrace();
        }
        
        return event;
    }
    
    /**
     * Delete an event by its ID
     * 
     * @param id The ID of the event to delete
     */
    public void deleteById(int id) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM event WHERE id = ?")) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error deleting event: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Find events by type
     * 
     * @param type The event type to search for
     * @return List of events matching the given type
     */
    public List<Event> findByType(String type) {
        List<Event> events = new ArrayList<>();
        
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT e.* FROM event e JOIN event_type et ON e.event_type_id = et.id WHERE et.name = ?")) {
            
            pstmt.setString(1, type);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    events.add(mapResultSetToEvent(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding events by type: " + e.getMessage());
            e.printStackTrace();
        }
        
        return events;
    }
    
    /**
     * Map a database result set to an Event object
     * 
     * @param rs The result set
     * @return Mapped Event object
     * @throws SQLException if a database access error occurs
     */
    private Event mapResultSetToEvent(ResultSet rs) throws SQLException {
        Event event = new Event();
        
        event.setId(rs.getInt("id"));
        event.setName(rs.getString("name"));
        event.setDescription(rs.getString("description"));
        event.setEventDate(rs.getTimestamp("event_date"));
        event.setLocation(rs.getString("location"));
        
        // Handle nullable fields
        int createdBy = rs.getInt("created_by");
        if (!rs.wasNull()) {
            event.setCreatedBy(createdBy);
            event.setOrganizerId(createdBy); // Keep both fields in sync
        }
        
        int eventTypeId = rs.getInt("event_type_id");
        if (!rs.wasNull()) {
            event.setEventTypeId(eventTypeId);
        }
        
        event.setAttendeeCount(rs.getInt("attendee_count"));
        event.setCapacity(rs.getInt("capacity"));
        event.setRegistrationRequired(rs.getBoolean("registration_required"));
        event.setTicketPrice(rs.getDouble("ticket_price"));
        
        try {
            event.setCreatedAt(rs.getTimestamp("created_at"));
            event.setUpdatedAt(rs.getTimestamp("updated_at"));
        } catch (SQLException e) {
            // Columns might not exist, ignore
        }
        
        // Try to get the event type name from a join if available
        try {
            String eventTypeName = rs.getString("event_type_name");
            if (eventTypeName != null) {
                event.setEventTypeName(eventTypeName);
                event.setType(eventTypeName); // For backward compatibility
            }
        } catch (SQLException e) {
            // Column not in result set, ignore
        }
        
        return event;
    }
}