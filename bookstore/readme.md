# JAVA Bookstore online inventory system

Thank you for taking your time to review my assignment.

## Step 1: Setup Instruction
1. Access the Repository: Clone or download the repository containing the Spring Boot application for the online book inventory system.

2. Install Dependencies: Make sure you have Java, Maven, and MySQL installed on your system.

3. Database Configuration: Set up a MySQL database (8.1.0) and update the application's application.properties file with the appropriate database connection details.

- To set up the database schema on local, you could run the following SQL code on TablePlus or MySQLWorkbench.

```
CREATE DATABASE bookstore;
USE bookstore;
```

- By default, local MySQL server endpoint is "localhost:3306". To change the database configuration such as url, username and password, you could edit the ./src/main/resources/application.properties accordingly.
```
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/bookstore
spring.datasource.username=root
spring.datasource.password=password
```

## Step 2: Explore the function APIs
It's recommended to use Postman to test the function APIs. You could import the collections to Postman to test the APIs. By default, the BookstoreApplication endpoint is "localhost:8080".

The sample query for every function has been packed into bookstore.zip in the bookstore/ directory. You could import this collection into Postman to simplify the test.

The following functions have been done and optimized to some degree.

1. Add a book to the inventory.
2. Remove a book from the inventory.
3. Update the quantity in stock for a given book.
4. Retrieve the quantity in stock for a given book.
5. List all books in the inventory.
6. Search and Filter Functionality
7. Authentication and authorisation
   - implemented a simple token based authentication mechanism
8. Error Handling
9.  Performance Optimization
   - Pagination of response for **list all books info** (/listAll) and **seach** (/search)  
   - Cache for **get book quantity** and **update book quantity**

## Step 3: Logic flow of the inventory system
1. user need to first register a username and password
2. user need to login the system with user username and password, the system will return a uuid token. User would need this token to perform add book, remove book, and update book quantity operations. Other functions does not require this token.
3. For authorised functions (add book, remove book, and update book quantity), user need to pass the token via request parameters
4. user logout the system. The system will remove the token from its memory.