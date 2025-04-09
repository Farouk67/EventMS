package com.emma.ml;

import com.emma.model.Event;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for predicting event types based on event attributes
 */
public class EventPredictor {
    
    private static final Logger LOGGER = Logger.getLogger(EventPredictor.class.getName());
    private Classifier model;
    private Instances datasetStructure;
    
    /**
     * Constructor that loads a pre-trained model
     */
    public EventPredictor() {
        try {
            this.model = ModelTrainer.loadModel();
            this.datasetStructure = FeatureExtractor.createDatasetStructure();
            LOGGER.info("EventPredictor initialized successfully with pre-trained model");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load pre-trained model. Will attempt to use default model.", e);
            tryInitializeWithDefaultModel();
        }
    }
    
    /**
     * Attempt to initialize with a default model if loading fails
     */
    private void tryInitializeWithDefaultModel() {
        try {
            // Create a default model using RandomForest
            this.model = ModelTrainer.createDefaultModel();
            this.datasetStructure = FeatureExtractor.createDatasetStructure();
            LOGGER.info("EventPredictor initialized with default model");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize with default model", e);
        }
    }
    
    /**
     * Constructor that takes a specific model
     * 
     * @param model The trained classifier to use
     */
    public EventPredictor(Classifier model) {
        this.model = model;
        this.datasetStructure = FeatureExtractor.createDatasetStructure();
    }
    
    /**
     * Predicts the event type for a given event based on its attributes
     * 
     * @param event The event to classify
     * @return The predicted event type name
     */
    public String predictEventType(Event event) {
        try {
            if (model == null) {
                LOGGER.warning("No model available for prediction, returning default event type");
                return "Conference"; // Default type if model is not available
            }
            
            // Extract features from the event
            Instance instance = FeatureExtractor.extractFeatures(event, datasetStructure);
            
            // Classify the instance
            double classIndex = model.classifyInstance(instance);
            
            // Get the predicted class name
            String predictedType = datasetStructure.classAttribute().value((int) classIndex);
            LOGGER.info("Predicted event type for event " + event.getName() + ": " + predictedType);
            
            return predictedType;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error predicting event type for event " + event.getName(), e);
            return "Conference"; // Default type if prediction fails
        }
    }
    
    /**
     * Predicts the event type for a given event and returns the probability distribution
     * 
     * @param event The event to classify
     * @return Array of probabilities for each event type
     */
    public double[] predictEventTypeProbabilities(Event event) {
        try {
            // Extract features from the event
            Instance instance = FeatureExtractor.extractFeatures(event, datasetStructure);
            
            // Get probability distribution
            return model.distributionForInstance(instance);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error getting probability distribution for event " + event.getName(), e);
            
            // Return equal probabilities for all classes if prediction fails
            int numClasses = datasetStructure.classAttribute().numValues();
            double[] equalProbs = new double[numClasses];
            for (int i = 0; i < numClasses; i++) {
                equalProbs[i] = 1.0 / numClasses;
            }
            return equalProbs;
        }
    }
    
    /**
     * Gets the names of all possible event types in the order of the probability array
     * 
     * @return Array of event type names
     */
    public String[] getEventTypeNames() {
        String[] eventTypes = new String[datasetStructure.classAttribute().numValues()];
        
        for (int i = 0; i < eventTypes.length; i++) {
            eventTypes[i] = datasetStructure.classAttribute().value(i);
        }
        
        return eventTypes;
    }
}