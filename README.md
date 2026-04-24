# LMS College Portal

A complete College Management System built with Spring Boot, MySQL, and JWT Authentication.

## Tech Stack

- **Backend**: Java 17 + Spring Boot 3.5.13
- **Database**: MySQL 8.0
- **Security**: Spring Security + JWT
- **Documentation**: Swagger/OpenAPI 3.0
- **Build Tool**: Maven

## Project Structure

```
src/main/java/com/landminesoft/lms/
├── config/         → JWT, Security, Swagger config
├── controller/     → REST API controllers
├── dto/            → Data Transfer Objects
├── entity/         → Database entities
├── exception/      → Custom exception classes
├── repository/     → JPA repositories
└── service/        → Business logic
```

## Prerequisites

Make sure you have these installed before running the project:

- Java 17 (Temurin)
- MySQL 8.0
- Maven 3.9+
- Git

## Local Setup Instructions

### Step 1 — Clone the repository

```bash
git clone https://github.com/prudeep/college_management_portal-.git
cd college_management_portal-
```

### Step 2 — Create MySQL database

Open MySQL Workbench and run:

```sql
CREATE DATABASE lms_db;
```

### Step 3 — Set environment variables

Create a `run.ps1` file in project root with your actual values:

```powershell
$env:DB_PASSWORD="your_mysql_password"
$env:MAIL_USERNAME="yourgmail@gmail.com"
$env:MAIL_PASSWORD="your_16_char_app_password"
$env:JWT_SECRET="LandmineSoftLMSSecretKey2024SuperSecureKeyForJWTTokenGeneration"
./mvnw spring-boot:run
```

### Step 4 — Run the application

```bash
./run.ps1
```

Server starts at: `http://localhost:8080`

## API Documentation

Swagger UI is available at: `http://localhost:8080/swagger-ui/index.html`

### Authentication APIs

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | /api/auth/student/register | Register new student | No |
| POST | /api/auth/student/login | Student login — returns JWT | No |
| POST | /api/auth/faculty/register | Register new faculty | No |
| POST | /api/auth/faculty/login | Faculty login — returns JWT | No |
| POST | /api/auth/admin/register | Register new admin | No |
| POST | /api/auth/admin/login | Admin login — returns JWT | No |
| POST | /api/auth/forgot-password | Send password reset email | No |
| POST | /api/auth/reset-password | Reset password using token | No |
| POST | /api/auth/change-password | Change current password | JWT |

### Student APIs

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | /api/student/profile | Get student profile | STUDENT role |
| PUT | /api/student/profile | Update student profile | STUDENT role |
| GET | /api/student/enrollments | Get student enrollments | STUDENT role |

### Faculty APIs

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | /api/faculty/students | Get all students | FACULTY role |

### Admin APIs

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | /api/admin/students | Get all students | ADMIN role |
| GET | /api/admin/faculty | Get all faculty | ADMIN role |
| GET | /api/admin/reports | Get system reports | ADMIN role |

## Environment Variables

| Variable | Description |
|----------|-------------|
| DB_PASSWORD | MySQL root password |
| MAIL_USERNAME | Gmail address for sending emails |
| MAIL_PASSWORD | Gmail app password (16 characters) |
| JWT_SECRET | Secret key for JWT token generation |

## Running Tests

```bash
./mvnw test
```

Expected output:
```
Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Database Tables

11 tables created automatically by Hibernate on startup:

| Table | Description |
|-------|-------------|
| students | Student accounts and profile data |
| faculty_personal | Faculty accounts and details |
| admins | Admin accounts |
| subjects | Subject master data |
| courses | Faculty-subject assignments |
| enrollments | Student course enrollments |
| attendance | Class attendance records |
| marks | Student marks and grades |
| fee_structure | Fee details per branch and semester |
| fee_payments | Student fee payment records |
| announcements | College announcements |
| password_reset_tokens | Temporary tokens for password reset |

## Security Features

- BCrypt password hashing for all user passwords
- JWT token authentication with 24 hour expiry
- Role-based access control — STUDENT, FACULTY, ADMIN roles
- Custom exception handling with proper HTTP status codes — 401, 403, 409
- Sensitive config via environment variables — never hardcoded

## Error Responses

| HTTP Code | Error | When |
|-----------|-------|------|
| 401 | Unauthorized | No token or invalid token |
| 401 | Invalid credentials | Wrong email or password |
| 403 | Forbidden | Correct token but wrong role |
| 409 | Conflict | Email already registered |
| 400 | Validation failed | Invalid request body |

## Weekly Progress

| Week | Task | Status |
|------|------|--------|
| Week 1 | Project setup, all 11 entities, repositories, DTOs | Done |
| Week 2 | Student, Faculty, Admin registration and login APIs | Done |
| Week 3 | JWT implementation, security filter, RBAC | Done |
| Week 4 | Password reset, profile management, email integration | Done |
| Week 5 | Custom exceptions, error handling, protected endpoints | Done |
| Week 6 | Swagger docs, unit tests, Postman scripts, deployment | Done |

## Postman Collection

Import the collection from the postman folder:

```
postman/LMS-Final-Collection.postman_collection.json
```

After importing, create an environment called LMS Local with variable `token` and set it to the JWT value from any login response.

## Deployment

Backend is deployed on Render.
