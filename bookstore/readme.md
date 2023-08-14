# JAVA Bookstore online inventory system

## How to set up the database schema and connection details

This submission uses MySQL 8.1.0 for implementation, and tested on local MySQL server. To set up the database schema on local, you could run the following SQL code on TablePlus or MySQLWorkbench.

```
CREATE DATABASE bookstore;
USE bookstore;
```

By default, local MySQL server endpoint is "localhost:3306". To change the database configuration such as url, username and password, you could edit the ./src/main/resources/application.properties accordingly.
```
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/bookstore
spring.datasource.username=root
spring.datasource.password=password
```

## How to run/test the function APIs
It's recommended to use Postman to test the function APIs. You could import the collections to Postman to test the APIs. By default, the BookstoreApplication endpoint is "localhost:8080".