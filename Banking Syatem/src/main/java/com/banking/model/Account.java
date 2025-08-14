package com.banking.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Account model class representing a bank account
 */
public class Account {
    private int accountId;
    private String accountNumber;
    private int customerId;
    private AccountType accountType;
    private BigDecimal balance;
    private String pin;
    private AccountStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Account type enum
    public enum AccountType {
        SAVINGS("SAVINGS"),
        CHECKING("CHECKING"),
        FIXED_DEPOSIT("FIXED_DEPOSIT");

        private final String value;

        AccountType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static AccountType fromString(String text) {
            for (AccountType type : AccountType.values()) {
                if (type.value.equalsIgnoreCase(text)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("No constant with text " + text + " found");
        }
    }

    // Account status enum
    public enum AccountStatus {
        ACTIVE("ACTIVE"),
        INACTIVE("INACTIVE"),
        SUSPENDED("SUSPENDED");

        private final String value;

        AccountStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static AccountStatus fromString(String text) {
            for (AccountStatus status : AccountStatus.values()) {
                if (status.value.equalsIgnoreCase(text)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("No constant with text " + text + " found");
        }
    }

    // Default constructor
    public Account() {
        this.balance = BigDecimal.ZERO;
        this.status = AccountStatus.ACTIVE;
    }

    // Constructor with all fields
    public Account(int accountId, String accountNumber, int customerId, 
                  AccountType accountType, BigDecimal balance, String pin, AccountStatus status) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = balance;
        this.pin = pin;
        this.status = status;
    }

    // Constructor without ID (for new accounts)
    public Account(String accountNumber, int customerId, AccountType accountType, BigDecimal balance, String pin) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = balance;
        this.pin = pin;
        this.status = AccountStatus.ACTIVE;
    }

    // Getters and Setters
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Helper methods for balance operations
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            this.balance = this.balance.add(amount);
        } else {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
    }

    public boolean withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            if (this.balance.compareTo(amount) >= 0) {
                this.balance = this.balance.subtract(amount);
                return true;
            } else {
                return false; // Insufficient funds
            }
        } else {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
    }

    public boolean isActive() {
        return this.status == AccountStatus.ACTIVE;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", accountNumber='" + accountNumber + '\'' +
                ", customerId=" + customerId +
                ", accountType=" + accountType +
                ", balance=" + balance +
                ", pin='****'" +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return accountId == account.accountId && 
               accountNumber.equals(account.accountNumber);
    }

    @Override
    public int hashCode() {
        int result = accountId;
        result = 31 * result + accountNumber.hashCode();
        return result;
    }
} 