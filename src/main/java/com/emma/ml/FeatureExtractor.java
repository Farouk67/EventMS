package com.emma.ml;

import com.emma.model.Event;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for extracting features from events for ML classification
 */
public class FeatureExtractor {
    
    private static final Logger LOGGER = Logger.getLogger(FeatureExtractor.class.getName());
    
    // Predefined locations from the dataset - extended from assignment brief
    private static final List<String> LOCATIONS = Arrays.asList(
            "London", "Manchester", "Coventry", "Birmingham", "Edinburgh", 
            "Leeds", "Cardiff", "Glasgow", "Liverpool", "Bristol", "Oxford",
            "Cambridge", "Nottingham", "Sheffield", "Newcastle", "York", "Bath",
            "Brighton", "Portsmouth", "Southampton", "Plymouth", "Leicester", "Exeter");
    
    // Month names for extracting month from date
    private static final List<String> MONTHS = Arrays.asList(
            "January", "February", "March", "April", "May", "June", 
            "July", "August", "September", "October", "November", "December");
    
    // Event types from the dataset
    private static final List<String> EVENT_TYPES = Arrays.asList(
            "Conference", "Wedding", "Workshop", "Party", "Exhibition", "Concert", "Sports", "Social", "Seminar");
    
    // Keywords for different event types
    private static final Map<String, List<String>> EVENT_TYPE_KEYWORDS = new HashMap<>();
    
    static {
        EVENT_TYPE_KEYWORDS.put("Conference", Arrays.asList(
                "conference", "summit", "tech", "innovation", "showcase", "trends", 
        "healthcare", "ai", "technology", "industry", "professional", "symposium",
        "forum", "convention", "congress", "meeting", "expo", "talks"));
        
        EVENT_TYPE_KEYWORDS.put("Wedding", Arrays.asList(
                "wedding", "celebration", "bride", "groom", "ceremony", "reception", 
                "family", "garden", "private", "couple", "marriage"));
        
        EVENT_TYPE_KEYWORDS.put("Workshop", Arrays.asList(
                "workshop", "bootcamp", "learn", "hands-on", "training", "skills", 
                "programming", "basics", "techniques", "introduction", "tutorial", "development"));
        
        EVENT_TYPE_KEYWORDS.put("Party", Arrays.asList(
                "party", "celebration", "bash", "festive", "celebrate", "graduation", 
                "birthday", "anniversary", "new year", "holiday", "social gathering"));
        
        EVENT_TYPE_KEYWORDS.put("Exhibition", Arrays.asList(
                "exhibition", "gallery", "display", "art", "showcase", "museum", "collection",
                "exhibit", "installation", "contemporary", "modern", "artistic"));
        
        EVENT_TYPE_KEYWORDS.put("Concert", Arrays.asList(
                "concert", "music", "live", "band", "performance", "stage", "tour", 
                "musical", "orchestra", "singer", "musician", "jazz", "rock", "classical"));
        
        EVENT_TYPE_KEYWORDS.put("Sports", Arrays.asList(
                "sports", "game", "match", "tournament", "competition", "athletic", 
        "team", "league", "championship", "race", "fitness", "running",
        "football", "soccer", "basketball", "baseball", "tennis"));
        
        EVENT_TYPE_KEYWORDS.put("Social", Arrays.asList(
                "social", "networking", "meetup", "community", "gathering", "mixer", 
                "club", "society", "fundraiser", "charity", "volunteer", "barbecue"));
        
        EVENT_TYPE_KEYWORDS.put("Seminar", Arrays.asList(
                "seminar", "lecture", "talk", "presentation", "speaker", "educational", 
                "academic", "class", "session", "discussion", "forum", "panel"));
    }
    
