package com.emma.ml;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * This listener initializes the ML model on application startup
 */
@WebListener
public class ModelInitializer implements ServletContextListener {
    
    private static final Logger LOGGER = Logger.getLogger(ModelInitializer.class.getName());
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Initializing ML model on application startup");
        
        try {
            // Try to load an existing model
            try {
                ModelTrainer.loadModel();
                LOGGER.info("Existing model loaded successfully");
            } catch (Exception e) {
                LOGGER.log(Level.INFO, "No existing model found. Training a new one...", e);
                
                // Generate training data and train a new model
                LOGGER.info("Generating training data...");
                LOGGER.info("Training model...");
                weka.classifiers.Classifier model = ModelTrainer.createDefaultModel();
                
                // Save the model
                ModelTrainer.saveModel(model);
                LOGGER.info("New model trained and saved successfully");
            }
            
            // Initialize a predictor to verify everything works
            EventPredictor predictor = new EventPredictor();
            LOGGER.info("Event predictor initialized successfully");
            
            // Store the predictor in the servlet context for application-wide use
            sce.getServletContext().setAttribute("eventPredictor", predictor);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing ML model", e);
        }
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Nothing to clean up for the ML model
        LOGGER.info("ML model resources released");
    }
}