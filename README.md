# Products API

Spring Boot 3 / Java 17 implementation of the Zest India Java Backend assignment. It provides versioned Product CRUD endpoints, child Item handling, JWT authentication with rotating refresh tokens, role-based access control, validation, standardized errors, pagination, OpenAPI documentation, PostgreSQL Docker support, and H2/Mockito test support.

## Run it

Prerequisites: Java 17+ and Docker (or Maven 3.9+).

```bash
docker compose up --build
```

The API starts at `http://localhost:8080`; Swagger UI is at `http://localhost:8080/swagger-ui.html`.

For a non-Docker run, set `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, and a strong 32+ byte `JWT_SECRET`, then run `mvn spring-boot:run`. Tests run with `mvn test` and use H2.

## Authentication

The application initializes a development administrator on first start:

```json
POST /api/v1/auth/login
{"username":"admin","password":"Admin123!"}
```

Use `accessToken` as `Authorization: Bearer <accessToken>`. `POST /api/v1/auth/refresh` accepts `{ "refreshToken": "..." }`; each successful refresh revokes the submitted token and returns a new pair.

Change the seeded password/user strategy before production deployment. Set a unique random `JWT_SECRET`, restrict `CORS_ORIGINS`, terminate TLS at a trusted reverse proxy/load balancer, and set `ENFORCE_HTTPS=true`; the application then rejects plain HTTP by redirecting to HTTPS (and honors `X-Forwarded-Proto`).

## API

| Method | Endpoint | Role | Purpose |
|---|---|---|---|
| GET | `/api/v1/products?page=0&size=20` | USER, ADMIN | Paginated products |
| GET | `/api/v1/products/{id}` | USER, ADMIN | One product |
| POST | `/api/v1/products` | ADMIN | Create product |
| PUT | `/api/v1/products/{id}` | ADMIN | Update product and item list |
| DELETE | `/api/v1/products/{id}` | ADMIN | Delete product |
| GET | `/api/v1/products/{id}/items` | USER, ADMIN | Product items |

Create/update body:

```json
{"productName":"Coffee","items":[{"quantity":10},{"quantity":5}]}
```

Error responses consistently include timestamp, HTTP status, message, path, and per-field validation errors when applicable. Database indexes are created for product name, item product ID, usernames, and refresh-token lookup.

## Architecture

The project follows a simple layered design: controllers expose REST resources, services own transactions/business rules, repositories own persistence, and a stateless security filter validates bearer access tokens. `Product` owns its `Item` collection with orphan removal, so updating an item list is atomic. Refresh tokens are persisted and single-use, providing rotation and revocation on refresh.
