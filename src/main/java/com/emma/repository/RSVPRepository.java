package com.emma.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.emma.model.RSVP;
import com.emma.util.DBConnectionPool;

/**
 * Repository for RSVP entities using database storage
 */
public class RSVPRepository {
    
    /**
     * Find RSVP by ID
     * 
     * @param id The RSVP ID
     * @return Optional containing the RSVP if found
     */
    public Optional<RSVP> findById(int id) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM rsvp WHERE id = ?")) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToRSVP(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding RSVP by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
    /**
     * Find RSVPs by user ID
     * 
     * @param userId The user ID
     * @return List of RSVPs for the user
     */
    public List<RSVP> findByUserId(Integer userId) {
        List<RSVP> userRsvps = new ArrayList<>();
        
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM rsvp WHERE user_id = ?")) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    userRsvps.add(mapResultSetToRSVP(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding RSVPs by user ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return userRsvps;
    }
    
    /**
     * Find RSVPs by event ID
     * 
     * @param eventId The event ID
     * @return List of RSVPs for the event
     */
    public List<RSVP> findByEventId(int eventId) {
        List<RSVP> eventRsvps = new ArrayList<>();
        
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM rsvp WHERE event_id = ?")) {
            
            pstmt.setInt(1, eventId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    eventRsvps.add(mapResultSetToRSVP(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding RSVPs by event ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return eventRsvps;
    }
    
    /**
     * Find RSVP by user ID and event ID
     * 
     * @param userId The user ID
     * @param eventId The event ID
     * @return Optional containing the RSVP if found
     */
    public Optional<RSVP> findByUserIdAndEventId(Integer userId, int eventId) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM rsvp WHERE user_id = ? AND event_id = ?")) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, eventId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToRSVP(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding RSVP by user ID and event ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
    /**
     * Save a new RSVP
     * 
     * @param rsvp The RSVP to save
     * @return The ID of the saved RSVP
     */
    public int save(RSVP rsvp) {
        if (rsvp.getId() <= 0) {
            return insert(rsvp);
        } else {
            update(rsvp);
            return rsvp.getId();
        }
    }
    
    /**
     * Insert a new RSVP
     * 
     * @param rsvp The RSVP to insert
     * @return The ID of the inserted RSVP
     */
    private int insert(RSVP rsvp) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO rsvp (user_id, event_id, status, responded_at, notes) VALUES (?, ?, ?, ?, ?)",
                 Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, rsvp.getUserId());
            pstmt.setInt(2, rsvp.getEventId());
            pstmt.setString(3, rsvp.getStatus() != null ? rsvp.getStatus() : "attending");
            
            // Set responded_at to current time if not provided
            if (rsvp.getRespondedAt() != null) {
                pstmt.setTimestamp(4, new Timestamp(rsvp.getRespondedAt().getTime()));
            } else {
                pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            }
            
            pstmt.setString(5, rsvp.getNotes());
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving RSVP: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Update an existing RSVP
     * 
     * @param rsvp The RSVP to update
     */
    public void update(RSVP rsvp) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "UPDATE rsvp SET user_id = ?, event_id = ?, status = ?, responded_at = ?, notes = ? WHERE id = ?")) {
            
            pstmt.setInt(1, rsvp.getUserId());
            pstmt.setInt(2, rsvp.getEventId());
            pstmt.setString(3, rsvp.getStatus() != null ? rsvp.getStatus() : "attending");
            
            // Set responded_at to current time if not provided
            if (rsvp.getRespondedAt() != null) {
                pstmt.setTimestamp(4, new Timestamp(rsvp.getRespondedAt().getTime()));
            } else {
                pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            }
            
            pstmt.setString(5, rsvp.getNotes());
            pstmt.setInt(6, rsvp.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating RSVP: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Delete an RSVP
     * 
     * @param id The ID of the RSVP to delete
     */
    public void delete(int id) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM rsvp WHERE id = ?")) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error deleting RSVP: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Count RSVPs for an event
     * 
     * @param eventId The event ID
     * @return The number of RSVPs for the event
     */
    public int countByEventId(int eventId) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM rsvp WHERE event_id = ?")) {
            
            pstmt.setInt(1, eventId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting RSVPs by event ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Map a database result set to an RSVP object
     * 
     * @param rs The result set
     * @return Mapped RSVP object
     * @throws SQLException if a database access error occurs
     */
    private RSVP mapResultSetToRSVP(ResultSet rs) throws SQLException {
        RSVP rsvp = new RSVP();
        
        rsvp.setId(rs.getInt("id"));
        rsvp.setUserId(rs.getInt("user_id"));
        rsvp.setEventId(rs.getInt("event_id"));
        rsvp.setStatus(rs.getString("status"));
        rsvp.setRespondedAt(rs.getTimestamp("responded_at"));
        rsvp.setNotes(rs.getString("notes"));
        
        return rsvp;
    }
}