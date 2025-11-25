-- DDL Script: create_schema.sql
DROP TABLE IF EXISTS TRANSACTION_RECORD;
DROP TABLE IF EXISTS ACCOUNT;
DROP TABLE IF EXISTS LOAN;
DROP TABLE IF EXISTS CUSTOMER;

-- CUSTOMER Table
CREATE TABLE CUSTOMER (
    customer_id INT PRIMARY KEY AUTO_INCREMENT, -- Primary Key, auto-generated
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE, -- unique
    phone_number VARCHAR(15) NOT NULL,
    dob DATE NOT NULL,
    address VARCHAR(255) NOT NULL
);

-- ACCOUNT Table (Child of CUSTOMER)
CREATE TABLE ACCOUNT (
    account_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL, -- Foreign Key to link to the owner
    account_type ENUM('Checking', 'Savings') NOT NULL,
    balance DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    open_date DATE NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES CUSTOMER(customer_id)
);

-- LOAN Table (Child of CUSTOMER)
CREATE TABLE LOAN (
    loan_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL, -- Foreign Key to link to the customer
    loan_type VARCHAR(50) NOT NULL,
    loan_amount DECIMAL(10, 2) NOT NULL,
    interest_rate DECIMAL(4, 3) NOT NULL,
    status ENUM('Pending', 'Approved', 'Rejected', 'Paid') NOT NULL,
    start_date DATE NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES CUSTOMER(customer_id)
);

-- TRANSACTION Table (Child Table, links to ACCOUNT twice)
CREATE TABLE TRANSACTION_RECORD (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    source_account_id INT NOT NULL,       -- FK: source of money
    destination_account_id INT NOT NULL,  -- FK: destination of funds
    amount DECIMAL(10, 2) NOT NULL,
    transaction_type ENUM('Deposit', 'Withdrawal', 'Transfer', 'Fee') NOT NULL,
    transaction_date DATETIME NOT NULL,
    FOREIGN KEY (source_account_id) REFERENCES ACCOUNT(account_id),
    FOREIGN KEY (destination_account_id) REFERENCES ACCOUNT(account_id)
);