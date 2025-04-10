package com.emma.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.emma.ml.ModelTrainer;
import com.emma.model.Event;


public class AdminController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        try {
            switch (action) {
                case "/ml-dashboard":
                    showMlDashboard(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/events/list");
                    break;
            }
        } catch (Exception e) {
            handleError(request, response, e.getMessage());
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        try {
            switch (action) {
                case "/train-model":
                    trainModel(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/events/list");
                    break;
            }
        } catch (Exception e) {
            handleError(request, response, e.getMessage());
        }
    }
    
    private void showMlDashboard(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get model info if available
        try {
            Map<String, Object> modelInfo = new HashMap<>();
            // Get model details from a file or database
            // For now, use dummy data
            modelInfo.put("accuracy", "87.5");
            modelInfo.put("trainingSize", "100");
            modelInfo.put("lastTrained", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
            
            request.setAttribute("modelInfo", modelInfo);
        } catch (Exception e) {
            // No model info available
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/admin/ml-dashboard.jsp");
        dispatcher.forward(request, response);
    }
    
    private void trainModel(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int trainingSize = 100; // Default
        
        try {
            String sizeParam = request.getParameter("trainingSize");
            if (sizeParam != null && !sizeParam.isEmpty()) {
                trainingSize = Integer.parseInt(sizeParam);
            }
            
            // Generate training data
            List<Event> trainingData = ModelTrainer.generateTrainingData(trainingSize);
            System.out.println("Generated " + trainingData.size() + " training examples");
            
            // Train model
            weka.classifiers.Classifier model = ModelTrainer.trainModel(trainingData);
            
            // Evaluate model
            double accuracy = ModelTrainer.evaluateModel(model, trainingData);
            System.out.println("Model accuracy: " + (accuracy * 100) + "%");
            
            // Save model
            ModelTrainer.saveModel(model);
            
            // Save model info
            // Store model metadata in a properties file or database
            
            request.setAttribute("message", "Model trained successfully with accuracy: " + 
                               String.format("%.2f%%", accuracy * 100));
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error training model: " + e.getMessage());
        }
        
        // Redirect back to dashboard
        response.sendRedirect(request.getContextPath() + "/admin/ml-dashboard");
    }
    
    private void handleError(HttpServletRequest request, HttpServletResponse response, String message) 
            throws ServletException, IOException {
        request.setAttribute("errorMessage", message);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/common/error.jsp");
        dispatcher.forward(request, response);
    }
}