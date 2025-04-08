package com.emma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.emma.model.User;
import com.emma.model.Event;
import com.emma.util.DBConnectionPool;

public class UserDAO {
    
    public UserDAO() {
        // No need for constructor to create connection anymore
    }
    
    public User createUser(User user) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "INSERT INTO user (username, email, password, first_name, last_name, bio, " +
                        "registered_date, is_active, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getFirstName());
            statement.setString(5, user.getLastName());
            statement.setString(6, user.getBio());
            
            // Set current timestamp if registered_date is null
            if (user.getRegisteredDate() != null) {
                statement.setTimestamp(7, new Timestamp(user.getRegisteredDate().getTime()));
            } else {
                statement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            }
            
            statement.setBoolean(8, user.isActive());
            statement.setString(9, user.getRole() != null ? user.getRole() : "user");
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            
            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
            
            return user;
        } finally {
            if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
    
    public User getUserById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "SELECT * FROM user WHERE id = ?";
            
            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }
            
            return null;
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
    
    public User getUserByUsername(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "SELECT * FROM user WHERE username = ?";
            
            statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }
            
            return null;
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
    
    public User getUserByEmail(String email) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "SELECT * FROM user WHERE email = ?";
            
            statement = conn.prepareStatement(sql);
            statement.setString(1, email);
            
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }
            
            return null;
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
    
    public List<User> getAllUsers() throws SQLException {
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "SELECT * FROM user";
            
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
            
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(extractUserFromResultSet(resultSet));
            }
            
            return users;
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
    
    public boolean updateUser(User user) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "UPDATE user SET username = ?, email = ?, password = ?, first_name = ?, " +
                        "last_name = ?, bio = ?, last_login_date = ?, is_active = ?, role = ? WHERE id = ?";
            
            statement = conn.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getFirstName());
            statement.setString(5, user.getLastName());
            statement.setString(6, user.getBio());
            
            if (user.getLastLoginDate() != null) {
                statement.setTimestamp(7, new Timestamp(user.getLastLoginDate().getTime()));
            } else {
                statement.setNull(7, java.sql.Types.TIMESTAMP);
            }
            
            statement.setBoolean(8, user.isActive());
            statement.setString(9, user.getRole());
            statement.setInt(10, user.getId());
            
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
    
    public boolean deleteUser(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            // First delete RSVPs for this user (due to foreign key constraints)
            String deleteRSVPs = "DELETE FROM rsvp WHERE user_id = ?";
            statement = conn.prepareStatement(deleteRSVPs);
            statement.setInt(1, id);
            statement.executeUpdate();
            
            // Then delete the user
            String sql = "DELETE FROM user WHERE id = ?";
            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
    
    public boolean authenticateUser(String username, String password) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
            
            statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            
            resultSet = statement.executeQuery();
            return resultSet.next();
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
    
    public List<Event> getUserEvents(int userId) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            // Get events where the user is the creator
            String sql = "SELECT * FROM event WHERE created_by = ?";
            
            statement = conn.prepareStatement(sql);
            statement.setInt(1, userId);
            
            resultSet = statement.executeQuery();
            List<Event> events = new ArrayList<>();
            
            while (resultSet.next()) {
                Event event = extractEventFromResultSet(resultSet);
                events.add(event);
            }
            
            return events;
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
    
    public List<Event> getUserRSVPEvents(int userId) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            // Get events the user has RSVPed to
            String sql = "SELECT e.* FROM event e " +
                        "INNER JOIN rsvp r ON e.id = r.event_id " +
                        "WHERE r.user_id = ?";
            
            statement = conn.prepareStatement(sql);
            statement.setInt(1, userId);
            
            resultSet = statement.executeQuery();
            List<Event> events = new ArrayList<>();
            
            while (resultSet.next()) {
                Event event = extractEventFromResultSet(resultSet);
                events.add(event);
            }
            
            return events;
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
    
    public boolean updateLoginTime(int userId) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "UPDATE user SET last_login_date = ? WHERE id = ?";
            
            statement = conn.prepareStatement(sql);
            statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            statement.setInt(2, userId);
            
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
    
    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUsername(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setBio(resultSet.getString("bio"));
        user.setRegisteredDate(resultSet.getTimestamp("registered_date"));
        user.setLastLoginDate(resultSet.getTimestamp("last_login_date"));
        user.setActive(resultSet.getBoolean("is_active"));
        user.setRole(resultSet.getString("role"));
        
        return user;
    }
    
    private Event extractEventFromResultSet(ResultSet resultSet) throws SQLException {
        Event event = new Event();
        event.setId(resultSet.getInt("id"));
        event.setName(resultSet.getString("name"));
        event.setDescription(resultSet.getString("description"));
        event.setEventDate(resultSet.getTimestamp("event_date"));
        event.setLocation(resultSet.getString("location"));
        
        int createdBy = resultSet.getInt("created_by");
        if (!resultSet.wasNull()) {
            event.setCreatedBy(createdBy);
            // If the Event class has the organizer ID field, set it too
            try {
                event.setOrganizerId(createdBy);
            } catch (Exception e) {
                // Ignore if method doesn't exist
            }
        }
        
        int eventTypeId = resultSet.getInt("event_type_id");
        if (!resultSet.wasNull()) {
            event.setEventTypeId(eventTypeId);
        }
        
        event.setAttendeeCount(resultSet.getInt("attendee_count"));
        event.setCapacity(resultSet.getInt("capacity"));
        
        try {
            event.setRegistrationRequired(resultSet.getBoolean("registration_required"));
            event.setTicketPrice(resultSet.getDouble("ticket_price"));
        } catch (SQLException e) {
            // These columns might not exist in all implementations
        }
        
        event.setCreatedAt(resultSet.getTimestamp("created_at"));
        event.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        
        return event;
    }
}