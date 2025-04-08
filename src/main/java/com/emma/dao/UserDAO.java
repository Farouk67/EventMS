package com.emma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.emma.model.User;
import com.emma.model.Event;
import com.emma.util.DBConnectionPool;

public class UserDAO {
    private Connection connection;
    
    public UserDAO() throws SQLException {
        try {
            connection = DBConnectionPool.getConnection();
            System.out.println("UserDAO initialized with connection: " + (connection != null));
        } catch (SQLException e) {
            System.err.println("Error initializing UserDAO: " + e.getMessage());
            throw e;
        }
    }
    
    public User createUser(User user) throws SQLException {
        System.out.println("Attempting to create new user: " + user.getUsername());
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "INSERT INTO user (username, email, password, registered_date, role) VALUES (?, ?, ?, NOW(), ?)";
            
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole() != null ? user.getRole() : "user");
            
            System.out.println("Executing SQL: " + sql.replace("?", "'...'"));
            
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
            
            if (rowsAffected > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                    System.out.println("User created successfully with ID: " + user.getId());
                } else {
                    System.err.println("User created but failed to retrieve ID");
                }
            } else {
                System.err.println("Failed to create user - no rows affected");
            }
            
            return user;
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { /* ignore */ }
            if (conn != null) try { conn.close(); } catch (SQLException e) { /* ignore */ }
        }
    }
    
    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM user WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by ID: " + e.getMessage());
            throw e;
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) { /* ignore */ }
            if (statement != null) try { statement.close(); } catch (SQLException e) { /* ignore */ }
            if (conn != null && conn != this.connection) try { conn.close(); } catch (SQLException e) { /* ignore */ }
        }
        
        return null;
    }
    
    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM user WHERE username = ?";
        
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            
            System.out.println("Looking up user by username: " + username);
            
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = extractUserFromResultSet(resultSet);
                System.out.println("Found user: " + user.getUsername() + " (ID: " + user.getId() + ")");
                return user;
            } else {
                System.out.println("User not found: " + username);
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by username: " + e.getMessage());
            throw e;
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) { /* ignore */ }
            if (statement != null) try { statement.close(); } catch (SQLException e) { /* ignore */ }
            if (conn != null && conn != this.connection) try { conn.close(); } catch (SQLException e) { /* ignore */ }
        }
        
        return null;
    }
    
    public User getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM user WHERE email = ?";
        
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            statement = conn.prepareStatement(sql);
            statement.setString(1, email);
            
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by email: " + e.getMessage());
            throw e;
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) { /* ignore */ }
            if (statement != null) try { statement.close(); } catch (SQLException e) { /* ignore */ }
            if (conn != null && conn != this.connection) try { conn.close(); } catch (SQLException e) { /* ignore */ }
        }
        
        return null;
    }
    
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";
        
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                users.add(extractUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
            throw e;
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) { /* ignore */ }
            if (statement != null) try { statement.close(); } catch (SQLException e) { /* ignore */ }
            if (conn != null && conn != this.connection) try { conn.close(); } catch (SQLException e) { /* ignore */ }
        }
        
        return users;
    }
    
    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE user SET username = ?, email = ?, password = ? WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement statement = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            statement = conn.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setInt(4, user.getId());
            
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            throw e;
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) { /* ignore */ }
            if (conn != null && conn != this.connection) try { conn.close(); } catch (SQLException e) { /* ignore */ }
        }
    }
    
    public boolean deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM user WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement statement = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            throw e;
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) { /* ignore */ }
            if (conn != null && conn != this.connection) try { conn.close(); } catch (SQLException e) { /* ignore */ }
        }
    }
    
    public User authenticateUser(String username, String password) throws SQLException {
        System.out.println("Attempting to authenticate user: " + username);
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            System.out.println("Executing SQL: " + sql.replace("?", "'" + username + "'") + " [password masked]");
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password")); // Should be masked in a real app
                
                // Try to get optional fields
                try {
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                } catch (SQLException e) {
                    // Ignore if columns don't exist
                }
                
                try {
                    user.setRole(rs.getString("role"));
                } catch (SQLException e) {
                    user.setRole("user"); // Default role
                }
                
                System.out.println("Authentication successful for user: " + username + " (ID: " + user.getId() + ")");
                return user;
            } else {
                System.out.println("Authentication failed for user: " + username + " - No matching user found");
            }
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { /* ignore */ }
            if (conn != null) try { conn.close(); } catch (SQLException e) { /* ignore */ }
        }
        
        return null;
    }
    public List<Event> getUserEvents(int userId) throws SQLException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT e.* FROM event e WHERE e.created_by = ?";
        
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            statement = conn.prepareStatement(sql);
            statement.setInt(1, userId);
            
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Event event = new Event();
                event.setId(resultSet.getInt("id"));
                event.setName(resultSet.getString("name"));
                event.setDescription(resultSet.getString("description"));
                event.setEventDate(resultSet.getTimestamp("event_date"));
                event.setLocation(resultSet.getString("location"));
                event.setOrganizerId(resultSet.getInt("created_by"));
                
                // Get type name if available
                try {
                    String typeName = getEventTypeName(resultSet.getInt("event_type_id"));
                    event.setType(typeName);
                } catch (Exception e) {
                    // Ignore if type name cannot be retrieved
                }
                
                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Error getting user events: " + e.getMessage());
            throw e;
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) { /* ignore */ }
            if (statement != null) try { statement.close(); } catch (SQLException e) { /* ignore */ }
            if (conn != null && conn != this.connection) try { conn.close(); } catch (SQLException e) { /* ignore */ }
        }
        
        return events;
    }
    
    private String getEventTypeName(int typeId) throws SQLException {
        String sql = "SELECT name FROM event_type WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            statement = conn.prepareStatement(sql);
            statement.setInt(1, typeId);
            
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            }
            return "Other";
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) { /* ignore */ }
            if (statement != null) try { statement.close(); } catch (SQLException e) { /* ignore */ }
            if (conn != null && conn != this.connection) try { conn.close(); } catch (SQLException e) { /* ignore */ }
        }
    }
    
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            // Log the exception
            e.printStackTrace();
        }
    }
    
    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUsername(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        
        // Try to extract additional fields if they exist
        try {
            user.setFirstName(resultSet.getString("first_name"));
            user.setLastName(resultSet.getString("last_name"));
            user.setRegisteredDate(resultSet.getTimestamp("registered_date"));
            user.setLastLoginDate(resultSet.getTimestamp("last_login_date"));
            user.setActive(resultSet.getBoolean("is_active"));
            user.setRole(resultSet.getString("role"));
        } catch (SQLException e) {
            // Ignore if additional fields don't exist
        }
        
        return user;
    }
}