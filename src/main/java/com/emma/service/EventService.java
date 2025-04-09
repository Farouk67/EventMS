package com.emma.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.emma.model.Event;
import com.emma.repository.EventRepository;
import com.emma.ml.EventPredictor;

/**
 * Service class for handling Event-related operations
 */
public class EventService {

    // Repositories for database operations
    private final EventRepository eventRepository;
    private RSVPService rsvpService; // No longer final, will be set after initialization
    private EventPredictor eventPredictor;// ML service for event type prediction
    
    /**
     * Constructor with explicit dependencies
     * 
     * @param eventRepository The Event repository
     */
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        try {
            this.eventPredictor = new EventPredictor();
        } catch (Exception e) {
            System.err.println("Failed to initialize EventPredictor: " + e.getMessage());
        }
    }
    /**
     * Set the RSVPService (to be called after initialization)
     * 
     * @param rsvpService The RSVP service
     */
    public void setRsvpService(RSVPService rsvpService) {
        this.rsvpService = rsvpService;
    }
    
    /**
     * Retrieves all events from the system
     * 
     * @return List of all events
     */
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    /**
     * Gets all unique event types in the system
     * 
     * @return List of event type strings
     */
    public List<String> getAllEventTypes() {
        List<Event> events = eventRepository.findAll();
        Set<String> eventTypes = new HashSet<>();
        
        for (Event event : events) {
            if (event.getType() != null && !event.getType().isEmpty()) {
                eventTypes.add(event.getType());
            }
        }
        
        // Add default event types if none exist yet
        if (eventTypes.isEmpty()) {
            eventTypes.add("Conference");
            eventTypes.add("Workshop");
            eventTypes.add("Seminar");
            eventTypes.add("Party");
            eventTypes.add("Concert");
            eventTypes.add("Exhibition");
            eventTypes.add("Sports");
            eventTypes.add("Social");
            eventTypes.add("Other");
        }
        
        return new ArrayList<>(eventTypes);
    }
    
    /**
     * Filters events by their type
     * 
     * @param eventType The type of events to retrieve
     * @return List of events matching the given type
     */
    public List<Event> getEventsByType(String eventType) {
        if (eventType == null || eventType.isEmpty()) {
            return new ArrayList<>();
        }
        
        return eventRepository.findByType(eventType);
    }

    /**
     * Gets a specific event by its ID
     * 
     * @param id The ID of the event to retrieve
     * @return The event with the given ID, or null if not found
     */
    public Event getEvent(int id) {
        return eventRepository.findById(id).orElse(null);
    }

    /**
     * Creates a new event in the system
     * 
     * @param newEvent The event to create
     */
    public void createEvent(Event newEvent) {
        if (newEvent != null) {
            // Set default values if not provided
            if (newEvent.getAttendeeCount() == 0) {
                newEvent.setAttendeeCount(0);
            }
            
            // Use ML to predict event type if not provided
            if (newEvent.getType() == null || newEvent.getType().isEmpty()) {
    if (eventPredictor != null) {
        try {
            String predictedType = eventPredictor.predictEventType(newEvent);
            newEvent.setType(predictedType);
            System.out.println("ML predicted event type: " + predictedType);
        } catch (Exception e) {
            System.err.println("ML prediction failed, using fallback method: " + e.getMessage());
            newEvent.setType(getRecommendedEventType(newEvent));
        }
    } else {
        newEvent.setType(getRecommendedEventType(newEvent));
    }
}
            
            eventRepository.save(newEvent);
        }
    }

    /**
     * Updates an existing event
     * 
     * @param event The event with updated information
     */
    public void updateEvent(Event event) {
        if (event != null && event.getId() > 0) {
            if (eventRepository.existsById(event.getId())) {
                eventRepository.save(event);
            }
        }
    }

    /**
     * Deletes an event by its ID
     * 
     * @param id The ID of the event to delete
     */
    public void deleteEvent(int id) {
        if (eventRepository.existsById(id)) {
            // Delete associated RSVPs first
            if (rsvpService != null) {
                List<Integer> rsvpIds = rsvpService.findRSVPsByEventId(id)
                    .stream()
                    .map(rsvp -> rsvp.getId())
                    .collect(Collectors.toList());
                
                // Delete each RSVP individually
                rsvpIds.forEach(rsvpId -> rsvpService.deleteRSVP(rsvpId));
            }
            
            // Then delete the event
            eventRepository.deleteById(id);
        }
    }

    /**
     * Gets upcoming events (events with dates in the future)
     * 
     * @return List of upcoming events
     */
    public List<Event> getUpcomingEvents() {
        // This calls the database method to get upcoming events
        List<Event> upcomingEvents = eventRepository.findUpcomingEvents();
        
        // If the database method failed or returned no results, 
        // fallback to filtering events in memory
        if (upcomingEvents.isEmpty()) {
            System.out.println("No upcoming events found in database, falling back to in-memory filtering");
            
            // Get the current date
            Date now = new Date();
            
            // Get all events and filter for those in the future
            upcomingEvents = eventRepository.findAll().stream()
                .filter(event -> event.getEventDate() != null && event.getEventDate().after(now))
                .sorted((e1, e2) -> e1.getEventDate().compareTo(e2.getEventDate()))
                .limit(6)
                .collect(Collectors.toList());
            
            System.out.println("Found " + upcomingEvents.size() + " upcoming events via fallback method");
        }
        
        return upcomingEvents;
    }
    
    /**
     * Searches for events based on multiple criteria
     * 
     * @param keyword The search keyword (matches against name or description)
     * @param type The event type to filter by
     * @param location The location to filter by
     * @param date The date to filter by
     * @return List of events matching the search criteria
     */
    public List<Event> searchEvents(String keyword, String type, String location, Date date) {
        List<Event> allEvents = eventRepository.findAll();
        List<Event> filteredEvents = new ArrayList<>(allEvents);
        
        // Filter by keyword (name or description)
        if (keyword != null && !keyword.isEmpty()) {
            String lowercaseKeyword = keyword.toLowerCase();
            filteredEvents = filteredEvents.stream()
                .filter(event -> 
                    (event.getName() != null && event.getName().toLowerCase().contains(lowercaseKeyword)) ||
                    (event.getDescription() != null && event.getDescription().toLowerCase().contains(lowercaseKeyword)))
                .collect(Collectors.toList());
        }
        
        // Filter by type
        if (type != null && !type.isEmpty()) {
            filteredEvents = filteredEvents.stream()
                .filter(event -> type.equals(event.getType()))
                .collect(Collectors.toList());
        }
        
        // Filter by location
        if (location != null && !location.isEmpty()) {
            filteredEvents = filteredEvents.stream()
                .filter(event -> location.equals(event.getLocation()))
                .collect(Collectors.toList());
        }
        
        // Filter by date
        if (date != null) {
            filteredEvents = filteredEvents.stream()
                .filter(event -> {
                    if (event.getEventDate() == null) {
                        return false;
                    }
                    // Compare just the date part, ignoring time
                    return isSameDay(event.getEventDate(), date);
                })
                .collect(Collectors.toList());
        }
        
        return filteredEvents;
    }
    
    /**
     * Helper method to compare if two dates are the same day
     */
    private boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        cal1.setTime(date1);
        
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal2.setTime(date2);
        
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
               cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH) &&
               cal1.get(java.util.Calendar.DAY_OF_MONTH) == cal2.get(java.util.Calendar.DAY_OF_MONTH);
    }
    
    /**
     * Gets all unique locations where events are held
     * 
     * @return List of location strings
     */
    public List<String> getAllLocations() {
        List<Event> events = eventRepository.findAll();
        Set<String> locations = new HashSet<>();
        
        for (Event event : events) {
            if (event.getLocation() != null && !event.getLocation().isEmpty()) {
                locations.add(event.getLocation());
            }
        }
        
        return new ArrayList<>(locations);
    }
    
   /**
     * Recommends an event type based on event details using rule-based approach
     * This serves as a fallback when ML prediction fails
     * 
     * @param event The event to analyze
     * @return Recommended event type
     */
    public String getRecommendedEventType(Event event) {
        if (event == null || event.getName() == null || event.getDescription() == null) {
            return "Other";
        }
        
        String nameAndDesc = (event.getName() + " " + event.getDescription()).toLowerCase();
        
        if (nameAndDesc.contains("conference") || nameAndDesc.contains("meeting") || nameAndDesc.contains("summit")) {
            return "Conference";
        } else if (nameAndDesc.contains("concert") || nameAndDesc.contains("music") || nameAndDesc.contains("performance")) {
            return "Concert";
        } else if (nameAndDesc.contains("workshop") || nameAndDesc.contains("training") || nameAndDesc.contains("class")) {
            return "Workshop";
        } else if (nameAndDesc.contains("party") || nameAndDesc.contains("celebration") || nameAndDesc.contains("social")) {
            return "Social";
        } else if (nameAndDesc.contains("sports") || nameAndDesc.contains("game") || nameAndDesc.contains("match")) {
            return "Sports";
        } else if (nameAndDesc.contains("exhibition") || nameAndDesc.contains("expo") || nameAndDesc.contains("showcase")) {
            return "Exhibition";
        } else {
            return "Other";
        }
    }
    
    /**
 * Uses machine learning to get detailed prediction probabilities for an event
 * 
 * @param event The event to analyze
 * @return Map of event types to probability scores
 */
public java.util.Map<String, Double> getEventTypePredictions(Event event) {
    if (eventPredictor == null) {
        return new java.util.HashMap<>();
    }
    
    try {
        double[] probabilities = eventPredictor.predictEventTypeProbabilities(event);
        String[] eventTypes = eventPredictor.getEventTypeNames();
        
        java.util.Map<String, Double> result = new java.util.HashMap<>();
        for (int i = 0; i < eventTypes.length; i++) {
            result.put(eventTypes[i], probabilities[i]);
        }
        return result;
    } catch (Exception e) {
        System.err.println("Error getting prediction details: " + e.getMessage());
        return new java.util.HashMap<>();
    }
}

/**
 * Retrain the ML model using all events in the system
 * 
 * @return true if retraining was successful, false otherwise
 */
public boolean retrainMLModel() {
    try {
        List<Event> allEvents = getAllEvents();
        weka.classifiers.Classifier model = com.emma.ml.ModelTrainer.trainModel(allEvents);
        com.emma.ml.ModelTrainer.saveModel(model);
        this.eventPredictor = new EventPredictor(model);
        return true;
    } catch (Exception e) {
        System.err.println("Error retraining ML model: " + e.getMessage());
        return false;
    }
}
}