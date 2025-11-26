-- Sample customers
INSERT INTO CUSTOMER (first_name, last_name, email, phone_number, dob, address)
VALUES
('Thanh', 'Nguyen', 'thanh.nguyen@sjsu.edu', '408-555-1111', '1999-12-22', '123 Market St, San Jose, CA'),
('Charlie', 'Banh', 'charlie.banh@sjsu.edu', '408-555-2222', '1999-03-20', '478 King Rd, San Jose, CA'),
('Andrew', 'Gong', 'andrew.gong@sjsu.edu', '408-555-3333', '1998-09-14', '62 Blossom Hill Rd, San Jose, CA'),
('Emily', 'Tran', 'emily.tran@example.com', '408-555-4444', '2001-01-05', '88 Santa Clara St, San Jose, CA'),
('Jason', 'Kim', 'jason.kim@example.com', '408-555-5555', '2000-04-12', '501 Berryessa Rd, San Jose, CA'),
('Sophia', 'Ng', 'sophia.ng@example.com', '408-555-6666', '1997-08-19', '442 Monterey Rd, San Jose, CA'),
('Daniel', 'Wong', 'daniel.wong@example.com', '408-555-7777', '1999-11-11', '29 Tully Rd, San Jose, CA'),
('Lily', 'Pham', 'lily.pham@example.com', '408-555-8888', '2002-02-21', '902 Silver Creek Rd, San Jose, CA'),
('Kevin', 'Vo', 'kevin.vo@example.com', '408-555-9999', '1998-07-09', '15 Curtner Ave, San Jose, CA'),
('Ava', 'Le', 'ava.le@example.com', '408-555-1212', '2001-12-31', '1020 Story Rd, San Jose, CA'),
('Marcus', 'Chen', 'marcus.chen@example.com', '408-555-3434', '1996-03-28', '21 Brokaw Rd, San Jose, CA'),
('Hannah', 'Do', 'hannah.do@example.com', '408-555-5656', '1997-05-14', '499 Moorpark Ave, San Jose, CA'),
('Ryan', 'Ly', 'ryan.ly@example.com', '408-555-7878', '2000-09-23', '650 Meridian Ave, San Jose, CA'),
('Olivia', 'Lam', 'olivia.lam@example.com', '408-555-9090', '1999-01-30', '300 King St, San Jose, CA'),
('Derek', 'Huynh', 'derek.huynh@example.com', '408-555-4545', '1998-10-17', '222 Park Ave, San Jose, CA');

-- Sample accounts
INSERT INTO ACCOUNT (customer_id, account_type, balance, open_date)
VALUES
(1, 'Checking', 1500.00, '2025-01-01'),
(1, 'Savings', 5000.00, '2025-01-10'),
(2, 'Checking', 2200.00, '2025-01-15'),
(3, 'Savings', 3200.00, '2025-01-20'),
(4, 'Checking', 800.00, '2025-02-01'),
(5, 'Savings', 12000.00, '2025-02-05'),
(6, 'Checking', 950.00, '2025-02-08'),
(7, 'Checking', 4300.00, '2025-02-12'),
(8, 'Savings', 7600.00, '2025-02-14'),
(9, 'Checking', 1250.00, '2025-02-16'),
(10, 'Savings', 3100.00, '2025-02-20'),
(11, 'Checking', 2400.00, '2025-02-22'),
(12, 'Savings', 7000.00, '2025-02-25'),
(13, 'Checking', 1800.00, '2025-02-27'),
(14, 'Savings', 4200.00, '2025-03-01');

-- Sample loans
INSERT INTO LOAN (customer_id, loan_type, loan_amount, interest_rate, status, start_date)
VALUES
(1, 'Auto Loan', 15000.00, 4.500, 'Approved', '2025-02-01'),
(2, 'Personal Loan', 5000.00, 6.900, 'Pending', '2025-02-10'),
(3, 'Home Loan', 250000.00, 3.200, 'Rejected', '2025-02-15'),
(4, 'Personal Loan', 7000.00, 5.500, 'Approved', '2025-03-01'),
(5, 'Auto Loan', 18000.00, 4.200, 'Pending', '2025-03-03'),
(6, 'Home Loan', 300000.00, 3.100, 'Approved', '2025-03-05'),
(7, 'Auto Loan', 22000.00, 4.700, 'Rejected', '2025-03-08'),
(8, 'Personal Loan', 4500.00, 6.300, 'Approved', '2025-03-10'),
(9, 'Home Loan', 275000.00, 3.300, 'Pending', '2025-03-14'),
(10, 'Auto Loan', 16000.00, 4.800, 'Approved', '2025-03-16'),
(11, 'Personal Loan', 5200.00, 6.100, 'Rejected', '2025-03-18'),
(12, 'Home Loan', 310000.00, 3.250, 'Approved', '2025-03-20'),
(13, 'Auto Loan', 20000.00, 4.450, 'Approved', '2025-03-21'),
(14, 'Personal Loan', 6000.00, 5.750, 'Pending', '2025-03-22'),
(15, 'Home Loan', 285000.00, 3.180, 'Approved', '2025-03-23');


-- Sample transactions
INSERT INTO TRANSACTION_RECORD (source_account_id, destination_account_id, amount, transaction_type, transaction_date)
VALUES
(1, 3, 200.00, 'Transfer', '2025-03-01 10:30:00'),
(2, 1, 100.00, 'Transfer', '2025-03-02 14:45:00'),
(1, 1, 500.00, 'Deposit', '2025-03-03 09:15:00'),
(3, 3, 75.00, 'Withdrawal', '2025-03-04 16:10:00'),
(4, 5, 300.00, 'Transfer', '2025-03-05 11:00:00'),
(6, 4, 125.50, 'Transfer', '2025-03-06 13:22:00'),
(7, 7, 800.00, 'Deposit', '2025-03-07 08:55:00'),
(8, 9, 210.00, 'Transfer', '2025-03-08 09:45:00'),
(10, 8, 90.00, 'Fee', '2025-03-09 14:10:00'),
(11, 12, 350.00, 'Transfer', '2025-03-10 15:30:00'),
(12, 12, 1200.00, 'Deposit', '2025-03-11 12:00:00'),
(13, 11, 250.00, 'Transfer', '2025-03-12 17:15:00'),
(14, 15, 400.00, 'Transfer', '2025-03-13 11:40:00'),
(15, 13, 180.00, 'Transfer', '2025-03-14 10:20:00'),
(9, 14, 60.00, 'Fee', '2025-03-15 09:55:00');