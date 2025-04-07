package com.emma.service;

import com.emma.repository.EventRepository;
import com.emma.repository.RSVPRepository;
import com.emma.repository.UserRepository;

/**
 * Factory for service objects to manage dependencies
 */
public class ServiceFactory {
    
    // Repositories
    private static final EventRepository eventRepository = new EventRepository();
    private static final RSVPRepository rsvpRepository = new RSVPRepository();
    private static final UserRepository userRepository = new UserRepository();
    
    // Services with explicit dependency injection
    private static EventService eventService;
    private static RSVPService rsvpService;
    private static UserService userService;
    
    // Initialize services
    static {
        initializeServices();
    }
    
    /**
     * Initialize all services with their dependencies in the correct order to avoid circular dependencies
     */
    private static void initializeServices() {
        // First create EventService without RSVPService dependency
        eventService = new EventService(eventRepository);
        
        // Then create RSVPService
        rsvpService = new RSVPService(rsvpRepository, eventRepository);
        
        // Now set RSVPService in EventService
        eventService.setRsvpService(rsvpService);
        
        // Finally create UserService
        userService = new UserService(userRepository);
    }
    
    /**
     * Get the EventService instance
     * @return EventService instance
     */
    public static EventService getEventService() {
        if (eventService == null) {
            initializeServices();
        }
        return eventService;
    }
    
    /**
     * Get the RSVPService instance
     * @return RSVPService instance
     */
    public static RSVPService getRsvpService() {
        if (rsvpService == null) {
            initializeServices();
        }
        return rsvpService;
    }
    
    /**
     * Get the UserService instance
     * @return UserService instance
     */
    public static UserService getUserService() {
        if (userService == null) {
            initializeServices();
        }
        return userService;
    }
    
    /**
     * Reset all services (for testing purposes)
     */
    public static void resetServices() {
        eventService = null;
        rsvpService = null;
        userService = null;
        initializeServices();
    }
}