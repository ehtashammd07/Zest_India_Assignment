# Zest India — Products API

A production-oriented RESTful Product Management API built with **Java 17 and Spring Boot 3** for the **Zest India Java Backend Developer assignment**.

The application provides Product and Item management with **JWT authentication, refresh-token rotation, role-based authorization, validation, pagination, Swagger/OpenAPI documentation, PostgreSQL persistence, Docker deployment, and automated tests**.

---

## 🚀 Features

* Product CRUD operations
* Product–Item one-to-many relationship
* JWT-based authentication
* Refresh-token rotation and revocation
* Role-based access control (`USER` / `ADMIN`)
* Request validation
* Pagination for product listing
* Global exception handling
* PostgreSQL database integration
* Swagger / OpenAPI documentation
* Docker and Docker Compose support
* Unit and integration tests
* Database indexes for frequently queried fields
* Transactional product updates

---

## 🛠️ Technology Stack

| Technology         | Version / Purpose              |
| ------------------ | ------------------------------ |
| Java               | 17                             |
| Spring Boot        | 3.3                            |
| Spring Web         | REST APIs                      |
| Spring Data JPA    | Database persistence           |
| Hibernate          | ORM                            |
| PostgreSQL         | 16                             |
| Spring Security    | Authentication & authorization |
| JWT                | Stateless authentication       |
| Jakarta Validation | Request validation             |
| Springdoc OpenAPI  | Swagger documentation          |
| JUnit 5            | Testing                        |
| Mockito            | Unit testing                   |
| H2                 | Integration testing            |
| Docker             | Containerization               |
| Docker Compose     | Multi-container deployment     |

---

## 📁 Project Structure

```text
src/
├── main/
│   ├── java/com/zest/products/
│   │   ├── auth/
│   │   │   ├── AppUser.java
│   │   │   ├── AppUserRepository.java
│   │   │   ├── AuthController.java
│   │   │   ├── AuthService.java
│   │   │   ├── JwtService.java
│   │   │   ├── RefreshToken.java
│   │   │   └── RefreshTokenRepository.java
│   │   │
│   │   ├── common/
│   │   │   └── ApiExceptionHandler.java
│   │   │
│   │   ├── config/
│   │   │   ├── DataInitializer.java
│   │   │   ├── OpenApiConfig.java
│   │   │   └── SecurityConfig.java
│   │   │
│   │   ├── product/
│   │   │   ├── Item.java
│   │   │   ├── Product.java
│   │   │   ├── ProductController.java
│   │   │   ├── ProductDtos.java
│   │   │   ├── ProductRepository.java
│   │   │   └── ProductService.java
│   │   │
│   │   └── ProductsApplication.java
│   │
│   └── resources/
│       └── application.yml
│
└── test/
    ├── java/
    │   └── com/zest/products/product/
    │       ├── ProductApiIntegrationTest.java
    │       └── ProductServiceTest.java
    │
    └── resources/
        └── application.yml
```

---

# 🐳 Running with Docker

## Prerequisites

* Docker Desktop
* Internet connection for the initial image and Maven dependency downloads

From the project root:

```bash
docker compose up --build
```

Wait until the application logs show:

```text
Started ProductsApplication
```

### Application URLs

| Service      | URL                                   |
| ------------ | ------------------------------------- |
| Swagger UI   | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs     |
| API Base URL | http://localhost:8080/api/v1          |

### Stop the application

Press:

```text
Ctrl + C
```

Then run:

```bash
docker compose down
```

To remove the PostgreSQL volume as well:

```bash
docker compose down -v
```

---

# 💻 Running Without Docker

## Prerequisites

Install:

* Java 17+
* Maven 3.9+
* PostgreSQL 16+

Create a PostgreSQL database named:

```text
products
```

Set the required environment variables.

