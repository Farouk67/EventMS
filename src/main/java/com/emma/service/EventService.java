package com.emma.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.emma.model.Event;
import com.emma.repository.EventRepository;

public class EventService {

    // Create instance of repository
    private EventRepository eventRepository;
    
    /**
     * Constructor
     */
    public EventService() {
        this.eventRepository = new EventRepository();
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
            eventRepository.deleteById(id);
        }
    }

    /**
     * Increases the attendee count for an event
     * 
     * @param eventId The ID of the event to update
     */
    public void incrementAttendeeCount(int eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event != null) {
            event.setAttendeeCount(event.getAttendeeCount() + 1);
            eventRepository.save(event);
        }
    }

    /**
     * Decreases the attendee count for an event
     * 
     * @param eventId The ID of the event to update
     */
    public void decrementAttendeeCount(int eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event != null && event.getAttendeeCount() > 0) {
            event.setAttendeeCount(event.getAttendeeCount() - 1);
            eventRepository.save(event);
        }
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
                    if (event.getDate() == null) {
                        return false;
                    }
                    // Compare just the date part, ignoring time
                    return isSameDay(event.getDate(), date);
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
     * Recommends an event type based on event details
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
}