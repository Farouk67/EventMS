package com.emma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.emma.util.DatabaseUtil;

public class EventTypeDAO {
    private Connection connection;
    
    public EventTypeDAO() throws SQLException {
        connection = DatabaseUtil.getConnection();
    }
    
    public List<String> getAllEventTypes() throws SQLException {
        List<String> eventTypes = new ArrayList<>();
        String sql = "SELECT DISTINCT type FROM events";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                eventTypes.add(rs.getString("type"));
            }
        }
        
        return eventTypes;
    }
    
    public boolean isValidEventType(String eventType) throws SQLException {
        String sql = "SELECT COUNT(*) FROM events WHERE type = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, eventType);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        
        return false;
    }
    
    // Method for machine learning model - get event type counts
    public int getEventTypeCount(String eventType) throws SQLException {
        String sql = "SELECT COUNT(*) FROM events WHERE type = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, eventType);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        
        return 0;
    }
    
    // Method for machine learning model - get all event types with counts
    public List<Object[]> getEventTypeCounts() throws SQLException {
        List<Object[]> typeCounts = new ArrayList<>();
        String sql = "SELECT type, COUNT(*) as count FROM events GROUP BY type";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                Object[] typeCount = new Object[2];
                typeCount[0] = rs.getString("type");
                typeCount[1] = rs.getInt("count");
                typeCounts.add(typeCount);
            }
        }
        
        return typeCounts;
    }
}