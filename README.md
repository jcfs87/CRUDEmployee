# CRUDEmployee

This project is a Java application that performs CRUD (Create, Read, Update, Delete) operations on an employee database. Here's a summary of what each component does:

DAOEmployee: This is a class that contains methods to interact with the database and perform operations related to employees, such as getting all employees, adding an employee, updating an employee, deleting an employee, and getting an employee by their identifier.

ConnectionDB: This is a utility class that handles the database connection using JDBC (Java Database Connectivity). It provides methods to obtain a connection to the database and close it.

MYSQLConnection: Contains the connection information for the MySQL database, such as the database URL, username, and password.

Employee: This is a class that represents an employee with attributes such as id, first name, last name, date of birth, and salary. It also has methods to access and modify these attributes.

CRUDEmployee: This is the main class that contains the main method and handles user interaction through a menu. It allows the user to perform Create, Read, Update, and Delete operations on employees.

In summary, this project establishes a connection to a MySQL database and provides functionalities to interact with employee records, such as adding, reading, updating, and deleting employees through a simple console-based user interface.
