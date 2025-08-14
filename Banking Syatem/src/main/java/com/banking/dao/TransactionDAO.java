package com.banking.dao;

import com.banking.model.Transaction;
import com.banking.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Transaction operations
 */
public class TransactionDAO {
    
    /**
     * Create a new transaction
     * @param transaction Transaction object to create
     * @return Transaction with generated ID
     * @throws SQLException if database operation fails
     */
    public Transaction createTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (transaction_type, from_account_id, to_account_id, amount, description) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setString(1, transaction.getTransactionType().getValue());
            statement.setObject(2, transaction.getFromAccountId());
            statement.setObject(3, transaction.getToAccountId());
            statement.setBigDecimal(4, transaction.getAmount());
            statement.setString(5, transaction.getDescription());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating transaction failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    transaction.setTransactionId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating transaction failed, no ID obtained.");
                }
            }
            
            return transaction;
        }
    }
    
    /**
     * Get transaction by ID
     * @param transactionId Transaction ID
     * @return Transaction object or null if not found
     * @throws SQLException if database operation fails
     */
    public Transaction getTransactionById(int transactionId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, transactionId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToTransaction(resultSet);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get all transactions for an account
     * @param accountId Account ID
     * @return List of account transactions
     * @throws SQLException if database operation fails
     */
    public List<Transaction> getTransactionsByAccountId(int accountId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE from_account_id = ? OR to_account_id = ? " +
                    "ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, accountId);
            statement.setInt(2, accountId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(mapResultSetToTransaction(resultSet));
                }
            }
        }
        
        return transactions;
    }
    
    /**
     * Get all transactions
     * @return List of all transactions
     * @throws SQLException if database operation fails
     */
    public List<Transaction> getAllTransactions() throws SQLException {
        String sql = "SELECT * FROM transactions ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                transactions.add(mapResultSetToTransaction(resultSet));
            }
        }
        
        return transactions;
    }
    
    /**
     * Get transactions by type
     * @param transactionType Transaction type
     * @return List of transactions of specified type
     * @throws SQLException if database operation fails
     */
    public List<Transaction> getTransactionsByType(Transaction.TransactionType transactionType) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE transaction_type = ? ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, transactionType.getValue());
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(mapResultSetToTransaction(resultSet));
                }
            }
        }
        
        return transactions;
    }
    
    /**
     * Get transactions within a date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of transactions within date range
     * @throws SQLException if database operation fails
     */
    public List<Transaction> getTransactionsByDateRange(Date startDate, Date endDate) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE DATE(transaction_date) BETWEEN ? AND ? " +
                    "ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setDate(1, startDate);
            statement.setDate(2, endDate);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(mapResultSetToTransaction(resultSet));
                }
            }
        }
        
        return transactions;
    }
    
    /**
     * Get recent transactions (last N transactions)
     * @param limit Number of recent transactions to retrieve
     * @return List of recent transactions
     * @throws SQLException if database operation fails
     */
    public List<Transaction> getRecentTransactions(int limit) throws SQLException {
        String sql = "SELECT * FROM transactions ORDER BY transaction_date DESC LIMIT ?";
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, limit);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(mapResultSetToTransaction(resultSet));
                }
            }
        }
        
        return transactions;
    }
    
    /**
     * Get transaction summary for an account
     * @param accountId Account ID
     * @return Transaction summary with counts and totals
     * @throws SQLException if database operation fails
     */
    public TransactionSummary getTransactionSummary(int accountId) throws SQLException {
        String sql = "SELECT " +
                    "COUNT(*) as total_transactions, " +
                    "SUM(CASE WHEN transaction_type = 'DEPOSIT' THEN amount ELSE 0 END) as total_deposits, " +
                    "SUM(CASE WHEN transaction_type = 'WITHDRAWAL' THEN amount ELSE 0 END) as total_withdrawals, " +
                    "SUM(CASE WHEN transaction_type = 'TRANSFER' AND to_account_id = ? THEN amount ELSE 0 END) as total_received, " +
                    "SUM(CASE WHEN transaction_type = 'TRANSFER' AND from_account_id = ? THEN amount ELSE 0 END) as total_sent " +
                    "FROM transactions WHERE from_account_id = ? OR to_account_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, accountId);
            statement.setInt(2, accountId);
            statement.setInt(3, accountId);
            statement.setInt(4, accountId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new TransactionSummary(
                        resultSet.getInt("total_transactions"),
                        resultSet.getBigDecimal("total_deposits"),
                        resultSet.getBigDecimal("total_withdrawals"),
                        resultSet.getBigDecimal("total_received"),
                        resultSet.getBigDecimal("total_sent")
                    );
                }
            }
        }
        
        return new TransactionSummary(0, null, null, null, null);
    }
    
    /**
     * Delete transaction by ID
     * @param transactionId Transaction ID to delete
     * @return true if deletion successful, false otherwise
     * @throws SQLException if database operation fails
     */
    public boolean deleteTransaction(int transactionId) throws SQLException {
        String sql = "DELETE FROM transactions WHERE transaction_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, transactionId);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    /**
     * Map ResultSet to Transaction object
     * @param resultSet ResultSet containing transaction data
     * @return Transaction object
     * @throws SQLException if mapping fails
     */
    private Transaction mapResultSetToTransaction(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(resultSet.getInt("transaction_id"));
        transaction.setTransactionType(Transaction.TransactionType.fromString(resultSet.getString("transaction_type")));
        
        Integer fromAccountId = resultSet.getObject("from_account_id", Integer.class);
        transaction.setFromAccountId(fromAccountId);
        
        Integer toAccountId = resultSet.getObject("to_account_id", Integer.class);
        transaction.setToAccountId(toAccountId);
        
        transaction.setAmount(resultSet.getBigDecimal("amount"));
        transaction.setDescription(resultSet.getString("description"));
        transaction.setTransactionDate(resultSet.getTimestamp("transaction_date"));
        
        return transaction;
    }
    
    /**
     * Inner class for transaction summary
     */
    public static class TransactionSummary {
        private int totalTransactions;
        private java.math.BigDecimal totalDeposits;
        private java.math.BigDecimal totalWithdrawals;
        private java.math.BigDecimal totalReceived;
        private java.math.BigDecimal totalSent;
        
        public TransactionSummary(int totalTransactions, java.math.BigDecimal totalDeposits,
                                java.math.BigDecimal totalWithdrawals, java.math.BigDecimal totalReceived,
                                java.math.BigDecimal totalSent) {
            this.totalTransactions = totalTransactions;
            this.totalDeposits = totalDeposits != null ? totalDeposits : java.math.BigDecimal.ZERO;
            this.totalWithdrawals = totalWithdrawals != null ? totalWithdrawals : java.math.BigDecimal.ZERO;
            this.totalReceived = totalReceived != null ? totalReceived : java.math.BigDecimal.ZERO;
            this.totalSent = totalSent != null ? totalSent : java.math.BigDecimal.ZERO;
        }
        
        // Getters
        public int getTotalTransactions() { return totalTransactions; }
        public java.math.BigDecimal getTotalDeposits() { return totalDeposits; }
        public java.math.BigDecimal getTotalWithdrawals() { return totalWithdrawals; }
        public java.math.BigDecimal getTotalReceived() { return totalReceived; }
        public java.math.BigDecimal getTotalSent() { return totalSent; }
    }
} 