    /**
     * Creates the Weka Instances structure for the event classification dataset
     * 
     * @return Instances object with defined attributes but no data
     */
    public static Instances createDatasetStructure() {
        // Create attributes list
        ArrayList<Attribute> attributes = new ArrayList<>();
        
        // Location (nominal attribute)
        attributes.add(new Attribute("location", new ArrayList<>(LOCATIONS)));
        
        // Month (nominal attribute)
        attributes.add(new Attribute("month", new ArrayList<>(MONTHS)));
        
        // Description word counts for each event type
        for (String eventType : EVENT_TYPES) {
            attributes.add(new Attribute(eventType.toLowerCase() + "_keyword_count"));
        }
        
        // Class attribute (event type)
        attributes.add(new Attribute("event_type", new ArrayList<>(EVENT_TYPES)));
        
        // Create dataset with the defined attributes
        Instances dataset = new Instances("EventTypeClassification", attributes, 0);
        
        // Set the class index to the last attribute
        dataset.setClassIndex(dataset.numAttributes() - 1);
        
        return dataset;
    }
    
    /**
     * Extracts features from an Event object and creates a Weka Instance
     * 
     * @param event The event to extract features from
     * @param dataset The dataset structure the instance should conform to
     * @return A Weka Instance with extracted features
     */
    public static Instance extractFeatures(Event event, Instances dataset) {
        // Create a new instance
        Instance instance = new DenseInstance(dataset.numAttributes());
        instance.setDataset(dataset);
        
        try {
            // Set location attribute (safely)
            String location = event.getLocation();
            if (location != null) {
                // Find closest matching location
                String matchedLocation = findClosestLocation(location);
                instance.setValue(dataset.attribute("location"), matchedLocation);
            } else {
                // Default to first location if not found
                instance.setValue(dataset.attribute("location"), LOCATIONS.get(0));
            }
            
            // Set month attribute (safely)
            Calendar cal = Calendar.getInstance();
            if (event.getEventDate() != null) {
                cal.setTime(event.getEventDate());
            }
            int monthIndex = cal.get(Calendar.MONTH);
            instance.setValue(dataset.attribute("month"), MONTHS.get(monthIndex));
            
            // Count keywords in description
            String description = event.getDescription() != null ? event.getDescription().toLowerCase() : "";
            for (String eventType : EVENT_TYPES) {
                List<String> keywords = EVENT_TYPE_KEYWORDS.get(eventType);
                int count = 0;
                
                if (keywords != null) {
                    for (String keyword : keywords) {
                        if (description.contains(keyword.toLowerCase())) {
                            count++;
                        }
                    }
                }
                
                instance.setValue(dataset.attribute(eventType.toLowerCase() + "_keyword_count"), count);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error extracting features for event: " + event.getName(), e);
        }
        
        return instance;
    }
    
    /**
     * Finds the closest matching location from our predefined list
     * 
     * @param location The location string to match
     * @return The closest matching location from our predefined list
     */
    private static String findClosestLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            return LOCATIONS.get(0);
        }
        
        location = location.trim();
        
        // Direct match
        for (String predefinedLocation : LOCATIONS) {
            if (location.equalsIgnoreCase(predefinedLocation)) {
                return predefinedLocation;
            }
        }
        
        // Partial match (location contains a predefined location)
        for (String predefinedLocation : LOCATIONS) {
            if (location.toLowerCase().contains(predefinedLocation.toLowerCase())) {
                return predefinedLocation;
            }
        }
        
        // Predefined location contains location
        for (String predefinedLocation : LOCATIONS) {
            if (predefinedLocation.toLowerCase().contains(location.toLowerCase())) {
                return predefinedLocation;
            }
        }
        
        // Default to first location
        return LOCATIONS.get(0);
    }
    
    /**
     * Creates a training dataset from a list of events with known event types
     * 
     * @param events List of events with known event types
     * @return Weka Instances object containing the training data
     */
    public static Instances createTrainingDataset(List<Event> events) {
        Instances dataset = createDatasetStructure();
        
        for (Event event : events) {
            try {
                if (event.getType() == null || !EVENT_TYPES.contains(event.getType())) {
                    // Skip events with unknown types
                    continue;
                }
                
                Instance instance = extractFeatures(event, dataset);
                
                // Set the class value (event type)
                instance.setValue(dataset.classAttribute(), event.getType());
                
                // Add the instance to the dataset
                dataset.add(instance);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error processing event for training dataset: " + event.getName(), e);
            }
        }
        
        return dataset;
    }
}