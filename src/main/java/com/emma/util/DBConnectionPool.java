package com.emma.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Database connection pool manager using Apache DBCP2
 * Provides efficient connection management for the application
 */
public class DBConnectionPool {
    private static BasicDataSource dataSource;
    private static final Properties properties = new Properties();
    
    static {
        try {
            // Load database properties
            InputStream inputStream = DBConnectionPool.class.getClassLoader().getResourceAsStream("database.properties");
            
            if (inputStream != null) {
                properties.load(inputStream);
                String url = properties.getProperty("jdbc.url", "jdbc:mysql://localhost:3306/emma_events");
                String username = properties.getProperty("jdbc.user", "root");
                String password = properties.getProperty("jdbc.password", "1234");
                String driverClassName = properties.getProperty("jdbc.driver", "com.mysql.cj.jdbc.Driver");
                int minConnections = Integer.parseInt(properties.getProperty("jdbc.min.connections", "5"));
                int maxConnections = Integer.parseInt(properties.getProperty("jdbc.max.connections", "20"));
                
                // Set up connection pool
                dataSource = new BasicDataSource();
                dataSource.setDriverClassName(driverClassName);
                dataSource.setUrl(url);
                dataSource.setUsername(username);
                dataSource.setPassword(password);
                
                // Configure pool settings
                dataSource.setInitialSize(minConnections);
                dataSource.setMinIdle(minConnections);
                dataSource.setMaxIdle(maxConnections);
                dataSource.setMaxTotal(maxConnections);
                dataSource.setMaxWaitMillis(10000); // 10 seconds max wait time
                
                // Configure connection test settings
                dataSource.setTestOnBorrow(true);
                dataSource.setValidationQuery("SELECT 1");
                
                // Configure connection pool maintenance
                dataSource.setRemoveAbandonedOnBorrow(true);
                dataSource.setRemoveAbandonedTimeout(60); // 60 seconds
                
                inputStream.close();
                
                System.out.println("Database connection pool initialized successfully");
            } else {
                System.err.println("ERROR: database.properties file not found.");
                throw new IOException("database.properties file not found");
            }
        } catch (Exception e) {
            System.err.println("ERROR: Failed to initialize database connection pool: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Get a connection from the pool
     * 
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Connection pool not initialized");
        }
        return dataSource.getConnection();
    }
    
    /**
     * Close the connection pool (for application shutdown)
     */
    public static void closePool() {
        if (dataSource != null) {
            try {
                dataSource.close();
                System.out.println("Database connection pool closed");
            } catch (SQLException e) {
                System.err.println("ERROR: Failed to close connection pool: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Check if the database is available
     * 
     * @return true if the database is available
     */
    public static boolean isDatabaseAvailable() {
        if (dataSource == null) {
            return false;
        }
        
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database is not available: " + e.getMessage());
            return false;
        }
    }
}