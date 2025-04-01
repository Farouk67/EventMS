package com.emma.ml;

import com.emma.model.Event;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

public class EventPredictor {
    
    private Classifier model;
    private Instances datasetStructure;
    
    /**
     * Constructor that loads a pre-trained model
     * 
     * @throws Exception if loading the model fails
     */
    public EventPredictor() throws Exception {
        this.model = ModelTrainer.loadModel();
        this.datasetStructure = FeatureExtractor.createDatasetStructure();
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
     * @throws Exception if prediction fails
     */
    public String predictEventType(Event event) throws Exception {
        // Extract features from the event
        Instance instance = FeatureExtractor.extractFeatures(event, datasetStructure);
        
        // Classify the instance
        double classIndex = model.classifyInstance(instance);
        
        // Get the predicted class name
        return datasetStructure.classAttribute().value((int) classIndex);
    }
    
    /**
     * Predicts the event type for a given event and returns the probability distribution
     * 
     * @param event The event to classify
     * @return Array of probabilities for each event type
     * @throws Exception if prediction fails
     */
    public double[] predictEventTypeProbabilities(Event event) throws Exception {
        // Extract features from the event
        Instance instance = FeatureExtractor.extractFeatures(event, datasetStructure);
        
        // Get probability distribution
        return model.distributionForInstance(instance);
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