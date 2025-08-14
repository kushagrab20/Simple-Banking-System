package com.banking.dao;

import com.banking.model.Account;
import com.banking.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Account operations
 */
public class AccountDAO {
    
    /**
     * Create a new account
     * @param account Account object to create
     * @return Account with generated ID
     * @throws SQLException if database operation fails
     */
    public Account createAccount(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (account_number, customer_id, account_type, balance, pin, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setString(1, account.getAccountNumber());
            statement.setInt(2, account.getCustomerId());
            statement.setString(3, account.getAccountType().getValue());
            statement.setBigDecimal(4, account.getBalance());
            statement.setString(5, account.getPin());
            statement.setString(6, account.getStatus().getValue());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating account failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    account.setAccountId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating account failed, no ID obtained.");
                }
            }
            
            return account;
        }
    }
    
    /**
     * Get account by ID
     * @param accountId Account ID
     * @return Account object or null if not found
     * @throws SQLException if database operation fails
     */
    public Account getAccountById(int accountId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, accountId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToAccount(resultSet);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get account by account number
     * @param accountNumber Account number
     * @return Account object or null if not found
     * @throws SQLException if database operation fails
     */
    public Account getAccountByNumber(String accountNumber) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, accountNumber);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToAccount(resultSet);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get all accounts for a customer
     * @param customerId Customer ID
     * @return List of customer's accounts
     * @throws SQLException if database operation fails
     */
    public List<Account> getAccountsByCustomerId(int customerId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE customer_id = ? ORDER BY account_id";
        List<Account> accounts = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, customerId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    accounts.add(mapResultSetToAccount(resultSet));
                }
            }
        }
        
        return accounts;
    }
    
    /**
     * Get all accounts
     * @return List of all accounts
     * @throws SQLException if database operation fails
     */
    public List<Account> getAllAccounts() throws SQLException {
        String sql = "SELECT * FROM accounts ORDER BY account_id";
        List<Account> accounts = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                accounts.add(mapResultSetToAccount(resultSet));
            }
        }
        
        return accounts;
    }
    
    /**
     * Update account balance
     * @param accountId Account ID
     * @param newBalance New balance amount
     * @return true if update successful, false otherwise
     * @throws SQLException if database operation fails
     */
    public boolean updateBalance(int accountId, BigDecimal newBalance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setBigDecimal(1, newBalance);
            statement.setInt(2, accountId);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    /**
     * Update account PIN
     * @param accountId Account ID
     * @param newPin New PIN
     * @return true if update successful, false otherwise
     * @throws SQLException if database operation fails
     */
    public boolean updatePin(int accountId, String newPin) throws SQLException {
        String sql = "UPDATE accounts SET pin = ? WHERE account_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, newPin);
            statement.setInt(2, accountId);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    /**
     * Verify account PIN
     * @param accountNumber Account number
     * @param pin PIN to verify
     * @return true if PIN is correct, false otherwise
     * @throws SQLException if database operation fails
     */
    public boolean verifyPin(String accountNumber, String pin) throws SQLException {
        String sql = "SELECT pin FROM accounts WHERE account_number = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, accountNumber);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPin = resultSet.getString("pin");
                    return pin.equals(storedPin);
                }
            }
        }
        
        return false;
    }
    
    /**
     * Update account status
     * @param accountId Account ID
     * @param status New status
     * @return true if update successful, false otherwise
     * @throws SQLException if database operation fails
     */
    public boolean updateStatus(int accountId, Account.AccountStatus status) throws SQLException {
        String sql = "UPDATE accounts SET status = ? WHERE account_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, status.getValue());
            statement.setInt(2, accountId);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    /**
     * Delete account by ID
     * @param accountId Account ID to delete
     * @return true if deletion successful, false otherwise
     * @throws SQLException if database operation fails
     */
    public boolean deleteAccount(int accountId) throws SQLException {
        String sql = "DELETE FROM accounts WHERE account_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, accountId);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    /**
     * Check if account number exists
     * @param accountNumber Account number to check
     * @return true if account number exists, false otherwise
     * @throws SQLException if database operation fails
     */
    public boolean accountNumberExists(String accountNumber) throws SQLException {
        String sql = "SELECT COUNT(*) FROM accounts WHERE account_number = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, accountNumber);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Get total balance for a customer
     * @param customerId Customer ID
     * @return Total balance across all customer accounts
     * @throws SQLException if database operation fails
     */
    public BigDecimal getTotalBalanceByCustomerId(int customerId) throws SQLException {
        String sql = "SELECT SUM(balance) FROM accounts WHERE customer_id = ? AND status = 'ACTIVE'";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, customerId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    BigDecimal total = resultSet.getBigDecimal(1);
                    return total != null ? total : BigDecimal.ZERO;
                }
            }
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Generate unique account number
     * @return Unique account number
     * @throws SQLException if database operation fails
     */
    public String generateAccountNumber() throws SQLException {
        String sql = "SELECT MAX(CAST(SUBSTRING(account_number, 4) AS UNSIGNED)) FROM accounts";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            int nextNumber = 1;
            if (resultSet.next()) {
                int maxNumber = resultSet.getInt(1);
                nextNumber = maxNumber + 1;
            }
            
            return String.format("ACC%03d", nextNumber);
        }
    }
    
    /**
     * Map ResultSet to Account object
     * @param resultSet ResultSet containing account data
     * @return Account object
     * @throws SQLException if mapping fails
     */
    private Account mapResultSetToAccount(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setAccountId(resultSet.getInt("account_id"));
        account.setAccountNumber(resultSet.getString("account_number"));
        account.setCustomerId(resultSet.getInt("customer_id"));
        account.setAccountType(Account.AccountType.fromString(resultSet.getString("account_type")));
        account.setBalance(resultSet.getBigDecimal("balance"));
        account.setPin(resultSet.getString("pin"));
        account.setStatus(Account.AccountStatus.fromString(resultSet.getString("status")));
        account.setCreatedAt(resultSet.getTimestamp("created_at"));
        account.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return account;
    }
} 