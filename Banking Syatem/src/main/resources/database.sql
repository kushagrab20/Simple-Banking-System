-- Banking System Database Schema
-- Run this script to create the database tables

-- Create database (uncomment if needed)
-- CREATE DATABASE IF NOT EXISTS banking_system;
-- USE banking_system;

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS customers;

-- Create customers table
CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15),
    address TEXT,
    date_of_birth DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create accounts table
CREATE TABLE accounts (
    account_id INT PRIMARY KEY AUTO_INCREMENT,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    customer_id INT NOT NULL,
    account_type ENUM('SAVINGS', 'CHECKING', 'FIXED_DEPOSIT') DEFAULT 'SAVINGS',
    balance DECIMAL(15,2) DEFAULT 0.00,
    pin VARCHAR(4) NOT NULL DEFAULT '1234',
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);

-- Create transactions table
CREATE TABLE transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    transaction_type ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER', 'OPENING_BALANCE') NOT NULL,
    from_account_id INT,
    to_account_id INT,
    amount DECIMAL(15,2) NOT NULL,
    description TEXT,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (from_account_id) REFERENCES accounts(account_id) ON DELETE SET NULL,
    FOREIGN KEY (to_account_id) REFERENCES accounts(account_id) ON DELETE SET NULL
);

-- Insert sample data
INSERT INTO customers (first_name, last_name, email, phone, address, date_of_birth) VALUES
('John', 'Doe', 'john.doe@email.com', '+1234567890', '123 Main St, City, State', '1990-05-15'),
('Jane', 'Smith', 'jane.smith@email.com', '+1234567891', '456 Oak Ave, City, State', '1985-08-22'),
('Mike', 'Johnson', 'mike.johnson@email.com', '+1234567892', '789 Pine Rd, City, State', '1992-03-10');

-- Insert sample accounts
INSERT INTO accounts (account_number, customer_id, account_type, balance) VALUES
('ACC001', 1, 'SAVINGS', 5000.00),
('ACC002', 1, 'CHECKING', 2500.00),
('ACC003', 2, 'SAVINGS', 7500.00),
('ACC004', 3, 'FIXED_DEPOSIT', 10000.00);

-- Insert sample transactions
INSERT INTO transactions (transaction_type, to_account_id, amount, description) VALUES
('OPENING_BALANCE', 1, 5000.00, 'Initial deposit for savings account'),
('OPENING_BALANCE', 2, 2500.00, 'Initial deposit for checking account'),
('OPENING_BALANCE', 3, 7500.00, 'Initial deposit for savings account'),
('OPENING_BALANCE', 4, 10000.00, 'Initial deposit for fixed deposit account');

-- Create indexes for better performance
CREATE INDEX idx_account_number ON accounts(account_number);
CREATE INDEX idx_customer_email ON customers(email);
CREATE INDEX idx_transaction_date ON transactions(transaction_date);
CREATE INDEX idx_transaction_type ON transactions(transaction_type);

-- Create a view for account summary
CREATE VIEW account_summary AS
SELECT 
    a.account_id,
    a.account_number,
    CONCAT(c.first_name, ' ', c.last_name) AS customer_name,
    c.email,
    a.account_type,
    a.balance,
    a.status,
    a.created_at
FROM accounts a
JOIN customers c ON a.customer_id = c.customer_id;

-- Create a view for transaction history
CREATE VIEW transaction_history AS
SELECT 
    t.transaction_id,
    t.transaction_type,
    t.amount,
    t.description,
    t.transaction_date,
    CONCAT(c1.first_name, ' ', c1.last_name) AS from_customer,
    CONCAT(c2.first_name, ' ', c2.last_name) AS to_customer,
    a1.account_number AS from_account,
    a2.account_number AS to_account
FROM transactions t
LEFT JOIN accounts a1 ON t.from_account_id = a1.account_id
LEFT JOIN accounts a2 ON t.to_account_id = a2.account_id
LEFT JOIN customers c1 ON a1.customer_id = c1.customer_id
LEFT JOIN customers c2 ON a2.customer_id = c2.customer_id
ORDER BY t.transaction_date DESC; 