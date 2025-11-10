# Project and Demand Management System

This is a RESTful API for managing projects and tasks, developed as a challenge for `dev.matheuslf`. The system allows a development team to organize deliveries, track task status, and perform simple analyses.

## Technologies Used

*   **Java 21**
*   **Spring Boot 3.5.7**
*   **Spring Data JPA**
*   **Spring Security (with JWT)**
*   **PostgreSQL**
*   **Flyway** (for database migrations)
*   **Maven**
*   **Lombok**
*   **MapStruct**

## How to Run the Project

### Prerequisites

*   Java 21 or higher (if running directly without Docker)
*   Maven 3.8 or higher (if running directly without Docker)
*   Docker and Docker Compose (recommended)

### Running with Docker Compose (Recommended)

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd demoProjectAndDemandManagementSystem
    ```

2.  **Create a `.env` file:**
    Create a file named `.env` in the root directory of the project (where `docker-compose.yml` is located). This file will store your sensitive environment variables.

    ```
    DB_PASSWORD=your_database_password
    JWT_SECRET=your_jwt_secret_key
    ```
    Replace `your_database_password` and `your_jwt_secret_key` with strong, unique values.

3.  **Build and run the services:**
    ```bash
    docker-compose up --build
    ```
    This command will build the Docker images (if not already built), create the containers, and start the application and database services.

    The application will be available at `http://localhost:8080`.

### Running the Application Directly (without Docker)

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd demoProjectAndDemandManagementSystem
    ```

2.  **Create the database:**
    Ensure you have a PostgreSQL database running locally (e.g., on `localhost:5432`) with a database named `psm`.

3.  **Set environment variables:**
    Before running the application, set the `DB_PASSWORD` and `JWT_SECRET` environment variables in your shell:
    ```bash
    export DB_PASSWORD="your_database_password"
    export JWT_SECRET="your_jwt_secret_key"
    ```
    Replace `your_database_password` and `your_jwt_secret_key` with the actual values.

4.  **Run the application:**
    ```bash
    ./mvnw spring-boot:run
    ```
    The application will be available at `http://localhost:8080`.

## API Endpoints

The main endpoints of the application are:

### API Documentation (Swagger UI)

Once the application is running, you can access the API documentation via Swagger UI at:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Authentication
*   `POST /auth/register`: Register a new user.
*   `POST /auth/login`: Authenticate a user and get a JWT token.

### Projects
*   `POST /projects`: Create a new project.
*   `GET /projects`: List all projects.

### Tasks
*   `POST /tasks`: Create a new task.
*   `GET /tasks`: List tasks with optional filters (`status`, `priority`, `projectId`).
*   `PUT /tasks/{id}/status`: Update the status of a task.
*   `DELETE /tasks/{id}`: Delete a task.
