package com.emma.ml;

import com.emma.model.Event;
import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for training and evaluating event classification models
 */
public class ModelTrainer {
    
    private static final Logger LOGGER = Logger.getLogger(ModelTrainer.class.getName());
    private static final String MODEL_DIRECTORY = "ml-models";
    private static final String MODEL_FILE_NAME = "event_classification_model.model";
    
    /**
     * Gets the path to the model file, ensuring the directory exists
     * 
     * @return Full path to the model file
     */
    private static String getModelFilePath() {
        // Get the user's home directory
        String userHome = System.getProperty("user.home");
        File modelDir = new File(userHome, MODEL_DIRECTORY);
        
        // Create the directory if it doesn't exist
        if (!modelDir.exists()) {
            modelDir.mkdirs();
        }
        
        return new File(modelDir, MODEL_FILE_NAME).getAbsolutePath();
    }
    
    /**
     * Creates a default model for use when no trained model is available
     * 
     * @return A basic trained classifier
     * @throws Exception if model creation fails
     */
    public static Classifier createDefaultModel() throws Exception {
        // Generate synthetic training data
        List<Event> trainingEvents = generateSyntheticTrainingData(100);
        
        // Train a model on this data
        return trainModel(trainingEvents);
    }
    
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
        
        if (trainingData.numInstances() == 0) {
            throw new IllegalArgumentException("No valid training instances available");
        }
        
        LOGGER.info("Training model with " + trainingData.numInstances() + " instances");
        
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
        LOGGER.info("Model training completed successfully");
        
