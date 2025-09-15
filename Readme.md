# Expense Tracker Application

A full-stack Expense Tracker application with a React frontend and a Spring Boot backend. Easily track your expenses, manage categories, and view insightful reports. The project is fully containerized with Docker for seamless deployment.

---

## Table of Contents

- [Features](#features)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Backend Setup](#backend-setup)
  - [Frontend Setup](#frontend-setup)
  - [Running with Docker Compose](#running-with-docker-compose)
- [API Overview](#api-overview)
- [License](#license)

---

## Features

- User registration and authentication (JWT-secured)
- Add, edit, and delete expenses
- Categorize transactions
- Dashboard with summary and reports
- Responsive React UI
- RESTful API
- Dockerized for easy deployment

---

## Project Structure

```
expense-tracker/
├── expense-tracker-backend/   # Spring Boot backend
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── ...
├── expense-tracker-frontend/  # React frontend (Vite)
│   ├── src/
│   ├── package.json
│   ├── Dockerfile (optional)
│   └── ...
└── docker-compose.yml         # Multi-service orchestration
```

---

## Getting Started

### Backend Setup

1. **Navigate to the backend folder:**
   ```sh
   cd expense-tracker-backend
   ```

2. **Configure environment variables:**
   - Edit `.env` or `src/main/resources/application.properties` for database credentials.

3. **Build and run locally:**
   ```sh
   mvn clean package
   java -jar target/expense-tracker-backend-0.0.1-SNAPSHOT.jar
   ```
   - The backend runs at [http://localhost:8080](http://localhost:8080)

### Frontend Setup

1. **Navigate to the frontend folder:**
   ```sh
   cd expense-tracker-frontend
   ```

2. **Install dependencies:**
   ```sh
   npm install
   ```

3. **Run the development server:**
   ```sh
   npm run dev
   ```
   - The frontend runs at [http://localhost:5173](http://localhost:5173) by default.

### Running with Docker Compose

1. **From the project root (where `docker-compose.yml` is located):**
   ```sh
   docker-compose up --build
   ```
   - This will start both backend and frontend containers.
   - Access the app at [http://localhost:5173](http://localhost:5173)

---

## API Overview

**Authentication**
- `POST /api/auth/register` — Register a new user
- `POST /api/auth/login` — Login and receive JWT

**Transactions**
- `GET /api/transactions` — List user transactions
- `POST /api/transactions` — Add a transaction
- `PUT /api/transactions/{id}` — Update a transaction
- `DELETE /api/transactions/{id}` — Delete a transaction

**Reports**
- `GET /api/report/summary` — Get summary of income/expenses
- `GET /api/report/by-category` — Get totals by category

> All `/api/*` endpoints (except `/api/auth/*` and `/health`) require a valid JWT in the `Authorization: Bearer <token>` header.

---

## License

This project is for educational and demonstration purposes.