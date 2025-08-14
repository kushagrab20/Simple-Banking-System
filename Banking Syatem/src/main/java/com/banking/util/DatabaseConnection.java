package com.banking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection utility class
 */
public class DatabaseConnection {
    // Database configuration
    private static final String URL = "jdbc:mysql://localhost:3306/banking_system";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Kushagra@20";
    
    // JDBC Driver
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Static block to load the driver
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            throw new RuntimeException("Failed to load MySQL JDBC Driver", e);
        }
    }
    
    /**
     * Get a database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Close a database connection
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Test database connection
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get database URL
     * @return database URL
     */
    public static String getUrl() {
        return URL;
    }
    
    /**
     * Get database username
     * @return database username
     */
    public static String getUsername() {
        return USERNAME;
    }
    
    /**
     * Get database password
     * @return database password
     */
    public static String getPassword() {
        return PASSWORD;
    }
} 