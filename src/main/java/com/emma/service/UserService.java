package com.emma.service;

import com.emma.model.Event;
import com.emma.model.RSVP;
import com.emma.model.User;
import com.emma.repository.UserRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for handling User-related operations
 */
public class UserService {
    
    private UserRepository userRepository;
    
    /**
     * Constructor
     */
    public UserService() {
        this.userRepository = new UserRepository();
    }
    
    /**
     * Get a user by their ID
     * 
     * @param userId The ID of the user to retrieve
     * @return The user with the given ID, or null if not found
     */
    public User getUserById(Integer userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId).orElse(null);
    }
    
    /**
     * Get all events created by a specific user
     * 
     * @param userId The ID of the user whose events to retrieve
     * @return List of events created by the user
     */
    public List<Event> getUserEvents(Integer userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        
        // Get the event service from the factory
        EventService eventService = ServiceFactory.getEventService();
        
        // Get all events and filter by organizer ID
        List<Event> allEvents = eventService.getAllEvents();
        return allEvents.stream()
                .filter(event -> userId.equals(event.getOrganizerId()))
                .collect(Collectors.toList());
    }
    
    /**
     * Get all events that a user has RSVPed to
     * 
     * @param userId The ID of the user whose RSVPed events to retrieve
     * @return List of events the user has RSVPed to
     */
    public List<Event> getUserRSVPEvents(Integer userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        
        // Get the services from the factory
        RSVPService rsvpService = ServiceFactory.getRsvpService();
        EventService eventService = ServiceFactory.getEventService();
        
        // Get all of the user's RSVPs
        List<RSVP> userRSVPs = rsvpService.findRSVPsByUserId(userId);
        
        // Create a list of the events for each RSVP
        List<Event> rsvpedEvents = new ArrayList<>();
        for (RSVP rsvp : userRSVPs) {
            Event event = eventService.getEvent(rsvp.getEventId());
            if (event != null) {
                rsvpedEvents.add(event);
            }
        }
        
        return rsvpedEvents;
    }
    
    /**
     * Authenticate a user with username and password
     * 
     * @param username The username to check
     * @param password The password to verify
     * @return The authenticated user or null if authentication fails
     */
    public User authenticateUser(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return null;
        }
        
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        
        // Verify password
        String hashedPassword = hashPassword(password);
        if (hashedPassword != null && hashedPassword.equals(user.getPassword())) {
            // Update last login date
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            return user;
        }
        
        return null;
    }
    
    /**
     * Register a new user in the system
     * 
     * @param user The user to register
     * @throws IllegalArgumentException if username or email is already taken
     */
    public void registerUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        // Check if username is taken
        if (isUsernameTaken(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        
        // Check if email is taken
        if (isEmailTaken(user.getEmail())) {
            throw new IllegalArgumentException("Email is already taken");
        }
        
        // Hash the password
        String hashedPassword = hashPassword(user.getPassword());
        if (hashedPassword != null) {
            user.setPassword(hashedPassword);
        }
        
        // Set registration date
        user.setRegisteredDate(new Date());
        
        // Set active status
        user.setActive(true);
        
        // Set default role if not specified
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("user");
        }
        
        // Save the user
        userRepository.save(user);
    }
    
    /**
     * Check if an email is already taken by another user
     * 
     * @param email The email to check
     * @return true if the email is already taken, false otherwise
     */
    public boolean isEmailTaken(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        
        return userRepository.findByEmail(email) != null;
    }
    
    /**
     * Check if a username is already taken by another user
     * 
     * @param username The username to check
     * @return true if the username is already taken, false otherwise
     */
    public boolean isUsernameTaken(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        
        return userRepository.findByUsername(username) != null;
    }
    
    /**
     * Update a user's profile information
     * 
     * @param user The user with updated information
     */
    public void updateUserProfile(User user) {
        if (user == null || user.getId() <= 0) {
            throw new IllegalArgumentException("Invalid user");
        }
        
        User existingUser = getUserById(user.getId());
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        // Don't allow changing username or email to one that's already taken
        if (!existingUser.getUsername().equals(user.getUsername()) && isUsernameTaken(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        
        if (!existingUser.getEmail().equals(user.getEmail()) && isEmailTaken(user.getEmail())) {
            throw new IllegalArgumentException("Email is already taken");
        }
        
        // Don't update password if it's empty (meaning no change)
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(existingUser.getPassword());
        } else {
            // Hash the new password
            String hashedPassword = hashPassword(user.getPassword());
            if (hashedPassword != null) {
                user.setPassword(hashedPassword);
            }
        }
        
        // Preserve registration date
        user.setRegisteredDate(existingUser.getRegisteredDate());
        
        // Update the user
        userRepository.save(user);
    }
    
    /**
     * Change a user's password
     * 
     * @param userId The ID of the user
     * @param currentPassword The current password for verification
     * @param newPassword The new password to set
     * @return true if password was changed successfully, false otherwise
     */
    public boolean changePassword(int userId, String currentPassword, String newPassword) {
        User user = getUserById(userId);
        if (user == null) {
            return false;
        }
        
        // Verify current password
        String hashedCurrentPassword = hashPassword(currentPassword);
        if (hashedCurrentPassword == null || !hashedCurrentPassword.equals(user.getPassword())) {
            return false;
        }
        
        // Set new password
        String hashedNewPassword = hashPassword(newPassword);
        if (hashedNewPassword != null) {
            user.setPassword(hashedNewPassword);
            userRepository.save(user);
            return true;
        }
        
        return false;
    }
    
    /**
     * Delete a user account
     * 
     * @param userId The ID of the user to delete
     */
    public void deleteUser(int userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        }
    }
    
    /**
     * Hash a password using SHA-256
     * 
     * @param password The password to hash
     * @return The hashed password or null if hashing fails
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}