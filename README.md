# Simple Banking System

A Java-based banking system using MySQL and JDBC for managing customer accounts and transactions.

## Features

- **Account Management**: Create, view, and manage customer accounts
- **Transaction Operations**: Deposit, withdraw, and transfer money
- **Balance Inquiry**: Check account balances
- **Transaction History**: View transaction records
- **Database Integration**: MySQL database with JDBC connectivity

## Project Structure

```
Banking System/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   └── banking/
│   │   │   │       ├── model/
│   │   │   │       ├── dao/
│   │   │   │       ├── service/
│   │   │   │       └── util/
│   │   │   └── BankingSystem.java
│   │   └── resources/
│   │       └── database.sql
├── lib/
├── README.md
└── pom.xml
```

## Prerequisites

- Java 8 or higher
- MySQL 5.7 or higher
- Maven (for dependency management)

## Setup Instructions

### 1. Database Setup

1. Install MySQL if not already installed
2. Create a new database:
   ```sql
   CREATE DATABASE banking_system;
   USE banking_system;
   ```
3. Run the SQL script in `src/main/resources/database.sql` to create tables

### 2. Database Configuration

Update the database connection details in `DatabaseConnection.java`:
- Host: localhost
- Port: 3306
- Database: banking_system
- Username: your_username
- Password: your_password

### 3. Build and Run

```bash
# Compile the project
javac -cp "lib/*" src/main/java/*.java src/main/java/com/banking/*/*.java

# Run the application
java -cp "lib/*:src/main/java" BankingSystem
```

## Database Schema

### Tables

1. **customers**: Customer information
2. **accounts**: Bank accounts
3. **transactions**: Transaction records

### Sample Data

The system includes sample data for testing:
- Customer: John Doe (ID: 1001)
- Account: Savings Account (Balance: $5000)

## Usage

1. Run the application
2. Choose from the menu options:
   - Create Account
   - Deposit Money
   - Withdraw Money
   - Transfer Money
   - Check Balance
   - View Transaction History
   - Exit

## Features in Detail

### Account Creation
- Collect customer details (name, email, phone)
- Generate unique account number
- Set initial balance
- Create customer and account records

### Transactions
- **Deposit**: Add money to account
- **Withdraw**: Remove money from account (with balance check)
- **Transfer**: Move money between accounts

### Security Features
- Balance validation for withdrawals
- Transaction logging
- Account number validation

## Technologies Used

- **Java**: Core application logic
- **MySQL**: Database management
- **JDBC**: Database connectivity
- **Maven**: Dependency management

## Future Enhancements

- Web-based interface
- User authentication
- Interest calculation
- Loan management
- ATM simulation
- Email notifications 
