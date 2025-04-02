package com.emma.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.emma.model.User;

/**
 * Repository class to handle User data operations
 */
public class UserRepository {
    
    // In-memory database to store users
    private static List<User> users = new ArrayList<>();
    private static int nextId = 1;
    
    /**
     * Find all users in the repository
     * 
     * @return List of all users
     */
    public List<User> findAll() {
        return new ArrayList<>(users);
    }
    
    /**
     * Find a user by their ID
     * 
     * @param id The ID of the user to find
     * @return Optional containing the user if found, empty otherwise
     */
    public Optional<User> findById(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }
    
    /**
     * Find a user by their username
     * 
     * @param username The username to search for
     * @return The user with the given username or null if not found
     */
    public User findByUsername(String username) {
        return users.stream()
                .filter(user -> username.equals(user.getUsername()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Find a user by their email
     * 
     * @param email The email to search for
     * @return The user with the given email or null if not found
     */
    public User findByEmail(String email) {
        return users.stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Check if a user with the given ID exists
     * 
     * @param id The ID to check
     * @return true if the user exists, false otherwise
     */
    public boolean existsById(int id) {
        return users.stream().anyMatch(user -> user.getId() == id);
    }
    
    /**
     * Save a user (create or update)
     * 
     * @param user The user to save
     * @return The saved user
     */
    public User save(User user) {
        if (user.getId() <= 0) {
            // New user
            user.setId(nextId++);
            users.add(user);
        } else {
            // Update existing user
            deleteById(user.getId());
            users.add(user);
        }
        return user;
    }
    
    /**
     * Delete a user by their ID
     * 
     * @param id The ID of the user to delete
     */
    public void deleteById(int id) {
        users.removeIf(user -> user.getId() == id);
    }
}