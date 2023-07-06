# Inventory Management System

This is an Inventory Management System that allows you to manage inventory items in various warehouses. It provides functionalities to add, update, and delete inventory items, as well as retrieve inventory information based on warehouse ID.

## Author: Trafton Reynolds

## Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Maven

## Setup Instructions

1. Clone the repository to your local machine.

- git clone https://github.com/traftonrey/Inventory-Management-Project.git

2. Create a PostgreSQL database for the project.

- CREATE DATABASE inventory_management_system;

3. Add tables to the PostgreSQL database.

- CREATE TABLE warehouses (
  warehouse_id SERIAL PRIMARY KEY,
  warehouse_name VARCHAR(255),
  maximum_capacity INT
);

- CREATE TABLE books (
  book_id SERIAL PRIMARY KEY,
  book_title VARCHAR(255),
  author VARCHAR(255),
  publication_date DATE,
  ISBN VARCHAR(255)
);

- CREATE TABLE inventory (
  warehouse_id INT REFERENCES Warehouses(warehouse_id),
  book_id INT REFERENCES Books(book_id),
  quantity INT,
  PRIMARY KEY (warehouse_id, book_id)
);


4. Open the project in your preferred IDE (e.g., IntelliJ, Eclipse).

5. Update the database configuration in the `application.properties` file located in the `src/main/resources` directory. Replace the placeholders with your MySQL database credentials.

- spring.datasource.url=jdbc:postgresql://localhost:5432/inventory_management_system
- spring.datasource.username=__your-username__
- spring.datasource.password=__your-password__


6. Build the project using Maven.

- mvn clean install


7. Run the application.

- mvn spring-boot:run

8. The application will start running on `http://localhost:8080`.

