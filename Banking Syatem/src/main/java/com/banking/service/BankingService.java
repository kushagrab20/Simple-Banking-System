package com.banking.service;

import com.banking.dao.AccountDAO;
import com.banking.dao.CustomerDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.Transaction;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Service class for banking operations
 */
public class BankingService {
    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    
    public BankingService() {
        this.customerDAO = new CustomerDAO();
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
    }
    
    /**
     * Create a new customer account
     * @param firstName Customer first name
     * @param lastName Customer last name
     * @param email Customer email
     * @param phone Customer phone
     * @param address Customer address
     * @param dateOfBirth Customer date of birth
     * @param accountType Type of account to create
     * @param initialBalance Initial balance for the account
     * @param pin 4-digit PIN for the account
     * @return Created account
     * @throws SQLException if database operation fails
     * @throws IllegalArgumentException if validation fails
     */
    public Account createCustomerAccount(String firstName, String lastName, String email,
                                       String phone, String address, java.sql.Date dateOfBirth,
                                       Account.AccountType accountType, BigDecimal initialBalance,
                                       String pin) 
                                       throws SQLException, IllegalArgumentException {
        
        // Validate input parameters
        validateCustomerData(firstName, lastName, email, phone, address, dateOfBirth);
        validateAccountData(accountType, initialBalance);
        
        // Validate PIN
        if (pin == null || pin.length() != 4 || !pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must be a 4-digit number");
        }
        
        // Check if email already exists
        if (customerDAO.emailExists(email)) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }
        
        // Create customer
        Customer customer = new Customer(firstName, lastName, email, phone, address, dateOfBirth);
        customer = customerDAO.createCustomer(customer);
        
        // Generate account number
        String accountNumber = accountDAO.generateAccountNumber();
        
        // Create account with provided PIN
        Account account = new Account(accountNumber, customer.getCustomerId(), accountType, initialBalance, pin);
        account = accountDAO.createAccount(account);
        
        // Create opening balance transaction if initial balance > 0
        if (initialBalance.compareTo(BigDecimal.ZERO) > 0) {
            Transaction openingTransaction = new Transaction(
                Transaction.TransactionType.OPENING_BALANCE,
                account.getAccountId(),
                initialBalance,
                "Initial deposit for " + accountType.toString().toLowerCase() + " account"
            );
            transactionDAO.createTransaction(openingTransaction);
        }
        
