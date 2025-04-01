package com.emma.ml;

import com.emma.model.Event;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;

public class FeatureExtractor {
    
    // Predefined locations from the dataset
    private static final List<String> LOCATIONS = Arrays.asList(
            "London", "Manchester", "Coventry", "Birmingham", "Edinburgh", 
            "Leeds", "Cardiff", "Glasgow", "Liverpool");
    
    // Month names for extracting month from date
    private static final List<String> MONTHS = Arrays.asList(
            "January", "February", "March", "April", "May", "June", 
            "July", "August", "September", "October", "November", "December");
    
    // Event types from the dataset
    private static final List<String> EVENT_TYPES = Arrays.asList(
            "Conference", "Wedding", "Workshop", "Party");
    
    // Keywords for different event types
    private static final Map<String, List<String>> EVENT_TYPE_KEYWORDS = new HashMap<>();
    
    static {
        EVENT_TYPE_KEYWORDS.put("Conference", Arrays.asList(
                "conference", "summit", "tech", "innovation", "showcase", "trends", 
                "healthcare", "ai", "technology", "industry", "professional"));
        
        EVENT_TYPE_KEYWORDS.put("Wedding", Arrays.asList(
                "wedding", "celebration", "bride", "groom", "ceremony", "reception", 
                "family", "garden", "private", "couple"));
        
        EVENT_TYPE_KEYWORDS.put("Workshop", Arrays.asList(
                "workshop", "bootcamp", "learn", "hands-on", "training", "skills", 
                "programming", "basics", "techniques", "introduction", "tutorial"));
        
        EVENT_TYPE_KEYWORDS.put("Party", Arrays.asList(
                "party", "celebration", "bash", "festive", "celebrate", "graduation", 
                "birthday", "anniversary", "new year", "holiday"));
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
        
        // Set location attribute
        String location = event.getLocation();
        if (LOCATIONS.contains(location)) {
            instance.setValue(dataset.attribute("location"), location);
        } else {
            // Default to first location if not found
            instance.setValue(dataset.attribute("location"), LOCATIONS.get(0));
        }
        
        // Set month attribute
        Calendar cal = Calendar.getInstance();
        cal.setTime(event.getDate());
        int monthIndex = cal.get(Calendar.MONTH);
        instance.setValue(dataset.attribute("month"), MONTHS.get(monthIndex));
        
        // Count keywords in description
        String description = event.getDescription().toLowerCase();
        for (String eventType : EVENT_TYPES) {
            List<String> keywords = EVENT_TYPE_KEYWORDS.get(eventType);
            int count = 0;
            
            for (String keyword : keywords) {
                if (description.contains(keyword.toLowerCase())) {
                    count++;
                }
            }
            
            instance.setValue(dataset.attribute(eventType.toLowerCase() + "_keyword_count"), count);
        }
        
        return instance;
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
            Instance instance = extractFeatures(event, dataset);
            
            // Set the class value (event type)
            instance.setValue(dataset.classAttribute(), event.getEventTypeName());
            
            // Add the instance to the dataset
            dataset.add(instance);
        }
        
        return dataset;
    }
}