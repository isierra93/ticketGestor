# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Start the MySQL database (required before running the app)
docker-compose up -d

# Run the application
./mvnw spring-boot:run

# Build (skip tests)
./mvnw clean package -DskipTests

# Run all tests
./mvnw test

# Run a specific test class
./mvnw test -Dtest=TicketServiceImplTest
```

## Environment variables

The app requires these env vars (configured in `application.properties` via `${...}` placeholders):

| Variable | Purpose |
|---|---|
| `DB_URL` | JDBC URL, e.g. `jdbc:mysql://localhost:3306/db-ticket-gestor` |
| `DB_USERNAME` | DB user (default in docker-compose: `root`) |
| `DB_PASSWORD` | DB password (default in docker-compose: `1234`) |
| `JWT_SECRET_KEY` | HS256 signing secret (must be ≥32 chars) |
| `JWT_EXPIRATION` | Token TTL in milliseconds |

## Architecture

Spring Boot 4.0.1 / Java 25 REST API. All routes are prefixed with `/api/v1` (set in `application.properties`).

**Request flow:**
```
Controller → Service interface → ServiceImpl (@Primary) → Repository (Spring Data JPA) → MySQL
```

Mappers (`mappers/`) convert between JPA entities and DTOs manually — no MapStruct or Lombok.  
Models (`models/`) use manual getters/setters — no Lombok.

**Security:**
- JWT filter (`JwtAuthenticationFilter`) validates `Authorization: Bearer <token>` on every request.
- Public routes: `/auth/login`, `/auth/register`, `/swagger-ui/**`, `/v3/api-docs/**`.
- `DELETE /ticket/{id}` requires `ROLE_ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`).
- `POST /ticket` associates the new ticket with the authenticated user via `@AuthenticationPrincipal`.

**Key domain rules:**
- `tkNumber` is the business ticket number (unique constraint); `id` is the DB primary key.
- `TicketState` defaults to `ABIERTO` and `createdDate` defaults to `LocalDateTime.now()` on the entity.
- `updateTicket` in `TicketServiceImpl` also blocks duplicate `tkNumber`, so changing `tkNumber` on an existing ticket will throw `ResourceAlreadyExistsException`.

**Exception handling:**  
All custom exceptions (`ResourceNotFoundException`, `ResourceAlreadyExistsException`, `ResourceIncompleteException`, `InvalidDataFormatException`) are caught and converted to `ErrorDto` responses by `GlobalExceptionHandler`.

**Tests:**  
Unit tests use Mockito (`@ExtendWith(MockitoExtension.class)`) — no Spring context loaded.

**API docs:**  
Swagger UI available at `/api/v1/swagger-ui/index.html` when the app is running.
