package com.emma.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility class for database operations
 */
public class DatabaseUtil {
    private static String JDBC_URL;
    private static String JDBC_USER;
    private static String JDBC_PASSWORD;
    private static boolean driverLoaded = false;
    private static final Properties properties = new Properties();
    
    static {
        try {
            // Load database properties from file
            InputStream inputStream = DatabaseUtil.class.getClassLoader().getResourceAsStream("database.properties");
            
            if (inputStream != null) {
                properties.load(inputStream);
                JDBC_URL = properties.getProperty("jdbc.url", "jdbc:mysql://localhost:3306/emma_events");
                JDBC_USER = properties.getProperty("jdbc.user", "root");
                JDBC_PASSWORD = properties.getProperty("jdbc.password", "1234");
                inputStream.close();
            } else {
                // Fallback to default values if properties file not found
                JDBC_URL = "jdbc:mysql://localhost:3306/emma_events";
                JDBC_USER = "root";
                JDBC_PASSWORD = "1234";
                System.err.println("WARNING: database.properties file not found. Using default values.");
            }
            
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            driverLoaded = true;
            System.out.println("JDBC driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("WARNING: Failed to load JDBC driver: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("WARNING: Failed to load database properties: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Gets a connection to the database
     * 
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        if (!driverLoaded) {
            throw new SQLException("JDBC driver not loaded");
        }
        try {
            return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (SQLException e) {
            System.err.println("WARNING: Failed to get database connection: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Checks if the database is available
     * 
     * @return true if the database is available
     */
    public static boolean isDatabaseAvailable() {
        if (!driverLoaded) {
            return false;
        }
        
        try (Connection conn = getConnection()) {
            return true;
        } catch (SQLException e) {
            System.err.println("Database is not available: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Closes database resources
     * 
     * @param connection The connection to close
     * @param statement The statement to close
     * @param resultSet The result set to close
     */
    public static void closeResources(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Rolls back a transaction
     * 
     * @param connection The connection on which to roll back
     */
    public static void rollback(Connection connection) {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}