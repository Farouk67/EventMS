package com.emma.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Database initializer that runs on application startup
 * Sets up the database schema and loads initial data if needed
 */
@WebListener
public class DatabaseInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("==== DatabaseInitializer: contextInitialized method called ====");
        
        try {
            System.out.println("Checking database initialization...");
            
            // First check if we can connect to the database
            try (Connection conn = DBConnectionPool.getConnection()) {
                if (conn != null) {
                    System.out.println("Successfully connected to database: " + conn.getMetaData().getURL());
                } else {
                    System.err.println("ERROR: Connection object is null");
                    return;
                }
            } catch (SQLException e) {
                System.err.println("ERROR: Cannot connect to database: " + e.getMessage());
                e.printStackTrace();
                return;
            }
            
            if (!isSchemaInitialized()) {
                System.out.println("Database schema not found. Initializing database...");
                initializeDatabase();
            } else {
                System.out.println("Database schema already exists. Skipping initialization.");
            }
        } catch (Exception e) {
            System.err.println("ERROR: Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("==== DatabaseInitializer: contextDestroyed method called ====");
        
        // Close the database connection pool
        try {
            DBConnectionPool.closePool();
            System.out.println("Database connection pool closed successfully");
        } catch (Exception e) {
            System.err.println("ERROR: Failed to close database connection pool: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Deregister MySQL driver to prevent memory leaks
        System.out.println("Cleaning up JDBC resources...");
        
        try {
            // Use reflection to access the shutdown method as a workaround
            try {
                Class.forName("com.mysql.cj.jdbc.AbandonedConnectionCleanupThread")
                    .getMethod("uncheckedShutdown").invoke(null);
                System.out.println("MySQL cleanup thread shut down successfully.");
            } catch (Exception e) {
                System.err.println("Could not shut down MySQL cleanup thread via reflection: " + e.getMessage());
                // Alternative approach: just deregister the driver
            }
            
            // Manually deregister JDBC drivers
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                try {
                    DriverManager.deregisterDriver(driver);
                    System.out.println("Deregistered JDBC driver: " + driver);
                } catch (SQLException e) {
                    System.err.println("Error deregistering JDBC driver: " + driver);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("Error handling JDBC drivers: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Check if the database schema is already initialized
     * 
     * @return true if schema exists, false otherwise
     */
    private boolean isSchemaInitialized() {
        try (Connection conn = DBConnectionPool.getConnection()) {
            if (conn == null) {
                System.err.println("ERROR: Cannot check schema - connection is null");
                return false;
            }
            
            try (Statement stmt = conn.createStatement()) {
                System.out.println("Checking if tables exist in database...");
                
                // Check if tables exist by querying information_schema
                // Note: Updated to use the consistent table names as per schema.sql
                String sql = "SELECT COUNT(table_name) AS table_count " +
                             "FROM information_schema.tables " +
                             "WHERE table_schema = DATABASE() " +
                             "AND table_name IN ('user', 'event', 'rsvp', 'event_type')";
                
                try (ResultSet rs = stmt.executeQuery(sql)) {
                    if (rs.next()) {
                        int tableCount = rs.getInt("table_count");
                        System.out.println("Found " + tableCount + " tables out of 4 expected tables");
                        return tableCount >= 4; // We expect at least these 4 tables
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking database schema: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Initialize the database schema and load initial data
     */
    private void initializeDatabase() {
        try {
            System.out.println("Starting database initialization process...");
            
            // Execute schema.sql
            executeSqlFile("database/schema.sql");
            
            // Execute initial-data.sql
            executeSqlFile("database/initial-data.sql");
            
            System.out.println("Database initialized successfully!");
        } catch (Exception e) {
            System.err.println("ERROR: Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Execute SQL statements from a file
     * 
     * @param resourcePath Path to the SQL file in resources
     * @throws Exception if execution fails
     */
    private void executeSqlFile(String resourcePath) throws Exception {
        System.out.println("Attempting to execute SQL file: " + resourcePath);
        
        InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (is == null) {
            System.err.println("Resource not found: " + resourcePath);
            throw new Exception("Resource not found: " + resourcePath);
        }
        
        System.out.println("SQL file found, beginning execution: " + resourcePath);
        
        try (Connection conn = DBConnectionPool.getConnection()) {
            if (conn == null) {
                throw new SQLException("Cannot execute SQL file - connection is null");
            }
            
            conn.setAutoCommit(false);
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                StringBuilder sb = new StringBuilder();
                String line;
                int statementCount = 0;
                
                while ((line = reader.readLine()) != null) {
                    // Ignore comments and empty lines
                    if (line.startsWith("--") || line.trim().isEmpty()) {
                        continue;
                    }
                    
                    sb.append(line);
                    
                    // Execute when semicolon is found (end of statement)
                    if (line.trim().endsWith(";")) {
                        String sql = sb.toString();
                        try (Statement stmt = conn.createStatement()) {
                            stmt.execute(sql);
                            statementCount++;
                            
                            // Print periodic status for long scripts
                            if (statementCount % 10 == 0) {
                                System.out.println("Executed " + statementCount + " SQL statements so far...");
                            }
                        } catch (SQLException e) {
                            System.err.println("Error executing SQL statement: " + e.getMessage());
                            System.err.println("Problem statement: " + sql);
                            throw e;
                        }
                        sb = new StringBuilder();
                    }
                }
                
                conn.commit();
                System.out.println("Successfully executed " + statementCount + " SQL statements from file: " + resourcePath);
            } catch (Exception e) {
                System.err.println("Error processing SQL file, rolling back transaction: " + e.getMessage());
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}