package com.emma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.emma.model.RSVP;
import com.emma.util.DBConnectionPool;


public class RSVPDAO {
    private Connection connection;
    
    public RSVPDAO() throws SQLException {
        connection = DBConnectionPool.getConnection();
    }
    
    public RSVP createRSVP(RSVP rsvp) throws SQLException {
        String sql = "INSERT INTO rsvp (user_id, event_id) VALUES (?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, rsvp.getUserId());
            statement.setInt(2, rsvp.getEventId());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating RSVP failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    rsvp.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating RSVP failed, no ID obtained.");
                }
            }
        }
        
        return rsvp;
    }
    
    public void deleteRSVP(int userId, int eventId) throws SQLException {
        String sql = "DELETE FROM rsvp WHERE user_id = ? AND event_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, eventId);
            statement.executeUpdate();
        }
    }
    
    public boolean hasUserRSVPed(int userId, int eventId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rsvp WHERE user_id = ? AND event_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, eventId);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        
        return false;
    }
    
    public List<Integer> getEventIdsByUser(int userId) throws SQLException {
        List<Integer> eventIds = new ArrayList<>();
        String sql = "SELECT event_id FROM rsvp WHERE user_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                eventIds.add(rs.getInt("event_id"));
            }
        }
        
        return eventIds;
    }
    
    public List<Integer> getUserIdsByEvent(int eventId) throws SQLException {
        List<Integer> userIds = new ArrayList<>();
        String sql = "SELECT user_id FROM rsvp WHERE event_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, eventId);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                userIds.add(rs.getInt("user_id"));
            }
        }
        
        return userIds;
    }
    
    public int getEventRSVPCount(int eventId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rsvp WHERE event_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, eventId);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        
        return 0;
    }
}