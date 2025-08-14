package com.banking.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Transaction model class representing a bank transaction
 */
public class Transaction {
    private int transactionId;
    private TransactionType transactionType;
    private Integer fromAccountId;
    private Integer toAccountId;
    private BigDecimal amount;
    private String description;
    private Timestamp transactionDate;

    // Transaction type enum
    public enum TransactionType {
        DEPOSIT("DEPOSIT"),
        WITHDRAWAL("WITHDRAWAL"),
        TRANSFER("TRANSFER"),
        OPENING_BALANCE("OPENING_BALANCE");

        private final String value;

        TransactionType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static TransactionType fromString(String text) {
            for (TransactionType type : TransactionType.values()) {
                if (type.value.equalsIgnoreCase(text)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("No constant with text " + text + " found");
        }
    }

    // Default constructor
    public Transaction() {}

    // Constructor with all fields
    public Transaction(int transactionId, TransactionType transactionType, 
                      Integer fromAccountId, Integer toAccountId, 
                      BigDecimal amount, String description, Timestamp transactionDate) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.description = description;
        this.transactionDate = transactionDate;
    }

    // Constructor without ID (for new transactions)
    public Transaction(TransactionType transactionType, Integer fromAccountId, 
                      Integer toAccountId, BigDecimal amount, String description) {
        this.transactionType = transactionType;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.description = description;
        this.transactionDate = new Timestamp(System.currentTimeMillis());
    }

    // Constructor for deposit/withdrawal
    public Transaction(TransactionType transactionType, Integer accountId, 
                      BigDecimal amount, String description) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
        this.transactionDate = new Timestamp(System.currentTimeMillis());
        
        if (transactionType == TransactionType.DEPOSIT) {
            this.toAccountId = accountId;
        } else if (transactionType == TransactionType.WITHDRAWAL) {
            this.fromAccountId = accountId;
        }
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Integer fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Integer getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Integer toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    // Helper methods
    public boolean isDeposit() {
        return transactionType == TransactionType.DEPOSIT;
    }

    public boolean isWithdrawal() {
        return transactionType == TransactionType.WITHDRAWAL;
    }

    public boolean isTransfer() {
        return transactionType == TransactionType.TRANSFER;
    }

    public boolean isOpeningBalance() {
        return transactionType == TransactionType.OPENING_BALANCE;
    }

    public String getFormattedAmount() {
        return "$" + amount.toString();
    }

    public String getFormattedDate() {
        if (transactionDate != null) {
            return transactionDate.toString();
        }
        return "N/A";
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", transactionType=" + transactionType +
                ", fromAccountId=" + fromAccountId +
                ", toAccountId=" + toAccountId +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", transactionDate=" + transactionDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return transactionId == that.transactionId;
    }

    @Override
    public int hashCode() {
        return transactionId;
    }
} 