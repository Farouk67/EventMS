package com.emma.dao;

import com.emma.model.Event;
import com.emma.util.DBConnectionPool;
import com.emma.util.DatabaseUtil;

import java.sql.*;

/**
 * Data Access Object for Event entity
 */
public class EventDAO {
    
    public Event createEvent(Event event) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "INSERT INTO event (name, description, event_date, location, created_by, event_type_id, capacity, created_at, updated_at) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                         
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, event.getName());
            pstmt.setString(2, event.getDescription());
            
            // Ensure we handle the date properly
            java.util.Date eventDate = event.getEventDate();
            if (eventDate != null) {
                pstmt.setTimestamp(3, new Timestamp(eventDate.getTime()));
            } else {
                pstmt.setTimestamp(3, null);
            }
            
            pstmt.setString(4, event.getLocation());
            
            // Handle potentially null values
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
            
            pstmt.setInt(7, event.getCapacity());
            
            // Set timestamps or current time if null
            Timestamp now = new Timestamp(System.currentTimeMillis());
            pstmt.setTimestamp(8, event.getCreatedAt() != null ? event.getCreatedAt() : now);
            pstmt.setTimestamp(9, event.getUpdatedAt() != null ? event.getUpdatedAt() : now);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating event failed, no rows affected.");
            }
            
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                event.setId(rs.getInt(1));
            } else {
                throw new SQLException("Creating event failed, no ID obtained.");
            }
            
            return event;
        } finally {
            DatabaseUtil.closeResources(conn, pstmt, rs);
        }
    }
    
    public boolean updateEvent(Event event) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "UPDATE event SET name = ?, description = ?, event_date = ?, location = ?, " +
                         "event_type_id = ?, capacity = ?, updated_at = ? WHERE id = ?";
                         
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, event.getName());
            pstmt.setString(2, event.getDescription());
            
            // Ensure we handle the date properly
            java.util.Date eventDate = event.getEventDate();
            if (eventDate != null) {
                pstmt.setTimestamp(3, new Timestamp(eventDate.getTime()));
            } else {
                pstmt.setTimestamp(3, null);
            }
            
            pstmt.setString(4, event.getLocation());
            
            // Handle potentially null values
            if (event.getEventTypeId() != null) {
                pstmt.setInt(5, event.getEventTypeId());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }
            
            pstmt.setInt(6, event.getCapacity());
            pstmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(8, event.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } finally {
            DatabaseUtil.closeResources(conn, pstmt, null);
        }
    }
    
    public boolean deleteEvent(int eventId) throws SQLException {
        // This method doesn't need changes as it doesn't interact with Event objects
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String deleteRSVPs = "DELETE FROM rsvp WHERE event_id = ?";
            pstmt = conn.prepareStatement(deleteRSVPs);
            pstmt.setInt(1, eventId);
            pstmt.executeUpdate();
            
            String deleteEvent = "DELETE FROM event WHERE id = ?";
            pstmt = conn.prepareStatement(deleteEvent);
            pstmt.setInt(1, eventId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } finally {
            DatabaseUtil.closeResources(conn, pstmt, null);
        }
    }
    
    public Event getEventById(int eventId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "SELECT e.*, et.name as event_type_name FROM event e " +
                         "JOIN event_type et ON e.event_type_id = et.id " +
                         "WHERE e.id = ?";
                         
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, eventId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToEvent(rs);
            }
            
            return null;
        } finally {
            DatabaseUtil.closeResources(conn, pstmt, rs);
        }
    }
    
    private Event mapResultSetToEvent(ResultSet rs) throws SQLException {
        Event event = new Event();
        
        event.setId(rs.getInt("id"));
        event.setName(rs.getString("name"));
        event.setDescription(rs.getString("description"));
        
        Timestamp eventDateTimestamp = rs.getTimestamp("event_date");
        if (eventDateTimestamp != null) {
            event.setEventDate(new Date(eventDateTimestamp.getTime()));
            event.setDate(new Date(eventDateTimestamp.getTime())); // Keep both date fields in sync
        }
        
        event.setLocation(rs.getString("location"));
        event.setCreatedBy(rs.getInt("created_by"));
        event.setEventTypeId(rs.getInt("event_type_id"));
        event.setEventTypeName(rs.getString("event_type_name"));
        event.setCapacity(rs.getInt("capacity"));
        event.setCreatedAt(rs.getTimestamp("created_at"));
        event.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return event;
    }
}