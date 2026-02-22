# YourLight - Backend API

Backend REST API for a digital library platform built with Spring Boot.

---

## 🚀 Overview

YourLight is a secure and scalable backend application designed to manage users and books in a digital library environment.

The project implements JWT authentication (access & refresh tokens) and follows a layered architecture pattern.

---

## 🏗 Architecture

Layered architecture:

- Controller → Handles HTTP requests
- Service → Business logic
- Repository → Database access
- Security → JWT authentication & authorization
- DTO → Data transfer objects

---

## 🔐 Authentication

- JWT Access Token
- JWT Refresh Token
- Stateless authentication
- Protected endpoints
- Role-based structure ready

---

## 🛠 Technologies

- Java 17
- Spring Boot 3
- Spring Security
- Spring Data JPA
- MySQL
- JWT (Auth0)
- Swagger / OpenAPI
- Maven
- JUnit & Mockito (Unit Testing)

---

## 📦 Features

- User registration
- User login
- Refresh token
- CRUD operations for books
- Favorite books
- Pagination & sorting
- Global exception handling
- Unit tests
- API documentation via Swagger

---

## 📄 API Documentation

After running the application:
http://localhost:8081/swagger-ui/index.html


---

## ⚙️ Running the Project

1. Clone repository

2. Configure database in:
src/main/resources/application.properties


3. Run:


Or run the main class from your IDE.

---

## 🧪 Running Tests
mvnw.cmd test


---

## Author

Kelton Rodrigues

Backend Developer | Java & Spring Boot