        return classifier;
    }
    
    /**
     * Saves the trained model to a file
     * 
     * @param model The trained classifier to save
     * @throws Exception if saving fails
     */
    public static void saveModel(Classifier model) throws Exception {
        String modelPath = getModelFilePath();
        LOGGER.info("Saving model to: " + modelPath);
        SerializationHelper.write(modelPath, model);
        LOGGER.info("Model saved successfully");
    }
    
    /**
     * Loads a previously trained model from a file
     * 
     * @return The loaded classifier
     * @throws Exception if loading fails
     */
    public static Classifier loadModel() throws Exception {
        String modelPath = getModelFilePath();
        File modelFile = new File(modelPath);
        
        if (!modelFile.exists()) {
            LOGGER.warning("Model file not found at: " + modelPath);
            throw new IOException("Model file not found at: " + modelPath);
        }
        
        LOGGER.info("Loading model from: " + modelPath);
        return (Classifier) SerializationHelper.read(modelPath);
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
        
        if (testData.numInstances() == 0) {
            LOGGER.warning("No valid test instances available for evaluation");
            return 0.0;
        }
        
        int correct = 0;
        for (int i = 0; i < testData.numInstances(); i++) {
            double predicted = model.classifyInstance(testData.instance(i));
            double actual = testData.instance(i).classValue();
            
            if (predicted == actual) {
                correct++;
            }
        }
        
        double accuracy = (double) correct / testData.numInstances();
        LOGGER.info("Model evaluation completed. Accuracy: " + (accuracy * 100) + "%");
        return accuracy;
    }
    
    /**
     * Generates synthetic training data based on the assignment brief examples
     * 
     * @param targetSize The target number of events to generate
     * @return A list of events with balanced event types
     */
    public static List<Event> generateSyntheticTrainingData(int targetSize) {
        LOGGER.info("Generating synthetic training data, target size: " + targetSize);
        List<Event> syntheticEvents = new ArrayList<>();
        
        // Sample data from the assignment brief
        addSampleEvents(syntheticEvents);
        
        // Generate additional events to reach the target size
        Random random = new Random(42); // Fixed seed for reproducibility
        String[] eventTypes = {"Conference", "Wedding", "Workshop", "Party", "Exhibition", "Concert", "Sports", "Social", "Seminar"};
        String[] locations = {"London", "Manchester", "Birmingham", "Edinburgh", "Glasgow", "Liverpool", "Leeds", "Bristol", "Cardiff", "Newcastle", "Sheffield", "Coventry"};
        
        // Event description templates for each type
        Map<String, List<String>> descriptionTemplates = new HashMap<>();
        descriptionTemplates.put("Conference", Arrays.asList(
            "Annual {topic} conference for industry professionals",
            "International summit on {topic} with expert speakers",
            "Professional gathering focused on {topic} trends and innovations",
            "{topic} conference featuring keynote speakers and workshops"
        ));
        
        descriptionTemplates.put("Wedding", Arrays.asList(
            "Beautiful {setting} wedding celebration",
            "Intimate family wedding ceremony and reception",
            "Traditional wedding with reception to follow",
            "Garden wedding celebration with close friends and family"
        ));
        
        descriptionTemplates.put("Workshop", Arrays.asList(
            "Hands-on learning workshop for {topic} skills",
            "Practical {topic} workshop for beginners",
            "Interactive {topic} training session with industry experts",
            "Skill-building workshop focusing on {topic} techniques"
        ));
        
        descriptionTemplates.put("Party", Arrays.asList(
            "{occasion} celebration with food and entertainment",
            "Festive {occasion} party with music and dancing",
            "Social gathering to celebrate {occasion}",
            "Fun {occasion} party with games and activities"
        ));
        
        // Topics, settings, and occasions for templates
        String[] topics = {"technology", "AI", "finance", "marketing", "healthcare", "education", "sustainability", "cybersecurity", "blockchain", "data science"};
        String[] settings = {"garden", "beach", "countryside", "urban", "historic", "modern", "rustic", "elegant"};
        String[] occasions = {"graduation", "birthday", "anniversary", "holiday", "New Year", "Christmas", "Halloween", "summer", "retirement"};
        
        // Generate events until we reach the target size
        while (syntheticEvents.size() < targetSize) {
            try {
                // Select random event type
                String eventType = eventTypes[random.nextInt(eventTypes.length)];
                
                // Create a new event
                Event event = new Event();
                event.setId(syntheticEvents.size() + 100); // Arbitrary ID
                
                // Set event name
                event.setName("Sample " + eventType + " " + (syntheticEvents.size() + 1));
                
                // Set location
                event.setLocation(locations[random.nextInt(locations.length)]);
                
                // Set event date (within next 2 years)
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, random.nextInt(730)); // Random date within next 2 years
                event.setEventDate(cal.getTime());
                
                // Set description based on templates
                List<String> templates = descriptionTemplates.get(eventType);
                if (templates != null && !templates.isEmpty()) {
                    String template = templates.get(random.nextInt(templates.size()));
                    
                    // Replace placeholders
                    if (template.contains("{topic}")) {
                        template = template.replace("{topic}", topics[random.nextInt(topics.length)]);
                    }
                    if (template.contains("{setting}")) {
                        template = template.replace("{setting}", settings[random.nextInt(settings.length)]);
                    }
                    if (template.contains("{occasion}")) {
                        template = template.replace("{occasion}", occasions[random.nextInt(occasions.length)]);
                    }
                    
                    event.setDescription(template);
                } else {
                    event.setDescription("Sample " + eventType + " description");
                }
                
                // Set event type
                event.setType(eventType);
                
                // Add to the list
                syntheticEvents.add(event);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error generating synthetic event", e);
            }
        }
        
        LOGGER.info("Generated " + syntheticEvents.size() + " synthetic events for training");
        return syntheticEvents;
    }
    
    /**
     * Adds the sample events from the assignment brief to the list
     * 
     * @param events The list to add events to
     */
    private static void addSampleEvents(List<Event> events) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            // Sample events from the assignment brief
            addSampleEvent(events, "AI Summit", "Conference", "London", 
                    dateFormat.parse("2025-02-20"), "AI conference with workshops");
            
            addSampleEvent(events, "Sarah & Tom's Wedding", "Wedding", "Manchester", 
                    dateFormat.parse("2025-03-15"), "Private family wedding");
            
            addSampleEvent(events, "Java Programming Workshop", "Workshop", "Coventry", 
                    dateFormat.parse("2025-01-28"), "Learn Java Basics");
            
            addSampleEvent(events, "New Year Bash", "Party", "Birmingham", 
                    dateFormat.parse("2025-12-31"), "Celebrate the New Year");
            
            addSampleEvent(events, "Tech Innovations", "Conference", "Edinburgh", 
                    dateFormat.parse("2025-06-10"), "Showcase of tech trends");
            
            addSampleEvent(events, "Data Science Bootcamp", "Workshop", "London", 
                    dateFormat.parse("2025-03-05"), "Intro to data science techniques");
            
            addSampleEvent(events, "Emily & Jack's Wedding", "Wedding", "Leeds", 
                    dateFormat.parse("2025-04-18"), "Garden wedding celebration");
            
            addSampleEvent(events, "Graduation Party", "Party", "Cardiff", 
                    dateFormat.parse("2025-07-01"), "Celebrate with graduates");
            
            addSampleEvent(events, "Healthcare Tech", "Conference", "Glasgow", 
                    dateFormat.parse("2025-11-02"), "Innovations in healthcare tech");
            
            addSampleEvent(events, "Cybersecurity Basics", "Workshop", "Liverpool", 
                    dateFormat.parse("2025-08-15"), "Intro to online safety");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error adding sample events", e);
        }
    }
    
    /**
     * Helper method to add a sample event
     */
    private static void addSampleEvent(List<Event> events, String name, String type, 
            String location, Date date, String description) {
        Event event = new Event();
        event.setId(events.size() + 1);
        event.setName(name);
        event.setType(type);
        event.setLocation(location);
        event.setEventDate(date);
        event.setDescription(description);
        events.add(event);
    }
    
    /**
     * Main method to train and save a model
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            // Generate training data
            List<Event> trainingData = generateSyntheticTrainingData(100);
            
            // Train model
            Classifier model = trainModel(trainingData);
            
            // Save model
            saveModel(model);
            
            // Evaluate model
            double accuracy = evaluateModel(model, trainingData);
            System.out.println("Model trained and saved successfully. Accuracy: " + (accuracy * 100) + "%");
            
        } catch (Exception e) {
            System.err.println("Error training model: " + e.getMessage());
            e.printStackTrace();
        }
    }
}