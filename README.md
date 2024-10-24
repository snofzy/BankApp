# Bank Management Application

## Overview
This Java-based Bank Management System provides a graphical user interface (GUI) that allows users to interact with various bank account operations. The application uses Java's Swing library to create the GUI, and it manages different types of bank accounts, including savings accounts and credit accounts, along with customer information.

The application is designed to simulate basic banking functionality such as account creation, deposit, withdrawal, and credit management, all accessible through a user-friendly interface.

## Features
- **GUI with Swing**: The application provides an intuitive graphical interface built using Java's Swing framework.
- **Multiple Account Types**: Supports both savings accounts and credit accounts.
- **Account Management**: Allows users to create accounts, deposit and withdraw money, and manage credits.
- **Customer Management**: Keeps track of customer information and links accounts to specific customers.
- **Data Persistence**: Account and customer data can be saved and loaded using file-based persistence.

## Class Overview
### `Account.java`
This class represents a general bank account. It contains common attributes such as balance, account number, and methods for depositing and withdrawing money.

### `SavingsAccount.java`
A subclass of `Account`, this class implements the functionality specific to savings accounts, including rules for withdrawal limits and interest calculation.

### `CreditAccount.java`
This class extends `Account` and adds features specific to credit accounts, including the handling of credit limits and the calculation of interest on borrowed amounts.

### `Customer.java`
The `Customer` class stores personal details of a bank customer, including name and unique ID. Each customer can have multiple accounts.

### `BankLogic.java`
This class handles the business logic of the application, coordinating the interaction between customers and accounts. It manages the creation of accounts, performing transactions, and keeping track of customer data.

### `BankGUI.java`
This class builds the graphical user interface (GUI) for the application using Swing. It provides a set of windows and forms that allow the user to perform operations like account management, transaction handling, and viewing customer information.

### `SaveClass.java`
A utility class used for saving and loading customer and account data to and from files. It ensures data persistence between sessions.

## `Future Improvements`
- **Database Integration:** Replace file-based persistence with a database for more robust data handling.
- **Enhanced Error Handling:** Improve input validation and error messaging.
- **Additional Features**: Add features like transaction history, account statements, and more account types such as business accounts.

## `Requirements`
Java 8 or later
Swing (part of standard Java)
