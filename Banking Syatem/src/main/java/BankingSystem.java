import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.Transaction;
import com.banking.service.BankingService;
import com.banking.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

/**
 * Main Banking System Application
 * Console-based banking system with JDBC and MySQL
 */
public class BankingSystem {
    private static BankingService bankingService;
    private static Scanner scanner;
    private static SimpleDateFormat dateFormat;
    
    public static void main(String[] args) {
        System.out.println("=== WELCOME TO SIMPLE BANKING SYSTEM ===");
        System.out.println("Java + MySQL + JDBC Implementation\n");
        
        // Initialize components
        bankingService = new BankingService();
        scanner = new Scanner(System.in);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        // Test database connection
        if (!DatabaseConnection.testConnection()) {
            System.err.println("ERROR: Cannot connect to database!");
            System.err.println("Please ensure MySQL is running and database is set up correctly.");
            System.err.println("Database URL: " + DatabaseConnection.getUrl());
            System.err.println("Username: " + DatabaseConnection.getUsername());
            System.err.println("Password: " + DatabaseConnection.getPassword());
            return;
        }
        
        System.out.println("✓ Database connection successful!");
        System.out.println();
        
        // Start the application
        showMainMenu();
    }
    
    /**
     * Display main menu and handle user input
     */
    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Create New Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. Check Balance");
            System.out.println("6. View Transaction History");
            System.out.println("7. Update PIN");
            System.out.println("8. View All Accounts");
            System.out.println("9. View All Customers");
            System.out.println("10. Search Customer");
            System.out.println("0. Exit");
            System.out.print("\nEnter your choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1:
                        createNewAccount();
                        break;
                    case 2:
                        depositMoney();
                        break;
                    case 3:
                        withdrawMoney();
                        break;
                    case 4:
                        transferMoney();
                        break;
                    case 5:
                        checkBalance();
                        break;
                    case 6:
                        viewTransactionHistory();
                        break;
                    case 7:
                        updatePin();
                        break;
                    case 8:
                        viewAllAccounts();
                        break;
                    case 9:
                        viewAllCustomers();
                        break;
                    case 10:
                        searchCustomer();
                        break;
                    case 0:
                        System.out.println("\nThank you for using Simple Banking System!");
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
            }
        }
    }
    
    /**
     * Create a new customer account
     */
    private static void createNewAccount() {
        System.out.println("\n=== CREATE NEW ACCOUNT ===");
        
        try {
            // Get customer information
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine().trim();
            
            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine().trim();
            
            System.out.print("Enter Email: ");
            String email = scanner.nextLine().trim();
            
            System.out.print("Enter Phone: ");
            String phone = scanner.nextLine().trim();
            
            System.out.print("Enter Address: ");
            String address = scanner.nextLine().trim();
            
            System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
            String dobStr = scanner.nextLine().trim();
            Date dateOfBirth = Date.valueOf(dobStr);
            
            // Get account type
            System.out.println("\nAccount Types:");
            System.out.println("1. Savings");
            System.out.println("2. Checking");
            System.out.println("3. Fixed Deposit");
            System.out.print("Select Account Type (1-3): ");
            int accountTypeChoice = Integer.parseInt(scanner.nextLine().trim());
            
            Account.AccountType accountType;
            switch (accountTypeChoice) {
                case 1:
                    accountType = Account.AccountType.SAVINGS;
                    break;
                case 2:
                    accountType = Account.AccountType.CHECKING;
                    break;
                case 3:
                    accountType = Account.AccountType.FIXED_DEPOSIT;
                    break;
                default:
                    System.out.println("Invalid account type! Using Savings account.");
                    accountType = Account.AccountType.SAVINGS;
            }
            
            // Get initial balance
            System.out.print("Enter Initial Balance: $");
            BigDecimal initialBalance = new BigDecimal(scanner.nextLine().trim());
            
            // Create account
            Account account = bankingService.createCustomerAccount(
                firstName, lastName, email, phone, address, dateOfBirth, accountType, initialBalance
            );
            
            System.out.println("\n✓ Account created successfully!");
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Account Type: " + account.getAccountType());
            System.out.println("Initial Balance: $" + account.getBalance());
            System.out.println("Default PIN: 1234 (Please change it for security)");
            
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Deposit money into an account
     */
    private static void depositMoney() {
        System.out.println("\n=== DEPOSIT MONEY ===");
        
        try {
            System.out.print("Enter Account Number: ");
            String accountNumber = scanner.nextLine().trim();
            
            System.out.print("Enter Amount to Deposit: $");
            BigDecimal amount = new BigDecimal(scanner.nextLine().trim());
            
            System.out.print("Enter Description (optional): ");
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                description = null;
            }
            
            Account account = bankingService.depositMoney(accountNumber, amount, description);
            
            System.out.println("\n✓ Deposit successful!");
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Amount Deposited: $" + amount);
            System.out.println("New Balance: $" + account.getBalance());
            
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Withdraw money from an account
     */
    private static void withdrawMoney() {
        System.out.println("\n=== WITHDRAW MONEY ===");
        
        try {
            System.out.print("Enter Account Number: ");
            String accountNumber = scanner.nextLine().trim();
            
            System.out.print("Enter Amount to Withdraw: $");
            BigDecimal amount = new BigDecimal(scanner.nextLine().trim());
            
            System.out.print("Enter Description (optional): ");
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                description = null;
            }
            
            Account account = bankingService.withdrawMoney(accountNumber, amount, description);
            
            System.out.println("\n✓ Withdrawal successful!");
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Amount Withdrawn: $" + amount);
            System.out.println("New Balance: $" + account.getBalance());
            
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Transfer money between accounts
     */
    private static void transferMoney() {
        System.out.println("\n=== TRANSFER MONEY ===");
        
        try {
            System.out.print("Enter Source Account Number: ");
            String fromAccountNumber = scanner.nextLine().trim();
            
            System.out.print("Enter Destination Account Number: ");
            String toAccountNumber = scanner.nextLine().trim();
            
            System.out.print("Enter Amount to Transfer: $");
            BigDecimal amount = new BigDecimal(scanner.nextLine().trim());
            
            System.out.print("Enter Description (optional): ");
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                description = null;
            }
            
            BankingService.TransferResult result = bankingService.transferMoney(
                fromAccountNumber, toAccountNumber, amount, description
            );
            
            System.out.println("\n✓ Transfer successful!");
            System.out.println("From Account: " + result.getFromAccount().getAccountNumber());
            System.out.println("To Account: " + result.getToAccount().getAccountNumber());
            System.out.println("Amount Transferred: $" + result.getAmount());
            System.out.println("From Account New Balance: $" + result.getFromAccount().getBalance());
            System.out.println("To Account New Balance: $" + result.getToAccount().getBalance());
            
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Check account balance
     */
    private static void checkBalance() {
        System.out.println("\n=== CHECK BALANCE ===");
        
        try {
            System.out.print("Enter Account Number: ");
            String accountNumber = scanner.nextLine().trim();
            
            Account account = bankingService.getAccountDetails(accountNumber);
            Customer customer = bankingService.getCustomerDetails(account.getCustomerId());
            
            System.out.println("\n=== ACCOUNT DETAILS ===");
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Customer Name: " + customer.getFullName());
            System.out.println("Account Type: " + account.getAccountType());
            System.out.println("Current Balance: $" + account.getBalance());
            System.out.println("PIN: **** (hidden for security)");
            System.out.println("Account Status: " + account.getStatus());
            System.out.println("Created Date: " + account.getCreatedAt());
            
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * View transaction history for an account
     */
    private static void viewTransactionHistory() {
        System.out.println("\n=== TRANSACTION HISTORY ===");
        
        try {
            System.out.print("Enter Account Number: ");
            String accountNumber = scanner.nextLine().trim();
            
            List<Transaction> transactions = bankingService.getTransactionHistory(accountNumber);
            
            if (transactions.isEmpty()) {
                System.out.println("No transactions found for this account.");
                return;
            }
            
            System.out.println("\n=== TRANSACTION HISTORY FOR ACCOUNT " + accountNumber + " ===");
            System.out.printf("%-5s %-15s %-12s %-30s %-20s%n", 
                "ID", "TYPE", "AMOUNT", "DESCRIPTION", "DATE");
            System.out.println("=".repeat(85));
            
            for (Transaction transaction : transactions) {
                System.out.printf("%-5d %-15s $%-11s %-30s %-20s%n",
                    transaction.getTransactionId(),
                    transaction.getTransactionType(),
                    transaction.getAmount(),
                    transaction.getDescription() != null ? 
                        (transaction.getDescription().length() > 28 ? 
                         transaction.getDescription().substring(0, 25) + "..." : 
                         transaction.getDescription()) : "N/A",
                    transaction.getTransactionDate()
                );
            }
            
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * View all accounts
     */
    private static void viewAllAccounts() {
        System.out.println("\n=== ALL ACCOUNTS ===");
        
        try {
            List<Account> accounts = bankingService.getAllAccounts();
            
            if (accounts.isEmpty()) {
                System.out.println("No accounts found.");
                return;
            }
            
            System.out.printf("%-8s %-15s %-8s %-12s %-10s %-20s%n", 
                "ACC ID", "ACCOUNT NO", "CUST ID", "TYPE", "BALANCE", "STATUS");
            System.out.println("=".repeat(75));
            
            for (Account account : accounts) {
                System.out.printf("%-8d %-15s %-8d %-12s $%-9s %-20s%n",
                    account.getAccountId(),
                    account.getAccountNumber(),
                    account.getCustomerId(),
                    account.getAccountType(),
                    account.getBalance(),
                    account.getStatus()
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * View all customers
     */
    private static void viewAllCustomers() {
        System.out.println("\n=== ALL CUSTOMERS ===");
        
        try {
            List<Customer> customers = bankingService.getAllCustomers();
            
            if (customers.isEmpty()) {
                System.out.println("No customers found.");
                return;
            }
            
            System.out.printf("%-8s %-20s %-25s %-15s %-15s%n", 
                "CUST ID", "NAME", "EMAIL", "PHONE", "DATE OF BIRTH");
            System.out.println("=".repeat(85));
            
            for (Customer customer : customers) {
                System.out.printf("%-8d %-20s %-25s %-15s %-15s%n",
                    customer.getCustomerId(),
                    customer.getFullName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getDateOfBirth()
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Update account PIN
     */
    private static void updatePin() {
        System.out.println("\n=== UPDATE PIN ===");
        
        try {
            System.out.print("Enter Account Number: ");
            String accountNumber = scanner.nextLine().trim();
            
            System.out.print("Enter Current PIN: ");
            String currentPin = scanner.nextLine().trim();
            
            System.out.print("Enter New PIN (4 digits): ");
            String newPin = scanner.nextLine().trim();
            
            boolean success = bankingService.updatePin(accountNumber, currentPin, newPin);
            
            if (success) {
                System.out.println("\n✓ PIN updated successfully!");
                System.out.println("Account Number: " + accountNumber);
                System.out.println("New PIN: " + newPin);
            } else {
                System.out.println("\n✗ Failed to update PIN. Please try again.");
            }
            
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Search customer by name
     */
    private static void searchCustomer() {
        System.out.println("\n=== SEARCH CUSTOMER ===");
        
        try {
            System.out.print("Enter customer name to search: ");
            String searchName = scanner.nextLine().trim();
            
            List<Customer> customers = bankingService.searchCustomersByName(searchName);
            
            if (customers.isEmpty()) {
                System.out.println("No customers found matching: " + searchName);
                return;
            }
            
            System.out.println("\n=== SEARCH RESULTS ===");
            System.out.printf("%-8s %-20s %-25s %-15s %-15s%n", 
                "CUST ID", "NAME", "EMAIL", "PHONE", "DATE OF BIRTH");
            System.out.println("=".repeat(85));
            
            for (Customer customer : customers) {
                System.out.printf("%-8d %-20s %-25s %-15s %-15s%n",
                    customer.getCustomerId(),
                    customer.getFullName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getDateOfBirth()
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
} 