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
        List<Event> trainingEvents = generateTrainingData(100);
        
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
     * Generates training data for the ML model with balanced class distribution
     * 
     * @param count Number of training examples to generate
     * @return List of events with balanced class distribution
     */
    public static List<Event> generateTrainingData(int count) {
        LOGGER.info("Generating " + count + " training examples");
        List<Event> events = new ArrayList<>();
        
        // Start with sample data from assignment brief
        addSampleEventsFromBrief(events);
        
        // Define the event types to generate
        String[] types = {"Conference", "Workshop", "Party", "Wedding", "Exhibition", 
                          "Concert", "Sports", "Social", "Seminar"};
        
        // Calculate how many of each type we need for balanced distribution
        int perType = Math.max(5, (count - events.size()) / types.length);
        
        // Generate events for each type
        for (String type : types) {
            int typeCount = countEventsByType(events, type);
            int needed = perType - typeCount;
            
            for (int i = 0; i < needed && events.size() < count; i++) {
                events.add(generateEventOfType(type, events.size() + 1));
            }
        }
        
        // If we still need more events, add random ones
        Random random = new Random();
        while (events.size() < count) {
            String type = types[random.nextInt(types.length)];
            events.add(generateEventOfType(type, events.size() + 1));
        }
        
        LOGGER.info("Generated " + events.size() + " training examples");
        return events;
    }
    
    /**
     * Count events of a specific type in the list
     */
    private static int countEventsByType(List<Event> events, String type) {
        int count = 0;
        for (Event event : events) {
            if (type.equals(event.getType())) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Generate an event of the specified type
     */
    private static Event generateEventOfType(String type, int id) {
        Event event = new Event();
        event.setId(id);
        event.setType(type);
        
        Random random = new Random();
        
        // Set location from a list of common locations
        String[] locations = {"London", "Manchester", "Birmingham", "Edinburgh", "Coventry", 
                             "Leeds", "Cardiff", "Glasgow", "Liverpool", "Bristol", "Oxford",
                             "Cambridge", "Nottingham", "Sheffield", "Newcastle"};
        event.setLocation(locations[random.nextInt(locations.length)]);
        
        // Set date within next 2 years
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, random.nextInt(730)); // Random date within next 2 years
        event.setEventDate(cal.getTime());
        
        // Set capacity
        event.setCapacity(10 + random.nextInt(490)); // Capacity between 10 and 500
        
        // Set name and description based on type
        switch (type) {
            case "Conference":
                event.setName(generateConferenceName());
                event.setDescription(generateConferenceDescription());
                break;
            case "Workshop":
                event.setName(generateWorkshopName());
                event.setDescription(generateWorkshopDescription());
                break;
            case "Party":
                event.setName(generatePartyName());
                event.setDescription(generatePartyDescription());
                break;
            case "Wedding":
                event.setName(generateWeddingName());
                event.setDescription(generateWeddingDescription());
                break;
            case "Exhibition":
                event.setName(generateExhibitionName());
                event.setDescription(generateExhibitionDescription());
                break;
            case "Concert":
                event.setName(generateConcertName());
                event.setDescription(generateConcertDescription());
                break;
            case "Sports":
                event.setName(generateSportsName());
                event.setDescription(generateSportsDescription());
                break;
            case "Social":
                event.setName(generateSocialName());
                event.setDescription(generateSocialDescription());
                break;
            case "Seminar":
                event.setName(generateSeminarName());
                event.setDescription(generateSeminarDescription());
                break;
            default:
                event.setName("Generic Event " + id);
                event.setDescription("Generic event description");
        }
        
        return event;
    }
    
    // Name generators for each event type
    private static String generateConferenceName() {
        String[] prefixes = {"Annual", "International", "Global", "Tech", "Industry", "Innovation"};
        String[] topics = {"AI", "Technology", "Digital", "Business", "Healthcare", "Science", "Finance"};
        String[] suffixes = {"Conference", "Summit", "Congress", "Forum", "Symposium", "Convention"};
        
        Random r = new Random();
        return prefixes[r.nextInt(prefixes.length)] + " " + 
               topics[r.nextInt(topics.length)] + " " + 
               suffixes[r.nextInt(suffixes.length)] + " " + (2025 + r.nextInt(3));
    }
    
    private static String generateWorkshopName() {
        String[] prefixes = {"Practical", "Hands-on", "Interactive", "Intensive", "Professional"};
        String[] topics = {"Programming", "Design", "Marketing", "Leadership", "Data Analysis", 
                           "Project Management", "Communication"};
        String[] suffixes = {"Workshop", "Masterclass", "Training", "Bootcamp", "Session"};
        
        Random r = new Random();
        return prefixes[r.nextInt(prefixes.length)] + " " + 
               topics[r.nextInt(topics.length)] + " " + 
               suffixes[r.nextInt(suffixes.length)];
    }
    
    private static String generatePartyName() {
        String[] themes = {"Summer", "Winter", "Holiday", "New Year's", "Halloween", "Christmas", 
                           "Graduation", "Anniversary", "Birthday"};
        String[] types = {"Party", "Celebration", "Bash", "Gala", "Mixer", "Fiesta"};
        
        Random r = new Random();
        return themes[r.nextInt(themes.length)] + " " + types[r.nextInt(types.length)];
    }
    
    private static String generateWeddingName() {
        String[] firstNames1 = {"James", "Sarah", "Michael", "Emma", "David", "Jennifer", "John", "Lisa"};
        String[] firstNames2 = {"Robert", "Emily", "Thomas", "Rebecca", "Daniel", "Sophie", "William", "Anna"};
        
        Random r = new Random();
        String name1 = firstNames1[r.nextInt(firstNames1.length)];
        String name2 = firstNames2[r.nextInt(firstNames2.length)];
        
        return name1 + " & " + name2 + "'s Wedding";
    }
    
    private static String generateExhibitionName() {
        String[] themes = {"Modern", "Contemporary", "Classical", "Urban", "Digital", "Traditional", "Interactive"};
        String[] types = {"Art", "Photography", "Design", "Sculpture", "Fashion", "Technology", "History"};
        String[] suffixes = {"Exhibition", "Showcase", "Display", "Gallery", "Show"};
        
        Random r = new Random();
        return themes[r.nextInt(themes.length)] + " " + 
               types[r.nextInt(types.length)] + " " + 
               suffixes[r.nextInt(suffixes.length)];
    }
    
    private static String generateConcertName() {
        String[] prefixes = {"Live", "Annual", "Summer", "Winter", "Evening of"};
        String[] types = {"Jazz", "Rock", "Classical", "Pop", "Electronic", "Folk", "Country"};
        String[] suffixes = {"Concert", "Music Festival", "Performance", "Tour", "Recital", "Show"};
        
        Random r = new Random();
        return prefixes[r.nextInt(prefixes.length)] + " " + 
               types[r.nextInt(types.length)] + " " + 
               suffixes[r.nextInt(suffixes.length)];
    }
    
    private static String generateSportsName() {
        String[] prefixes = {"Annual", "Championship", "Charity", "Amateur", "Professional"};
        String[] sports = {"Football", "Basketball", "Tennis", "Golf", "Running", "Swimming", "Cycling"};
        String[] suffixes = {"Tournament", "Match", "Game", "Competition", "Cup", "Marathon", "Race"};
        
        Random r = new Random();
        return prefixes[r.nextInt(prefixes.length)] + " " + 
               sports[r.nextInt(sports.length)] + " " + 
               suffixes[r.nextInt(suffixes.length)];
    }
    
    private static String generateSocialName() {
        String[] prefixes = {"Community", "Charity", "Networking", "Industry", "Professional"};
        String[] types = {"Meetup", "Gathering", "Social", "Mixer", "Fundraiser", "Gala"};
        
        Random r = new Random();
        return prefixes[r.nextInt(prefixes.length)] + " " + types[r.nextInt(types.length)];
    }
    
    private static String generateSeminarName() {
        String[] prefixes = {"Professional", "Expert", "Introduction to", "Advanced", "Practical"};
        String[] topics = {"Finance", "Marketing", "Leadership", "Management", "Technology", 
                           "Innovation", "Communication", "Strategy"};
        String[] suffixes = {"Seminar", "Talk", "Lecture", "Presentation", "Discussion", "Session"};
        
        Random r = new Random();
        return prefixes[r.nextInt(prefixes.length)] + " " + 
               topics[r.nextInt(topics.length)] + " " + 
               suffixes[r.nextInt(suffixes.length)];
    }
    
    // Description generators for each event type
    private static String generateConferenceDescription() {
        String[] parts = {
            "Join industry leaders for discussions on the latest trends and innovations.",
            "A premier gathering of experts sharing insights on cutting-edge developments.",
            "Network with professionals and discover new opportunities in the field.",
            "Featuring keynote speakers, panel discussions, and interactive sessions.",
            "Learn from the best minds in the industry about future directions and challenges."
        };
        
        return parts[new Random().nextInt(parts.length)];
    }
    
    private static String generateWorkshopDescription() {
        String[] parts = {
            "Hands-on training session with practical exercises and real-world applications.",
            "Learn essential skills through interactive activities guided by expert instructors.",
            "Small group workshop focusing on skill development and immediate application.",
            "Intensive training designed to improve your capabilities in a supportive environment.",
            "Practical session where you'll learn by doing with expert guidance."
        };
        
        return parts[new Random().nextInt(parts.length)];
    }
    
    private static String generatePartyDescription() {
        String[] parts = {
            "Celebrate with music, food, and entertainment in a festive atmosphere.",
            "Join us for a night of fun, dancing, and celebration with friends.",
            "Food, drinks, and entertainment provided for an unforgettable celebration.",
            "A festive gathering to celebrate and enjoy good company.",
            "Come and enjoy a night of celebration with great music and atmosphere."
        };
        
        return parts[new Random().nextInt(parts.length)];
    }
    
    private static String generateWeddingDescription() {
        String[] parts = {
            "Join us as we celebrate our special day with family and friends.",
            "A beautiful ceremony followed by reception to celebrate the union.",
            "We invite you to share in our joy as we begin our journey together.",
            "An intimate celebration of love with close family and friends.",
            "Join us for a day of love, joy, and celebration."
        };
        
        return parts[new Random().nextInt(parts.length)];
    }
    
    private static String generateExhibitionDescription() {
        String[] parts = {
            "Displaying works from talented artists exploring various themes and techniques.",
            "Showcasing innovative creations and artistic expressions from around the world.",
            "An exhibition featuring unique perspectives and creative approaches.",
            "Come experience thought-provoking works in a carefully curated display.",
            "A showcase of creativity, innovation, and artistic vision."
        };
        
        return parts[new Random().nextInt(parts.length)];
    }
    
    private static String generateConcertDescription() {
        String[] parts = {
            "Live music performance featuring talented musicians and an engaging atmosphere.",
            "Experience the power of live music in an unforgettable performance.",
            "Join us for an evening of exceptional music and entertainment.",
            "A captivating musical experience with remarkable performers.",
            "Enjoy an evening of music that will move and inspire you."
        };
        
        return parts[new Random().nextInt(parts.length)];
    }
    
    private static String generateSportsDescription() {
        String[] parts = {
            "Competitive event showcasing athletic skill and sportsmanship.",
            "Join us for an exciting display of athletic prowess and team spirit.",
            "Watch top athletes compete in this thrilling sporting event.",
            "A celebration of athleticism, competition, and sporting excellence.",
            "Witness the excitement and drama of competitive sports at its best."
        };
        
        return parts[new Random().nextInt(parts.length)];
    }
    
    private static String generateSocialDescription() {
        String[] parts = {
            "An opportunity to connect with like-minded individuals in a relaxed setting.",
            "Meet new people and build connections in a friendly atmosphere.",
            "Expand your network and engage in meaningful conversations.",
            "A social gathering designed to foster connections and community.",
            "Join us for an evening of networking and relationship building."
        };
        
        return parts[new Random().nextInt(parts.length)];
    }
    
    private static String generateSeminarDescription() {
        String[] parts = {
            "Informative session delivering valuable insights and knowledge on the topic.",
            "Expert-led discussion on key concepts and best practices.",
            "Learn from industry experts about important principles and applications.",
            "Focused educational session providing depth and clarity on the subject.",
            "Gain valuable knowledge from experienced professionals in the field."
        };
        
        return parts[new Random().nextInt(parts.length)];
    }
    
    /**
     * Adds the sample events from the assignment brief to the training data
     */
    private static void addSampleEventsFromBrief(List<Event> events) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            // Conference events
            addSampleEvent(events, "AI Summit", "Conference", "London", 
                    dateFormat.parse("2025-02-20"), "AI conference with workshops");
            
            addSampleEvent(events, "Tech Innovations", "Conference", "Edinburgh", 
                    dateFormat.parse("2025-06-10"), "Showcase of tech trends");
            
            addSampleEvent(events, "Healthcare Tech", "Conference", "Glasgow", 
                    dateFormat.parse("2025-11-02"), "Innovations in healthcare tech");
            
            // Wedding events
            addSampleEvent(events, "Sarah & Tom's Wedding", "Wedding", "Manchester", 
                    dateFormat.parse("2025-03-15"), "Private family wedding");
            
            addSampleEvent(events, "Emily & Jack's Wedding", "Wedding", "Leeds", 
                    dateFormat.parse("2025-04-18"), "Garden wedding celebration");
            
            // Workshop events
            addSampleEvent(events, "Java Programming Workshop", "Workshop", "Coventry", 
                    dateFormat.parse("2025-01-28"), "Learn Java Basics");
            
            addSampleEvent(events, "Data Science Bootcamp", "Workshop", "London", 
                    dateFormat.parse("2025-03-05"), "Intro to data science techniques");
            
            addSampleEvent(events, "Cybersecurity Basics", "Workshop", "Liverpool", 
                    dateFormat.parse("2025-08-15"), "Intro to online safety");
            
            // Party events
            addSampleEvent(events, "New Year Bash", "Party", "Birmingham", 
                    dateFormat.parse("2025-12-31"), "Celebrate the New Year");
            
            addSampleEvent(events, "Graduation Party", "Party", "Cardiff", 
                    dateFormat.parse("2025-07-01"), "Celebrate with graduates");
            
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error adding sample events from brief", e);
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
        event.setCapacity(100);  // Default capacity
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
            List<Event> trainingData = generateTrainingData(100);
            
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