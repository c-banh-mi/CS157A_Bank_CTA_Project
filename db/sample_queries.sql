-- Show all customers
SELECT * FROM CUSTOMER;

-- List all accounts for Customer #1
SELECT *
FROM ACCOUNT
WHERE customer_id = 1;

-- Show full transaction history for Account #1
SELECT *
FROM TRANSACTION_RECORD
WHERE source_account_id = 1
   OR destination_account_id = 1
ORDER BY transaction_date DESC;

-- Display all loans with customer names
SELECT 
    L.loan_id,
    C.first_name,
    C.last_name,
    L.loan_type,
    L.loan_amount,
    L.status
FROM LOAN L
JOIN CUSTOMER C ON L.customer_id = C.customer_id;

-- Count accounts per customer
SELECT 
    customer_id,
    COUNT(*) AS number_of_accounts
FROM ACCOUNT
GROUP BY customer_id;

-- Insert a new fund transfer
INSERT INTO TRANSACTION_RECORD (source_account_id, destination_account_id, amount, transaction_type, transaction_date)
VALUES (1, 2, 250.00, 'Deposit', NOW());

-- Total balance for each customer
SELECT 
    C.customer_id,
    C.first_name,
    C.last_name,
    SUM(A.balance) AS total_balance
FROM CUSTOMER C
JOIN ACCOUNT A ON C.customer_id = A.customer_id
GROUP BY C.customer_id;

-- Show all transactions above $200
SELECT *
FROM TRANSACTION_RECORD
WHERE amount > 200;

-- Show all pending loans
SELECT *
FROM LOAN
WHERE status = 'Pending';

-- Get the most recent 5 transactions
SELECT *
FROM TRANSACTION_RECORD
ORDER BY transaction_date DESC
LIMIT 5;