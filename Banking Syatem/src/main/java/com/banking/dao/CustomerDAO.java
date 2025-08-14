package com.banking.dao;

import com.banking.model.Customer;
import com.banking.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Customer operations
 */
public class CustomerDAO {
    
    /**
     * Create a new customer
     * @param customer Customer object to create
     * @return Customer with generated ID
     * @throws SQLException if database operation fails
     */
    public Customer createCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (first_name, last_name, email, phone, address, date_of_birth) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPhone());
            statement.setString(5, customer.getAddress());
            statement.setDate(6, customer.getDateOfBirth());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating customer failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setCustomerId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating customer failed, no ID obtained.");
                }
            }
            
            return customer;
        }
    }
    
    /**
     * Get customer by ID
     * @param customerId Customer ID
     * @return Customer object or null if not found
     * @throws SQLException if database operation fails
     */
    public Customer getCustomerById(int customerId) throws SQLException {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, customerId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToCustomer(resultSet);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get customer by email
     * @param email Customer email
     * @return Customer object or null if not found
     * @throws SQLException if database operation fails
     */
    public Customer getCustomerByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM customers WHERE email = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, email);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToCustomer(resultSet);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get all customers
     * @return List of all customers
     * @throws SQLException if database operation fails
     */
    public List<Customer> getAllCustomers() throws SQLException {
        String sql = "SELECT * FROM customers ORDER BY customer_id";
        List<Customer> customers = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                customers.add(mapResultSetToCustomer(resultSet));
            }
        }
        
        return customers;
    }
    
    /**
     * Update customer information
     * @param customer Customer object with updated information
     * @return true if update successful, false otherwise
     * @throws SQLException if database operation fails
     */
    public boolean updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET first_name = ?, last_name = ?, email = ?, " +
                    "phone = ?, address = ?, date_of_birth = ? WHERE customer_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPhone());
            statement.setString(5, customer.getAddress());
            statement.setDate(6, customer.getDateOfBirth());
            statement.setInt(7, customer.getCustomerId());
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    /**
     * Delete customer by ID
     * @param customerId Customer ID to delete
     * @return true if deletion successful, false otherwise
     * @throws SQLException if database operation fails
     */
    public boolean deleteCustomer(int customerId) throws SQLException {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, customerId);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    /**
     * Search customers by name
     * @param name Name to search for (partial match)
     * @return List of matching customers
     * @throws SQLException if database operation fails
     */
    public List<Customer> searchCustomersByName(String name) throws SQLException {
        String sql = "SELECT * FROM customers WHERE first_name LIKE ? OR last_name LIKE ? ORDER BY customer_id";
        List<Customer> customers = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            String searchPattern = "%" + name + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    customers.add(mapResultSetToCustomer(resultSet));
                }
            }
        }
        
        return customers;
    }
    
    /**
     * Check if email exists
     * @param email Email to check
     * @return true if email exists, false otherwise
     * @throws SQLException if database operation fails
     */
    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM customers WHERE email = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, email);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Map ResultSet to Customer object
     * @param resultSet ResultSet containing customer data
     * @return Customer object
     * @throws SQLException if mapping fails
     */
    private Customer mapResultSetToCustomer(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(resultSet.getInt("customer_id"));
        customer.setFirstName(resultSet.getString("first_name"));
        customer.setLastName(resultSet.getString("last_name"));
        customer.setEmail(resultSet.getString("email"));
        customer.setPhone(resultSet.getString("phone"));
        customer.setAddress(resultSet.getString("address"));
        customer.setDateOfBirth(resultSet.getDate("date_of_birth"));
        customer.setCreatedAt(resultSet.getTimestamp("created_at"));
        customer.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return customer;
    }
} 