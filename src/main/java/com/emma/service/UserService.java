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
    
    private final UserRepository userRepository;
    private final EventService eventService;
    private final RSVPService rsvpService;
    
    /**
     * Constructor with explicit dependencies
     * 
     * @param userRepository The User repository
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        // Get service instances through ServiceFactory to avoid direct instantiation
        this.eventService = ServiceFactory.getEventService();
        this.rsvpService = ServiceFactory.getRsvpService();
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
        
        System.out.println("Found " + rsvpedEvents.size() + " RSVP'd events for user " + userId);
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
        System.out.println("User not found: " + username);
        return null;
    }
    
    // For debugging
    System.out.println("Found user: " + user.getUsername() + ", Stored password: [" + user.getPassword() + "]");
    System.out.println("Input password: [" + password + "]");
    
    // Try direct password comparison first (for development/testing)
    if (password.equals(user.getPassword())) {
        // Update last login date
        user.setLastLoginDate(new Date());
        userRepository.save(user);
        return user;
    }
    
    // Then try hashed password comparison
    String hashedPassword = hashPassword(password);
    System.out.println("Hashed input password: [" + hashedPassword + "]");
    
    if (hashedPassword != null && hashedPassword.equals(user.getPassword())) {
        // Update last login date
        user.setLastLoginDate(new Date());
        userRepository.save(user);
        return user;
    }
    
    System.out.println("Password mismatch for user: " + username);
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