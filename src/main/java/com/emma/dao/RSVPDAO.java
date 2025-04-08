package com.emma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.emma.model.RSVP;
import com.emma.util.DBConnectionPool;


public class RSVPDAO {
    
    public RSVPDAO() {
        // No need for constructor to create connection anymore
    }
    
    public RSVP createRSVP(RSVP rsvp) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "INSERT INTO rsvp (user_id, event_id, status, responded_at, notes) VALUES (?, ?, ?, ?, ?)";
            
            statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, rsvp.getUserId());
            statement.setInt(2, rsvp.getEventId());
            statement.setString(3, rsvp.getStatus() != null ? rsvp.getStatus() : "attending");
            
            // Set current timestamp if responded_at is null
            if (rsvp.getRespondedAt() != null) {
                statement.setTimestamp(4, new Timestamp(rsvp.getRespondedAt().getTime()));
            } else {
                statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            }
            
            statement.setString(5, rsvp.getNotes());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating RSVP failed, no rows affected.");
            }
            
            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                rsvp.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Creating RSVP failed, no ID obtained.");
            }
            
            return rsvp;
        } finally {
            if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
    
    public void deleteRSVP(int userId, int eventId) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "DELETE FROM rsvp WHERE user_id = ? AND event_id = ?";
            
            statement = conn.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, eventId);
            statement.executeUpdate();
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
    
    public boolean hasUserRSVPed(int userId, int eventId) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "SELECT COUNT(*) FROM rsvp WHERE user_id = ? AND event_id = ?";
            
            statement = conn.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, eventId);
            rs = statement.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
            return false;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
    
    public List<Integer> getEventIdsByUser(int userId) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "SELECT event_id FROM rsvp WHERE user_id = ?";
            
            statement = conn.prepareStatement(sql);
            statement.setInt(1, userId);
            rs = statement.executeQuery();
            
            List<Integer> eventIds = new ArrayList<>();
            while (rs.next()) {
                eventIds.add(rs.getInt("event_id"));
            }
            
            return eventIds;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
    
    public List<Integer> getUserIdsByEvent(int eventId) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "SELECT user_id FROM rsvp WHERE event_id = ?";
            
            statement = conn.prepareStatement(sql);
            statement.setInt(1, eventId);
            rs = statement.executeQuery();
            
            List<Integer> userIds = new ArrayList<>();
            while (rs.next()) {
                userIds.add(rs.getInt("user_id"));
            }
            
            return userIds;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
    
    public int getEventRSVPCount(int eventId) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "SELECT COUNT(*) FROM rsvp WHERE event_id = ?";
            
            statement = conn.prepareStatement(sql);
            statement.setInt(1, eventId);
            rs = statement.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
            return 0;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
    
    public RSVP getRSVP(int userId, int eventId) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "SELECT * FROM rsvp WHERE user_id = ? AND event_id = ?";
            
            statement = conn.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, eventId);
            rs = statement.executeQuery();
            
            if (rs.next()) {
                RSVP rsvp = new RSVP();
                rsvp.setId(rs.getInt("id"));
                rsvp.setUserId(rs.getInt("user_id"));
                rsvp.setEventId(rs.getInt("event_id"));
                rsvp.setStatus(rs.getString("status"));
                
                Timestamp respondedAt = rs.getTimestamp("responded_at");
                if (respondedAt != null) {
                    rsvp.setRespondedAt(new Date(respondedAt.getTime()));
                }
                
                rsvp.setNotes(rs.getString("notes"));
                
                return rsvp;
            }
            
            return null;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
    
    public RSVP getRSVPById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "SELECT * FROM rsvp WHERE id = ?";
            
            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            
            if (rs.next()) {
                RSVP rsvp = new RSVP();
                rsvp.setId(rs.getInt("id"));
                rsvp.setUserId(rs.getInt("user_id"));
                rsvp.setEventId(rs.getInt("event_id"));
                rsvp.setStatus(rs.getString("status"));
                
                Timestamp respondedAt = rs.getTimestamp("responded_at");
                if (respondedAt != null) {
                    rsvp.setRespondedAt(new Date(respondedAt.getTime()));
                }
                
                rsvp.setNotes(rs.getString("notes"));
                
                return rsvp;
            }
            
            return null;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
    
    public List<RSVP> getRSVPsByEventId(int eventId) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "SELECT * FROM rsvp WHERE event_id = ?";
            
            statement = conn.prepareStatement(sql);
            statement.setInt(1, eventId);
            rs = statement.executeQuery();
            
            List<RSVP> rsvps = new ArrayList<>();
            while (rs.next()) {
                RSVP rsvp = new RSVP();
                rsvp.setId(rs.getInt("id"));
                rsvp.setUserId(rs.getInt("user_id"));
                rsvp.setEventId(rs.getInt("event_id"));
                rsvp.setStatus(rs.getString("status"));
                
                Timestamp respondedAt = rs.getTimestamp("responded_at");
                if (respondedAt != null) {
                    rsvp.setRespondedAt(new Date(respondedAt.getTime()));
                }
                
                rsvp.setNotes(rs.getString("notes"));
                
                rsvps.add(rsvp);
            }
            
            return rsvps;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
    
    public void deleteRSVPById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "DELETE FROM rsvp WHERE id = ?";
            
            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
}