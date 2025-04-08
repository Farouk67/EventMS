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

import com.emma.model.User;
import com.emma.util.DBConnectionPool;

/**
 * Repository class to handle User data operations using database storage
 */
public class UserRepository {
    
    /**
     * Find all users in the repository
     * 
     * @return List of all users
     */
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DBConnectionPool.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM user")) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all users: " + e.getMessage());
            e.printStackTrace();
        }
        
        return users;
    }
    
    /**
     * Find a user by their ID
     * 
     * @param id The ID of the user to find
     * @return Optional containing the user if found, empty otherwise
     */
    public Optional<User> findById(int id) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM user WHERE id = ?")) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
    /**
     * Find a user by their username
     * 
     * @param username The username to search for
     * @return The user with the given username or null if not found
     */
    public User findByUsername(String username) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM user WHERE username = ?")) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by username: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Find a user by their email
     * 
     * @param email The email to search for
     * @return The user with the given email or null if not found
     */
    public User findByEmail(String email) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM user WHERE email = ?")) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by email: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Check if a user with the given ID exists
     * 
     * @param id The ID to check
     * @return true if the user exists, false otherwise
     */
    public boolean existsById(int id) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT 1 FROM user WHERE id = ?")) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error checking if user exists: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Save a user (create or update)
     * 
     * @param user The user to save
     * @return The saved user
     */
    public User save(User user) {
        try (Connection conn = DBConnectionPool.getConnection()) {
            if (user.getId() <= 0) {
                // Insert new user
                String sql = "INSERT INTO user (username, email, password, first_name, last_name, " +
                             "bio, registered_date, last_login_date, is_active, role) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setString(1, user.getUsername());
                    pstmt.setString(2, user.getEmail());
                    pstmt.setString(3, user.getPassword());
                    pstmt.setString(4, user.getFirstName());
                    pstmt.setString(5, user.getLastName());
                    pstmt.setString(6, user.getBio());
                    
                    // Set timestamps
                    if (user.getRegisteredDate() != null) {
                        pstmt.setTimestamp(7, new Timestamp(user.getRegisteredDate().getTime()));
                    } else {
                        pstmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
                    }
                    
                    if (user.getLastLoginDate() != null) {
                        pstmt.setTimestamp(8, new Timestamp(user.getLastLoginDate().getTime()));
                    } else {
                        pstmt.setNull(8, java.sql.Types.TIMESTAMP);
                    }
                    
                    pstmt.setBoolean(9, user.isActive());
                    pstmt.setString(10, user.getRole() != null ? user.getRole() : "user");
                    
                    pstmt.executeUpdate();
                    
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            user.setId(generatedKeys.getInt(1));
                        }
                    }
                }
            } else {
                // Update existing user
                String sql = "UPDATE user SET username = ?, email = ?, password = ?, first_name = ?, " +
                             "last_name = ?, bio = ?, last_login_date = ?, is_active = ?, role = ? " +
                             "WHERE id = ?";
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, user.getUsername());
                    pstmt.setString(2, user.getEmail());
                    pstmt.setString(3, user.getPassword());
                    pstmt.setString(4, user.getFirstName());
                    pstmt.setString(5, user.getLastName());
                    pstmt.setString(6, user.getBio());
                    
                    // Set last login timestamp
                    if (user.getLastLoginDate() != null) {
                        pstmt.setTimestamp(7, new Timestamp(user.getLastLoginDate().getTime()));
                    } else {
                        pstmt.setNull(7, java.sql.Types.TIMESTAMP);
                    }
                    
                    pstmt.setBoolean(8, user.isActive());
                    pstmt.setString(9, user.getRole() != null ? user.getRole() : "user");
                    pstmt.setInt(10, user.getId());
                    
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return user;
    }
    
    /**
     * Delete a user by their ID
     * 
     * @param id The ID of the user to delete
     */
    public void deleteById(int id) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM user WHERE id = ?")) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Map a database result set to a User object
     * 
     * @param rs The result set
     * @return Mapped User object
     * @throws SQLException if a database access error occurs
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setBio(rs.getString("bio"));
        user.setRegisteredDate(rs.getTimestamp("registered_date"));
        user.setLastLoginDate(rs.getTimestamp("last_login_date"));
        user.setActive(rs.getBoolean("is_active"));
        user.setRole(rs.getString("role"));
        
        return user;
    }
}