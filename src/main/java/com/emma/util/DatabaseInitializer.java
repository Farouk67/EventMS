package com.emma.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@WebListener
public class DatabaseInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("==== DatabaseInitializer: Starting ====");
        
        try {
            // Test database connection
            testConnection();
            
            // Create tables if they don't exist
            createTablesIfNeeded();
            
            System.out.println("==== DatabaseInitializer: Completed Successfully ====");
        } catch (Exception e) {
            System.err.println("==== DatabaseInitializer ERROR: " + e.getMessage() + " ====");
            e.printStackTrace();
        }
    }

    private void testConnection() {
        try (Connection conn = DBConnectionPool.getConnection()) {
            if (conn != null) {
                System.out.println("Database connection successful!");
            } else {
                System.err.println("Failed to get database connection!");
            }
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void createTablesIfNeeded() {
        Connection conn = null;
        Statement stmt = null;
        
        try {
            conn = DBConnectionPool.getConnection();
            stmt = conn.createStatement();
            
            // Create user table
            stmt.execute("CREATE TABLE IF NOT EXISTS user (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) NOT NULL UNIQUE, " +
                "email VARCHAR(100) NOT NULL UNIQUE, " +
                "password VARCHAR(255) NOT NULL, " +
                "first_name VARCHAR(50), " +
                "last_name VARCHAR(50), " +
                "registered_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "is_active BOOLEAN DEFAULT TRUE, " +
                "role VARCHAR(20) DEFAULT 'user'" +
                ")");
            
            // Create event_type table
            stmt.execute("CREATE TABLE IF NOT EXISTS event_type (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50) NOT NULL UNIQUE, " +
                "description TEXT, " +
                "icon VARCHAR(50)" +
                ")");
            
            // Create event table
            stmt.execute("CREATE TABLE IF NOT EXISTS event (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "description TEXT, " +
                "event_date TIMESTAMP NOT NULL, " +
                "location VARCHAR(255) NOT NULL, " +
                "created_by INT, " +
                "event_type_id INT, " +
                "attendee_count INT DEFAULT 0, " +
                "capacity INT DEFAULT 0, " +
                "FOREIGN KEY (created_by) REFERENCES user(id) ON DELETE SET NULL, " +
                "FOREIGN KEY (event_type_id) REFERENCES event_type(id) ON DELETE SET NULL" +
                ")");
            
            // Create rsvp table
            stmt.execute("CREATE TABLE IF NOT EXISTS rsvp (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "event_id INT NOT NULL, " +
                "status VARCHAR(20) DEFAULT 'yes', " +
                "responded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE, " +
                "UNIQUE KEY user_event (user_id, event_id)" +
                ")");
            
            // Insert default admin user if not exists
            stmt.execute("INSERT IGNORE INTO user (username, email, password, role) " +
                "VALUES ('admin', 'admin@example.com', 'admin123', 'admin')");
                
            // Insert default event types if needed
            String[] eventTypes = {"Conference", "Workshop", "Party", "Exhibition", "Concert", "Sports", "Social", "Seminar", "Other"};
            for (String type : eventTypes) {
                stmt.execute("INSERT IGNORE INTO event_type (name) VALUES ('" + type + "')");
            }
            
            System.out.println("Database tables created or verified!");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("==== DatabaseInitializer: Shutting down ====");
    }
}