### PowerShell

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/products"
$env:DB_USERNAME = "products"
$env:DB_PASSWORD = "products"
$env:JWT_SECRET = "replace-this-with-a-unique-secret-of-at-least-32-bytes"
```

Start the application:

```bash
mvn spring-boot:run
```

---

# 🔐 Authentication

A development administrator is automatically created during the first application startup.

### Development credentials

```text
Username: admin
Password: Admin123!
```

> ⚠️ These credentials are intended only for development/testing. Change them before production deployment.

---

## Login

Send a request to:

```http
POST /api/v1/auth/login
```

The response contains:

```json
{
  "accessToken": "<access-token>",
  "refreshToken": "<refresh-token>"
}
```

Use the access token for protected endpoints:

```http
Authorization: Bearer <accessToken>
```

### Token Expiration

Access tokens expire after **15 minutes by default**.

Refresh tokens can be used through:

```http
POST /api/v1/auth/refresh
```

Refresh-token rotation is implemented so that the submitted refresh token is **revoked after successful use**.

---

# 👥 Roles & Authorization

| Role    | Permissions                               |
| ------- | ----------------------------------------- |
| `USER`  | Read Products and Items                   |
| `ADMIN` | Read, create, update, and delete Products |

---

# 📡 API Endpoints

| Method   | Endpoint                          | Access      | Description                      |
| -------- | --------------------------------- | ----------- | -------------------------------- |
| `POST`   | `/api/v1/auth/login`              | Public      | Authenticate user                |
| `POST`   | `/api/v1/auth/refresh`            | Public      | Rotate refresh token             |
| `GET`    | `/api/v1/products?page=0&size=20` | USER, ADMIN | Get paginated products           |
| `GET`    | `/api/v1/products/{id}`           | USER, ADMIN | Get product by ID                |
| `POST`   | `/api/v1/products`                | ADMIN       | Create product and items         |
| `PUT`    | `/api/v1/products/{id}`           | ADMIN       | Update product and replace items |
| `DELETE` | `/api/v1/products/{id}`           | ADMIN       | Delete product                   |
| `GET`    | `/api/v1/products/{id}/items`     | USER, ADMIN | Get product items                |

---

# 📦 Product API Example

### Create Product

```http
POST /api/v1/products
Authorization: Bearer <accessToken>
Content-Type: application/json
```

Request:

```json
{
  "productName": "Laptop",
  "items": [
    {
      "quantity": 10
    },
    {
      "quantity": 5
    }
  ]
}
```

---

# 🏗️ Architecture

```text
                    ┌─────────────────┐
                    │    HTTP Client  │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │   Controller    │
                    └────────┬────────┘
                             │
                    Validation + JWT
                             │
                             ▼
                    ┌─────────────────┐
                    │     Service     │
                    └────────┬────────┘
                             │
                       Transactions
                             │
                             ▼
                    ┌─────────────────┐
                    │   Repository    │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │   PostgreSQL    │
                    └─────────────────┘
```

### Layer Responsibilities

**Controller**

* Exposes versioned REST endpoints
* Handles HTTP requests/responses
* Applies endpoint-level authorization

**Service**

* Contains business logic
* Handles transactional operations
* Manages Product and Item updates

**Repository**

* Uses Spring Data JPA
* Handles database persistence and queries

**Security**

* Stateless JWT authentication
* Bearer-token validation
* Role-based authorization
* Refresh-token rotation

**Entities**

* `Product` → one-to-many → `Item`
* Orphan removal is used when replacing Product Items

---

# 🗄️ Database

The application uses **PostgreSQL 16** for persistence.

Indexes are provided for frequently queried fields, including:

* Product name
* Item → Product relationship
* Username
* Refresh-token lookup

The test environment uses an **H2 in-memory database**.

---

# 🧪 Testing

Run all tests with:

```bash
mvn test
```

The project includes:

### Unit Tests

* `ProductServiceTest`
* Mockito-based service testing
* Business logic validation

### Integration Tests

* `ProductApiIntegrationTest`
* Spring Boot integration testing
* H2 in-memory database
* API-level behavior validation

---

# 📖 API Documentation

Swagger UI is available after starting the application:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI specification:

```text
http://localhost:8080/v3/api-docs
```

Swagger can be used to explore and test the API endpoints directly from the browser.

---

# 🔒 Production Considerations

Before deploying to production:

* Change the default administrator credentials.
* Generate a strong, unique `JWT_SECRET`.
* Never commit secrets or credentials to Git.
* Restrict `CORS_ORIGINS` to trusted frontend domains.
* Enable HTTPS using `ENFORCE_HTTPS=true`.
* Terminate TLS at a reverse proxy or load balancer.
* Use database migrations such as Flyway instead of Hibernate auto-DDL.
* Use production-specific database credentials.
* Configure appropriate logging and monitoring.

---

# 🚀 Quick Start

For the fastest setup:

```bash
git clone <repository-url>
cd zest-products-api
docker compose up --build
```

Then open:

```text
http://localhost:8080/swagger-ui.html
```

Login using the development administrator credentials and use the returned JWT access token to test protected Product APIs.

---

## 👨‍💻 Assignment

This project was developed as part of the **Zest India Java Backend Developer assignment**, demonstrating REST API development, Spring Boot architecture, authentication/authorization, database integration, testing, API documentation, and Docker-based deployment.

