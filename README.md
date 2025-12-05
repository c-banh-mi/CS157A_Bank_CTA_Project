# CS157A_Bank_CTA_Project
Bank CTA: Client Transaction Administration

An Online Banking Database System built with Java, JDBC, and MySQL.

---

Project Overview

Bank CTA is a web-based banking simulation designed to manage core financial operations. It serves as an administrative dashboard for bank staff to oversee client data, accounts, loans, and transactions.

This project demonstrates the practical application of:
* Relational Database Management: designing a BCNF-normalized schema
* ACID Transactions: ensuring data integrity during fund transfers
* MVC Architecture: separating concerns using Servlets, JSPs, and DAOs
* Modern Java Development: integrating the Jakarta EE namespace on Apache Tomcat 11

---

Features

Core Functionality
* Customer Management: register new clients with comprehensive financial profiles (income, credit score, SSN)
* Account Management: create Checking/Savings accounts and view real-time balances
* Loan Administration: review loan applications and approve/reject them based on financial metrics
* Fund Transfers: execute secure, atomic transfers between accounts with instant balance updates

Technical Highlights
* CRUD Operations: full Create, Read, Update, Delete capabilities across all entities
* Soft Delete: deactivate accounts instead of deleting them to preserve transaction history and referential integrity
* Search & Sort: dynamic filtering and sorting of records on the frontend
* Security: protection against SQL Injection using "PreparedStatement"

---

Technology Stack

* Backend: Java (JDK 21+), Jakarta EE Servlet API 6.0
* Database: MySQL 8.0
* Server: Apache Tomcat 11.0.14
* Frontend: JSP, JSTL, Bootstrap 5
* Build/Version Control: Git, VS Code

---

Setup & Installation

Prerequisites
Ensure you have the following installed:
1.  Java Development Kit (JDK) 21 or higher
2.  Apache Tomcat 11 (core zip)
3.  MySQL Server and MySQL Workbench
4.  Git

1. Clone the Repository
```bash
git clone https://github.com/c-banh-mi/CS157A_Bank_CTA_Project.git
cd CS157A_Bank_CTA_Project

2. Database Setup
* Open MySQL Workbench and ensure your local server is running on port 3306.
* The project includes a utility to set up the schema automatically.
* Edit src/DBConnector.java to update your MySQL username and password:
```Java
private static final String USER = "root";
private static final String PASSWORD = "YOUR_PASSWORD";
* Run the setup utility:
```bash
# Compile and run the connector
javac -cp "lib/*;src;." src/DBConnector.java
java -cp "lib/*;src;." DBConnector
# This will create the BankCTA_DB database and populate it with sample data

3. Compile the Application
* You need to compile the source code using the Tomcat libraries. Update the path to your local Tomcat installation.
```bash
del src\*.class
javac -cp "lib/*;C:\Tomcat\apache-tomcat-11.0.14\lib\*;webapp/WEB-INF/lib/*;." src/*.java

4. Build the WAR file
* Package the application for deployment
```bash
# clean out "build" directory
rmdir /s /q build
mkdir build
mkdir build\WEB-INF
mkdir build\WEB-INF\classes
mkdir build\WEB-INF\lib

# copy assets
copy src\*.class build\WEB-INF\classes\
copy webapp\*.jsp build\
copy webapp\WEB-INF\web.xml build\WEB-INF\

copy lib\mysql-connector-j-9.5.0.jar build\WEB-INF\lib\
copy lib\jakarta.servlet.jsp.jstl-3.0.1.jar build\WEB-INF\lib\
copy lib\jakarta.servlet.jsp.jstl-api-3.1.0-M1.jar build\WEB-INF\lib\

# create WAR
jar -cvf bankcta.war -C build/ .

5. Deploy & Run
* Delete any old instance of bankcta.war and bankcta folder in the webapps folder
* Delete if exists a bankcta folder in localhost, i.e. "C:\Tomcat\apache-tomcat-11.0.14\work\Catalina\localhost\bankcta"
* Make sure jakarta.servlet-api-6.2.0-M1.jar exists in the lib folder, i.e. "C:\Tomcat\apache-tomcat-11.0.14\lib\jakarta.servlet-api-6.2.0-M1.jar"
* Copy bankcta.war to your Tomcat webapps folder
* Start Tomcat via (bin/startup.bat)
* Access the application at: http://localhost:8080/bankcta/

Project Structure
CS157A_Bank_CTA_Project/
├── src/                    # Java Source Code (Servlets, DAOs, Services)
├── webapp/                 # Frontend (JSP, CSS, web.xml)
├── db/                     # SQL Scripts (Schema, Data)
├── lib/                    # Project Dependencies (MySQL Connector, JSTL)
└── README.md               # Project Documentation

Note
* Passwords: Be careful not to put your actual database password in the README if this repo is public. "YOUR_PASSWORD" is used as a placeholder.
* Paths are used to remind users to adjust the Tomcat path to match their own computer.

This README is clean and informative on how to run this code.

Contributors
Charlie Banh
Thanh Trung Nguyen
Andrew Gong