package com.emma.dao;

import com.emma.model.Event;
import com.emma.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Event entity
 */
public class EventDAO {
    
    public Event createEvent(Event event) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "INSERT INTO events (name, description, event_date, location, created_by, event_type_id, capacity, created_at, updated_at) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                         
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, event.getName());
            pstmt.setString(2, event.getDescription());
            pstmt.setTimestamp(3, new Timestamp(event.getEventDate().getTime()));
            pstmt.setString(4, event.getLocation());
            pstmt.setInt(5, event.getCreatedBy());
            pstmt.setInt(6, event.getEventTypeId());
            pstmt.setInt(7, event.getCapacity());
            pstmt.setTimestamp(8, new Timestamp(event.getCreatedAt().getTime()));
            pstmt.setTimestamp(9, new Timestamp(event.getUpdatedAt().getTime()));
            
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
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE events SET name = ?, description = ?, event_date = ?, location = ?, " +
                         "event_type_id = ?, capacity = ?, updated_at = ? WHERE id = ?";
                         
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, event.getName());
            pstmt.setString(2, event.getDescription());
            pstmt.setTimestamp(3, new Timestamp(event.getEventDate().getTime()));
            pstmt.setString(4, event.getLocation());
            pstmt.setInt(5, event.getEventTypeId());
            pstmt.setInt(6, event.getCapacity());
            pstmt.setTimestamp(7, new Timestamp(new java.util.Date().getTime()));
            pstmt.setInt(8, event.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } finally {
            DatabaseUtil.closeResources(conn, pstmt, null);
        }
    }
    
    public boolean deleteEvent(int eventId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String deleteRSVPs = "DELETE FROM rsvps WHERE event_id = ?";
            pstmt = conn.prepareStatement(deleteRSVPs);
            pstmt.setInt(1, eventId);
            pstmt.executeUpdate();
            
            String deleteEvent = "DELETE FROM events WHERE id = ?";
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
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT e.*, et.name as event_type_name FROM events e " +
                         "JOIN event_types et ON e.event_type_id = et.id " +
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
        Event event = new Event(0, null, null, null, null, null, 0);
        
        event.setId(rs.getInt("id"));
        event.setName(rs.getString("name"));
        event.setDescription(rs.getString("description"));
        event.setEventDate(rs.getTimestamp("event_date"));
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
