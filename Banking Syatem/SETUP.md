# Banking System Setup Guide

This guide will help you set up the Banking System application with MySQL database.

## Prerequisites

1. **Java 8 or higher**
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Or use OpenJDK: https://adoptium.net/

2. **MySQL 5.7 or higher**
   - Download from: https://dev.mysql.com/downloads/mysql/
   - Or use XAMPP: https://www.apachefriends.org/

3. **Git** (optional, for version control)
   - Download from: https://git-scm.com/

## Database Setup

### Step 1: Install MySQL
1. Download and install MySQL Server
2. During installation, set a root password (remember this!)
3. Ensure MySQL service is running

### Step 2: Create Database
1. Open MySQL Command Line Client or MySQL Workbench
2. Connect as root user
3. Run the following commands:

```sql
CREATE DATABASE banking_system;
USE banking_system;
```

### Step 3: Run Database Schema
1. Open the SQL script: `src/main/resources/database.sql`
2. Execute the entire script in your MySQL client
3. This will create all necessary tables and sample data

### Step 4: Verify Database Setup
Run these queries to verify the setup:

```sql
USE banking_system;
SHOW TABLES;
SELECT * FROM customers;
SELECT * FROM accounts;
SELECT * FROM transactions;
```

## Application Setup

### Step 1: Configure Database Connection
1. Open `src/main/java/com/banking/util/DatabaseConnection.java`
2. Update the database configuration:
   ```java
   private static final String USERNAME = "your_mysql_username";
   private static final String PASSWORD = "your_mysql_password";
   ```
3. If using a different host or port, update the URL:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/banking_system";
   ```

### Step 2: Build and Run

#### Option A: Using the Build Script (Windows)
1. Double-click `build.bat`
2. The script will automatically:
   - Check Java installation
   - Download MySQL JDBC driver
   - Compile the application
   - Run the application

#### Option B: Manual Build
1. Create `lib` directory
2. Download MySQL JDBC driver:
   ```
   https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar
   ```
3. Place the JAR file in the `lib` directory
4. Compile the application:
   ```bash
   javac -cp "lib/*" -d . src/main/java/*.java src/main/java/com/banking/*/*.java
   ```
5. Run the application:
   ```bash
   java -cp "lib/*;." BankingSystem
   ```

#### Option C: Using Maven
1. Ensure Maven is installed
2. Run: `mvn clean compile`
3. Run: `mvn exec:java -Dexec.mainClass="BankingSystem"`

## Testing the Application

### Step 1: Verify Database Connection
When you run the application, you should see:
```
=== WELCOME TO THE BANKING SYSTEM ===
A Java-based banking system using MySQL and JDBC

✓ Database connection successful!
```

### Step 2: Test Basic Operations
1. **View Sample Data**: Choose option 7 to view all customers
2. **Check Balance**: Choose option 5 and enter account number "ACC001"
3. **View Transactions**: Choose option 6 and enter account number "ACC001"

### Step 3: Create New Account
1. Choose option 1 from the main menu
2. Enter customer details
3. Select account type
4. Enter initial balance
5. Verify the account was created

### Step 4: Test Transactions
1. **Deposit**: Choose option 2, enter account number and amount
2. **Withdraw**: Choose option 3, enter account number and amount
3. **Transfer**: Choose option 4, enter source and destination accounts

## Troubleshooting

### Database Connection Issues
- **Error**: "Cannot connect to database"
  - Solution: Ensure MySQL service is running
  - Check username/password in DatabaseConnection.java
  - Verify database name is correct

### Compilation Issues
- **Error**: "Cannot find symbol"
  - Solution: Ensure all Java files are in correct directories
  - Check that MySQL JDBC driver is in lib directory

### Runtime Issues
- **Error**: "ClassNotFoundException"
  - Solution: Ensure classpath includes lib directory
  - Verify MySQL JDBC driver JAR is present

### Sample Data Issues
- **Error**: "Account not found"
  - Solution: Ensure database.sql script was executed completely
  - Check that sample data was inserted

## Database Schema Overview

### Tables
1. **customers**: Customer information (name, email, phone, etc.)
2. **accounts**: Bank accounts (account number, type, balance, etc.)
3. **transactions**: Transaction records (deposits, withdrawals, transfers)

### Sample Data
- **Customer**: John Doe (ID: 1001)
- **Account**: ACC001 (Savings, Balance: $5000)
- **Transactions**: Opening balance transactions for all accounts

## Features Available

1. **Account Management**
   - Create new customer accounts
   - View account details
   - Check account balances

2. **Transaction Operations**
   - Deposit money
   - Withdraw money
   - Transfer between accounts

3. **Reporting**
   - View transaction history
   - List all customers
   - List all accounts
   - Search customers by name

## Security Considerations

1. **Database Security**
   - Use strong passwords for MySQL
   - Limit database user permissions
   - Enable SSL connections in production

2. **Application Security**
   - Validate all user inputs
   - Use prepared statements (already implemented)
   - Implement proper error handling

## Next Steps

1. **Enhancements**
   - Add user authentication
   - Implement web interface
   - Add interest calculation
   - Create loan management system

2. **Production Deployment**
   - Use connection pooling
   - Implement logging
   - Add monitoring and alerts
   - Set up backup procedures

## Support

If you encounter issues:
1. Check the troubleshooting section above
2. Verify all prerequisites are installed
3. Ensure database setup is correct
4. Review error messages carefully

For additional help, refer to:
- MySQL Documentation: https://dev.mysql.com/doc/
- Java Documentation: https://docs.oracle.com/javase/
- JDBC Documentation: https://docs.oracle.com/javase/tutorial/jdbc/ 