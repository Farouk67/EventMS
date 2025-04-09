package com.emma.service;

import com.emma.ml.EventPredictor;
import com.emma.ml.ModelTrainer;
import com.emma.model.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for ML-related operations
 */
public class MLService {
    
    private static final Logger LOGGER = Logger.getLogger(MLService.class.getName());
    private EventPredictor eventPredictor;
    
    /**
     * Constructor - initializes the event predictor
     */
    public MLService() {
        try {
            this.eventPredictor = new EventPredictor();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing EventPredictor", e);
        }
    }
    
    /**
     * Constructor with explicit EventPredictor
     * 
     * @param eventPredictor The EventPredictor to use
     */
    public MLService(EventPredictor eventPredictor) {
        this.eventPredictor = eventPredictor;
    }
    
    /**
     * Predicts the event type for a given event
     * 
     * @param event The event to predict the type for
     * @return The predicted event type
     */
    public String predictEventType(Event event) {
        if (eventPredictor == null) {
            try {
                eventPredictor = new EventPredictor();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to initialize EventPredictor", e);
                return "Conference"; // Default type
            }
        }
        
        try {
            return eventPredictor.predictEventType(event);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error predicting event type", e);
            return "Conference"; // Default type
        }
    }
    
    /**
     * Gets the prediction details with probabilities for each event type
     * 
     * @param event The event to analyze
     * @return Map of event types to probability scores
     */
    public Map<String, Double> getPredictionDetails(Event event) {
        Map<String, Double> result = new HashMap<>();
        
        if (eventPredictor == null) {
            try {
                eventPredictor = new EventPredictor();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to initialize EventPredictor", e);
                return result;
            }
        }
        
        try {
            double[] probabilities = eventPredictor.predictEventTypeProbabilities(event);
            String[] eventTypes = eventPredictor.getEventTypeNames();
            
            for (int i = 0; i < eventTypes.length; i++) {
                result.put(eventTypes[i], probabilities[i]);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error getting prediction details", e);
        }
        
        return result;
    }
    
    /**
     * Suggests an event type based on event details
     * 
     * @param name The event name
     * @param description The event description
     * @param location The event location
     * @return The suggested event type
     */
    public String suggestEventType(String name, String description, String location) {
        // Create a temporary event with the provided details
        Event tempEvent = new Event();
        tempEvent.setName(name != null ? name : "");
        tempEvent.setDescription(description != null ? description : "");
        tempEvent.setLocation(location != null ? location : "");
        
        return predictEventType(tempEvent);
    }
    
    /**
     * Retrains the model with the latest event data
     * 
     * @param events The events to train on
     * @return True if training was successful, false otherwise
     */
    public boolean retrainModel(java.util.List<Event> events) {
        try {
            // Train a new model
            weka.classifiers.Classifier model = ModelTrainer.trainModel(events);
            
            // Save the model
            ModelTrainer.saveModel(model);
            
            // Update the predictor
            eventPredictor = new EventPredictor(model);
            
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retraining model", e);
            return false;
        }
    }
}