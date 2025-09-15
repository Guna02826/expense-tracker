# Expense Tracker Backend

This is the backend service for the Expense Tracker application. It is built with Spring Boot and packaged as a Docker container for easy deployment.

## Features

- User authentication and registration
- CRUD operations for expenses and categories
- RESTful API endpoints
- JWT-based security
- Dockerized for portability

## Prerequisites

- Java 21+
- Maven 3.9+
- Docker (optional, for containerization)
- PostgreSQL (or your preferred database)

## Getting Started

### 1. Clone the Repository

```sh
git clone https://github.com/yourusername/expense-tracker-backend.git
cd expense-tracker-backend
```

### 2. Configure Environment

Set your database connection details in `src/main/resources/application.properties` or use environment variables.

### 3. Build and Run Locally

```sh
mvn clean package
java -jar target/expense-tracker-backend-0.0.1-SNAPSHOT.jar
```

The server will start on [http://localhost:8080](http://localhost:8080).

### 4. Run with Docker

Build and run the Docker container:

```sh
docker build -t expense-tracker-backend .
docker run -p 8080:8080 expense-tracker-backend
```

## API Endpoints

- `POST /api/auth/register` — Register a new user
- `POST /api/auth/login` — Authenticate and receive a JWT
- `GET /api/expenses` — List expenses (requires authentication)
- `POST /api/expenses` — Add a new expense
- `PUT /api/expenses/{id}` — Update an expense
- `DELETE /api/expenses/{id}` — Delete an expense

## Project Structure

```
expense-tracker-backend/
├── src/
├── pom.xml
├── Dockerfile
└── README.md
```

## License

This project is for educational purposes.
