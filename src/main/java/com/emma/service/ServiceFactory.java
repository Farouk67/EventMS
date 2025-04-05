package com.emma.service;

/**
 * Factory for service objects to avoid circular dependencies
 */
public class ServiceFactory {
    
    private static EventService eventService;
    private static RSVPService rsvpService;
    private static UserService userService;
    
    // Initialize services
    static {
        initializeServices();
    }
    
    /**
     * Initialize all services and their relationships
     */
    private static void initializeServices() {
        // Create service instances
        eventService = new EventService();
        rsvpService = new RSVPService();
        userService = new UserService();
        
        // Set up dependencies
        rsvpService.setEventService(eventService);
       
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