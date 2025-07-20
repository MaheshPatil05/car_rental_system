# Car Rental System 🚗

A console-based Car Rental System built from scratch using core Java concepts and PostgreSQL as the database. This project demonstrates fundamental principles of object-oriented programming, database interaction (JDBC), and a multi-layered application architecture (DAO, Service, UI).

## Table of Contents

* [Features](#features)
* [Technologies Used](#technologies-used)
* [System Design](#system-design)
* [Database Setup](#database-setup)
* [Getting Started](#getting-started)
* [Usage](#usage)
* [Future Enhancements](#future-enhancements)
* [Contributing](#contributing)
* [License](#license)

---

## Features ✨

* **User Authentication**:
    * **User Registration**: Create new user accounts.
    * **User Login**: Authenticate existing users.
    * **User Logout**: End the current user session.
* **Car Management**:
    * View a list of **available cars**.
    * Add new cars to the system (basic admin functionality).
* **Rental Operations**:
    * **Rent a Car**: Book an available car for a specified period.
    * **Return a Car**: Mark a rented car as returned, making it available again.
    * **View My Rentals**: See a list of current and past rentals for the logged-in user.
    * **Rental Cost Calculation**: Basic calculation based on car type and duration.
* **Data Persistence**: All user, car, and rental data is stored persistently in a PostgreSQL database.

---

## Technologies Used 💻

* **Core Java**: For the application logic.
* **JDBC (Java Database Connectivity)**: For interacting with the PostgreSQL database.
* **PostgreSQL**: Relational database management system for data storage.
* **Intellij (Recommended)**: For project management and dependency handling (especially for the PostgreSQL JDBC driver).

---

## System Design 🏗️

The project follows a layered architecture to ensure separation of concerns:

* **Model Layer**: Plain Old Java Objects (POJOs) representing the core entities of the system (`User`, `Car`, `Rental`).
* **Data Access Object (DAO) Layer**: Handles all database interactions for the models (`UserDAO`, `CarDAO`, `RentalDAO`). It abstracts SQL queries from the business logic.
* **Service Layer (Business Logic)**: Contains the core business rules and orchestrates calls to the DAOs. Examples include `AuthService`, `CarService`, `RentalService`.
* **Presentation Layer (Console UI)**: The `Main` class, which handles user input and displays output to the console.

---

## Database Setup 🗄️

This project uses **PostgreSQL**. Follow these steps to set up your database:

1.  **Install PostgreSQL**:
    * Download and install PostgreSQL from [https://www.postgresql.org/download/](https://www.postgresql.org/download/).
2.  **Create Database**:
    * Using `psql` or `pgAdmin`, create a new database named `car_rental_db`.
        ```sql
        CREATE DATABASE car_rental_db;
        ```
3.  **Create Tables**:
    * Connect to the `car_rental_db` database.
    * Execute the following SQL schema to create the necessary tables:

    ```sql
    -- Table: users
    CREATE TABLE users (
        id SERIAL PRIMARY KEY,
        username VARCHAR(50) UNIQUE NOT NULL,
        password_hash VARCHAR(255) NOT NULL, -- Store hashed passwords (e.g., BCrypt hash)
        name VARCHAR(100) NOT NULL,
        contact_number VARCHAR(20),
        email VARCHAR(100) UNIQUE,
        address TEXT,
        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
    );

    -- Table: cars
    CREATE TABLE cars (
        id SERIAL PRIMARY KEY,
        car_num VARCHAR(20) UNIQUE NOT NULL,       -- e.g., license plate
        car_type VARCHAR(50) NOT NULL,            -- e.g., 'SUV', 'Sedan', 'Mini'
        car_name VARCHAR(100) NOT NULL,           -- e.g., 'Toyota Camry', 'Honda CRV'
        status VARCHAR(20) NOT NULL DEFAULT 'available', -- 'available', 'rented', 'maintenance'
        daily_rate NUMERIC(10, 2) NOT NULL,       -- e.g., 75.00
        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
    );

    -- Table: rentals
    CREATE TABLE rentals (
        id SERIAL PRIMARY KEY,
        user_id INT NOT NULL,
        car_id INT NOT NULL,
        start_date DATE NOT NULL,
        end_date DATE NOT NULL,
        actual_return_date DATE,                  -- NULL until car is returned
        total_cost NUMERIC(10, 2),                -- NULL until calculated on return
        rental_status VARCHAR(20) NOT NULL DEFAULT 'active', -- 'active', 'completed', 'cancelled'
        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

        -- Foreign Key Constraints
        CONSTRAINT fk_user
            FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE,
        CONSTRAINT fk_car
            FOREIGN KEY (car_id)
            REFERENCES cars(id)
            ON DELETE RESTRICT
    );

    -- Optional: Add indexes for performance
    CREATE INDEX idx_users_username ON users (username);
    CREATE INDEX idx_cars_status ON cars (status);
    CREATE INDEX idx_rentals_user_id ON rentals (user_id);
    CREATE INDEX idx_rentals_car_id ON rentals (car_id);
    CREATE INDEX idx_rentals_status ON rentals (rental_status);
    ```
4.  **Insert Sample Data (Optional but Recommended)**:
    * You can insert some initial data for testing. Remember to use **hashed passwords** for the `users` table in a real scenario.
    ```sql
    INSERT INTO users (username, password_hash, name, contact_number, email, address) VALUES
    ('john.doe', 'your_hashed_password_for_john', 'John Doe', '9876543210', 'john.doe@example.com', '123 Main St'),
    ('jane.smith', 'your_hashed_password_for_jane', '9988776655', 'jane.smith@example.com', '456 Oak Ave');

    INSERT INTO cars (car_num, car_type, car_name, status, daily_rate) VALUES
    ('KA01AB1234', 'Sedan', 'Toyota Camry', 'available', 50.00),
    ('KA02CD5678', 'SUV', 'Honda CRV', 'available', 75.00),
    ('KA03EF9012', 'Mini', 'Maruti Alto', 'available', 30.00);

    -- Example rental (assuming user_id and car_id from above inserts)
    -- You might need to adjust user_id and car_id based on your actual data.
    -- SELECT id FROM users WHERE username = 'john.doe';
    -- SELECT id FROM cars WHERE car_num = 'KA01AB1234';
    -- Then use those IDs.
    -- INSERT INTO rentals (user_id, car_id, start_date, end_date, rental_status) VALUES
    -- (1, 1, '2025-07-20', '2025-07-25', 'active');
    ```
5.  **Configure `DatabaseManager.java`**:
    * Update the `JDBC_URL`, `DB_USER`, and `DB_PASSWORD` variables in your `DatabaseManager.java` file to match your PostgreSQL setup.

    ```java
    // Example from DatabaseManager.java
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/car_rental_db";
    private static final String DB_USER = "your_db_user"; // e.g., postgres
    private static final String DB_PASSWORD = "your_db_password"; // your password
    ```

---

## Getting Started 🚀

To get this project up and running on your local machine:

1.  **Clone the Repository**:
    ```bash
    git clone [https://github.com/YourUsername/CarRentalSystem.git](https://github.com/YourUsername/CarRentalSystem.git)
    cd CarRentalSystem
    ```
    (Replace `YourUsername` with your actual GitHub username)

2.  **Add PostgreSQL JDBC Driver**:
    * If using Maven, add this dependency to your `pom.xml`:
        ```xml
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.7.3</version> </dependency>
        ```
    * If not using Maven, download the `.jar` file from [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/download/) and add it to your project's classpath (e.g., in a `lib` folder and configure your IDE).

3.  **Compile the Project**:
    * If using Maven:
        ```bash
        mvn clean install
        ```
    * If using a standard Java setup (e.g., with VS Code or IntelliJ without Maven configuration yet): Compile your `.java` files.

---

## Usage ▶️

1.  **Run the `Main` class**: Execute the `main` method in `src/main/java/Main.java`.
2.  **Follow Console Prompts**: The application will present a menu.
    * **Login / Register**: Start by registering a new user or logging in with existing credentials.
    * **Main Menu**: Once logged in, you can view cars, rent cars, return cars, or manage your rentals.

---

## Future Enhancements 💡

* **Robust Password Hashing**: Implement a strong hashing algorithm (e.g., BCrypt) for user passwords.
* **User Roles**: Introduce different user roles (e.g., Admin, Customer) with specific permissions.
* **More Advanced Rental Logic**:
    * Late return penalties.
    * Early return refunds.
    * Cancellation policies.
* **Inventory Management**: More detailed car management (e.g., maintenance scheduling, car availability calendar).
* **Reporting**: Generate reports on rentals, revenue, car utilization.
* **GUI / Web Interface**: Transform the console application into a graphical desktop application (Swing/JavaFX) or a web application (Spring Boot, JSP/Servlets).
* **Unit Testing**: Write unit tests for DAO and Service layers.
* **Logging**: Integrate a proper logging framework (Log4j2, SLF4J/Logback).

---

Any suggestions are welcome!
---
