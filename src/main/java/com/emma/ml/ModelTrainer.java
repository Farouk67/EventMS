package com.emma.ml;

import com.emma.model.Event;
import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.Utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ModelTrainer {
    
    private static final String MODEL_FILE_PATH = "event_classification_model.model";
    
    /**
     * Trains a classification model using the provided events data
     * 
     * @param trainingEvents List of events with known event types for training
     * @return The trained classifier
     * @throws Exception if training fails
     */
    public static Classifier trainModel(List<Event> trainingEvents) throws Exception {
        // Create the training dataset
        Instances trainingData = FeatureExtractor.createTrainingDataset(trainingEvents);
        
        // Initialize the classifier (using RandomForest as it works well for categorical data)
        RandomForest classifier = new RandomForest();
        
        // Configure the classifier using options
        // -I: number of trees to build
        // -K: number of features to consider (0 means log_2(numFeatures) + 1)
        // -depth: maximum depth of the trees (0 means unlimited)
        String[] options = Utils.splitOptions("-I 100 -K 0 -depth 0");
        classifier.setOptions(options);
        
        // Build the classifier
        classifier.buildClassifier(trainingData);
        
        return classifier;
    }
    
    /**
     * Saves the trained model to a file
     * 
     * @param model The trained classifier to save
     * @throws Exception if saving fails
     */
    public static void saveModel(Classifier model) throws Exception {
        SerializationHelper.write(MODEL_FILE_PATH, model);
    }
    
    /**
     * Loads a previously trained model from a file
     * 
     * @return The loaded classifier
     * @throws Exception if loading fails
     */
    public static Classifier loadModel() throws Exception {
        File modelFile = new File(MODEL_FILE_PATH);
        
        if (!modelFile.exists()) {
            throw new IOException("Model file not found. Please train the model first.");
        }
        
        return (Classifier) SerializationHelper.read(MODEL_FILE_PATH);
    }
    
    /**
     * Evaluates the model's performance on a test set
     * 
     * @param model The trained classifier
     * @param testEvents List of events with known event types for testing
     * @return Accuracy of the model on the test set (0.0 to 1.0)
     * @throws Exception if evaluation fails
     */
    public static double evaluateModel(Classifier model, List<Event> testEvents) throws Exception {
        Instances testData = FeatureExtractor.createTrainingDataset(testEvents);
        
        int correct = 0;
        for (int i = 0; i < testData.numInstances(); i++) {
            double predicted = model.classifyInstance(testData.instance(i));
            double actual = testData.instance(i).classValue();
            
            if (predicted == actual) {
                correct++;
            }
        }
        
        return (double) correct / testData.numInstances();
    }
    
    /**
     * Generates additional training data from the provided sample
     * 
     * @param sampleEvents The sample events to use as a basis for generation
     * @param targetSize The target number of events to generate
     * @return A list of events with balanced event types
     */
    public static List<Event> generateTrainingData(List<Event> sampleEvents, int targetSize) {
        // This would normally implement data augmentation techniques
        // For simplicity in this implementation, we'll just return the sample
        return sampleEvents;
        
        // A real implementation might include:
        // - Creating variations of existing events with similar attributes
        // - Randomly combining features from different events
        // - Ensuring a balanced distribution of event types
    }
}