        return account;
    }
    
    /**
     * Deposit money into an account
     * @param accountNumber Account number
     * @param amount Amount to deposit
     * @param description Transaction description
     * @return Updated account
     * @throws SQLException if database operation fails
     * @throws IllegalArgumentException if validation fails
     */
    public Account depositMoney(String accountNumber, BigDecimal amount, String description) 
                               throws SQLException, IllegalArgumentException {
        
        // Validate input
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        
        // Get account
        Account account = accountDAO.getAccountByNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        
        if (!account.isActive()) {
            throw new IllegalArgumentException("Account is not active: " + accountNumber);
        }
        
        // Update balance
        BigDecimal newBalance = account.getBalance().add(amount);
        accountDAO.updateBalance(account.getAccountId(), newBalance);
        account.setBalance(newBalance);
        
        // Create transaction record
        Transaction transaction = new Transaction(
            Transaction.TransactionType.DEPOSIT,
            account.getAccountId(),
            amount,
            description != null ? description : "Cash deposit"
        );
        transactionDAO.createTransaction(transaction);
        
        return account;
    }
    
    /**
     * Withdraw money from an account
     * @param accountNumber Account number
     * @param amount Amount to withdraw
     * @param description Transaction description
     * @return Updated account
     * @throws SQLException if database operation fails
     * @throws IllegalArgumentException if validation fails
     */
    public Account withdrawMoney(String accountNumber, BigDecimal amount, String description) 
                                throws SQLException, IllegalArgumentException {
        
        // Validate input
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        
        // Get account
        Account account = accountDAO.getAccountByNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        
        if (!account.isActive()) {
            throw new IllegalArgumentException("Account is not active: " + accountNumber);
        }
        
        // Check sufficient balance
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance. Available: $" + account.getBalance());
        }
        
        // Update balance
        BigDecimal newBalance = account.getBalance().subtract(amount);
        accountDAO.updateBalance(account.getAccountId(), newBalance);
        account.setBalance(newBalance);
        
        // Create transaction record
        Transaction transaction = new Transaction(
            Transaction.TransactionType.WITHDRAWAL,
            account.getAccountId(),
            amount,
            description != null ? description : "Cash withdrawal"
        );
        transactionDAO.createTransaction(transaction);
        
        return account;
    }
    
    /**
     * Transfer money between accounts
     * @param fromAccountNumber Source account number
     * @param toAccountNumber Destination account number
     * @param amount Amount to transfer
     * @param description Transaction description
     * @return Transfer result with updated accounts
     * @throws SQLException if database operation fails
     * @throws IllegalArgumentException if validation fails
     */
    public TransferResult transferMoney(String fromAccountNumber, String toAccountNumber, 
                                      BigDecimal amount, String description) 
                                      throws SQLException, IllegalArgumentException {
        
        // Validate input
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        
        // Get accounts
        Account fromAccount = accountDAO.getAccountByNumber(fromAccountNumber);
        Account toAccount = accountDAO.getAccountByNumber(toAccountNumber);
        
        if (fromAccount == null) {
            throw new IllegalArgumentException("Source account not found: " + fromAccountNumber);
        }
        
        if (toAccount == null) {
            throw new IllegalArgumentException("Destination account not found: " + toAccountNumber);
        }
        
        if (!fromAccount.isActive()) {
            throw new IllegalArgumentException("Source account is not active: " + fromAccountNumber);
        }
        
        if (!toAccount.isActive()) {
            throw new IllegalArgumentException("Destination account is not active: " + toAccountNumber);
        }
        
        // Check sufficient balance
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance in source account. Available: $" + fromAccount.getBalance());
        }
        
        // Update balances
        BigDecimal fromNewBalance = fromAccount.getBalance().subtract(amount);
        BigDecimal toNewBalance = toAccount.getBalance().add(amount);
        
        accountDAO.updateBalance(fromAccount.getAccountId(), fromNewBalance);
        accountDAO.updateBalance(toAccount.getAccountId(), toNewBalance);
        
        fromAccount.setBalance(fromNewBalance);
        toAccount.setBalance(toNewBalance);
        
        // Create transaction record
        Transaction transaction = new Transaction(
            Transaction.TransactionType.TRANSFER,
            fromAccount.getAccountId(),
            toAccount.getAccountId(),
            amount,
            description != null ? description : "Transfer from " + fromAccountNumber + " to " + toAccountNumber
        );
        transactionDAO.createTransaction(transaction);
        
        return new TransferResult(fromAccount, toAccount, amount);
    }
    
    /**
     * Get account balance
     * @param accountNumber Account number
     * @return Account balance
     * @throws SQLException if database operation fails
     * @throws IllegalArgumentException if account not found
     */
    public BigDecimal getAccountBalance(String accountNumber) throws SQLException, IllegalArgumentException {
        Account account = accountDAO.getAccountByNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        return account.getBalance();
    }
    
    /**
     * Get account details
     * @param accountNumber Account number
     * @return Account object
     * @throws SQLException if database operation fails
     * @throws IllegalArgumentException if account not found
     */
    public Account getAccountDetails(String accountNumber) throws SQLException, IllegalArgumentException {
        Account account = accountDAO.getAccountByNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        return account;
    }
    
    /**
     * Get customer details
     * @param customerId Customer ID
     * @return Customer object
     * @throws SQLException if database operation fails
     * @throws IllegalArgumentException if customer not found
     */
    public Customer getCustomerDetails(int customerId) throws SQLException, IllegalArgumentException {
        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }
        return customer;
    }
    
    /**
     * Get all accounts for a customer
     * @param customerId Customer ID
     * @return List of customer accounts
     * @throws SQLException if database operation fails
     */
    public List<Account> getCustomerAccounts(int customerId) throws SQLException {
        return accountDAO.getAccountsByCustomerId(customerId);
    }
    
    /**
     * Get transaction history for an account
     * @param accountNumber Account number
     * @return List of transactions
     * @throws SQLException if database operation fails
     * @throws IllegalArgumentException if account not found
     */
    public List<Transaction> getTransactionHistory(String accountNumber) throws SQLException, IllegalArgumentException {
        Account account = accountDAO.getAccountByNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        return transactionDAO.getTransactionsByAccountId(account.getAccountId());
    }
    
    /**
     * Get all customers
     * @return List of all customers
     * @throws SQLException if database operation fails
     */
    public List<Customer> getAllCustomers() throws SQLException {
        return customerDAO.getAllCustomers();
    }
    
    /**
     * Update account PIN
     * @param accountNumber Account number
     * @param currentPin Current PIN
     * @param newPin New PIN
     * @return true if update successful, false otherwise
     * @throws SQLException if database operation fails
     * @throws IllegalArgumentException if validation fails
     */
    public boolean updatePin(String accountNumber, String currentPin, String newPin) 
                            throws SQLException, IllegalArgumentException {
        
        // Validate PIN format
        if (newPin == null || newPin.length() != 4 || !newPin.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must be exactly 4 digits");
        }
        
        // Get account
        Account account = accountDAO.getAccountByNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        
        // Verify current PIN
        if (!accountDAO.verifyPin(accountNumber, currentPin)) {
            throw new IllegalArgumentException("Current PIN is incorrect");
        }
        
        // Update PIN
        return accountDAO.updatePin(account.getAccountId(), newPin);
    }
    
    /**
     * Verify account PIN
     * @param accountNumber Account number
     * @param pin PIN to verify
     * @return true if PIN is correct, false otherwise
     * @throws SQLException if database operation fails
     */
    public boolean verifyPin(String accountNumber, String pin) throws SQLException {
        return accountDAO.verifyPin(accountNumber, pin);
    }
    
    /**
     * Get all accounts
     * @return List of all accounts
     * @throws SQLException if database operation fails
     */
    public List<Account> getAllAccounts() throws SQLException {
        return accountDAO.getAllAccounts();
    }
    
    /**
     * Search customers by name
     * @param name Name to search for
     * @return List of matching customers
     * @throws SQLException if database operation fails
     */
    public List<Customer> searchCustomersByName(String name) throws SQLException {
        return customerDAO.searchCustomersByName(name);
    }
    
    // Validation methods
    private void validateCustomerData(String firstName, String lastName, String email, 
                                    String phone, String address, java.sql.Date dateOfBirth) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("Valid email is required");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address is required");
        }
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth is required");
        }
    }
    
    private void validateAccountData(Account.AccountType accountType, BigDecimal initialBalance) {
        if (accountType == null) {
            throw new IllegalArgumentException("Account type is required");
        }
        if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance must be non-negative");
        }
    }
    
    /**
     * Inner class for transfer result
     */
    public static class TransferResult {
        private Account fromAccount;
        private Account toAccount;
        private BigDecimal amount;
        
        public TransferResult(Account fromAccount, Account toAccount, BigDecimal amount) {
            this.fromAccount = fromAccount;
            this.toAccount = toAccount;
            this.amount = amount;
        }
        
        // Getters
        public Account getFromAccount() { return fromAccount; }
        public Account getToAccount() { return toAccount; }
        public BigDecimal getAmount() { return amount; }
    }
} 