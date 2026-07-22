# AGENTS.md — Ticket Gestor (CoreDesk)

Spring Boot 4.0.1 / Java 25 REST API at `com.soluciones.ticketgestor`. Production: `https://coredesk.isierra93.site/api/v1`. All routes prefixed `/api/v1` via `server.servlet.context-path`.

## Read first — Spec-Driven Development (SDD)

This project follows **Spec-Driven Development (SDD)**. The single source of truth is [`spec.md`](./spec.md) — it documents the current state of the code (entities, endpoints, DTOs, role matrix, business rules) and is updated alongside code changes.

- **Read `spec.md` at the start of every session and before any non-trivial change.**
- When `spec.md` and the code disagree, trust the code and update the spec.
- This file (`AGENTS.md`) only adds what isn't already obvious in `spec.md` — non-obvious commands, gotchas, conventions.

## Quick commands

```bash
# Local MySQL (see "docker-compose gotcha" below)
docker-compose up -d

# Run app (needs all 5 env vars from application.properties)
./mvnw spring-boot:run

# Build, skip tests
./mvnw clean package -DskipTests

# Unit tests only — TicketServiceImplTest is a pure Mockito test
./mvnw test -Dtest=TicketServiceImplTest

# All tests
./mvnw test
```

## Environment variables (all required, no defaults)

Set before running the app — `application.properties` uses `${VAR}` placeholders, the app will fail to start if any is missing:

| Var | Purpose |
|---|---|
| `DB_URL` | e.g. `jdbc:mysql://localhost:3306/db-ticket-gestor` |
| `DB_USERNAME` | DB user |
| `DB_PASSWORD` | DB password |
| `JWT_SECRET_KEY` | HS256 signing secret, **must be ≥32 chars** |
| `JWT_EXPIRATION` | Token TTL in ms |

## docker-compose gotcha

`docker-compose.yml` is listed in `.gitignore` (line 42) and **not committed**. You must create it locally before `./mvnw spring-boot:run`. The README/CLAUDE.md documented defaults are: MySQL on `localhost:3306`, DB `db-ticket-gestor`, user `root`, pass `1234`.

`Dockerfile` is committed (multi-stage Maven + JRE Alpine, exposes 8080).

## Architecture (one-liner)

`Controller → Service interface → ServiceImpl (@Primary) → Spring Data JPA Repository → MySQL`. Mappers are hand-written (`mappers/`, no MapStruct). All exceptions funnel through `GlobalExceptionHandler` → `ErrorDto`.

## Lombok

Lombok 1.18.42 is on the classpath. `Ticket` uses class-level `@Builder @Getter @Setter @NoArgsConstructor @Accessors(prefix = "ticket")` — the `@Accessors` strips the `ticket` prefix from `ticketPriority`/`ticketState` so generated accessors are `getPriority()/setPriority()/getState()/setState()` (matching DTOs and call sites). Defaults (`createdDate = LocalDateTime.now()`, `ticketState = ABIERTO`) live on the entity, not in DTOs. `User` and all DTOs use manual getters/setters — no Lombok on them.

## Tests

- Pure unit tests: Mockito only (`@ExtendWith(MockitoExtension.class)`), no Spring context. `TicketServiceImplTest` is the only example.
- The default `TicketGestorApplicationTests.contextLoads()` is `@SpringBootTest` and **fails without all 5 env vars** (it tries to stand up the full app and Hibernate, and will throw `Driver claims to not accept jdbcUrl, ${DB_URL}` if `DB_URL` is unset). Exclude it when iterating without a DB:
  ```bash
  ./mvnw test -Dtest='!TicketGestorApplicationTests'
  ```
- No integration test DB substitute (H2, Testcontainers) is configured.

## Conventions worth knowing

- `tkNumber` is the business ticket number (unique, validated in service via `existsByTkNumber` / `existsByTkNumberAndIdNot`); `id` is the DB PK. They are not the same.
- `Ticket` defaults: `ticketState = ABIERTO` and `createdDate = LocalDateTime.now()` set on the entity, not in the DTO.
- POST `/ticket` ignores `state` from the body — it always starts as `ABIERTO`. The mapper's `toEntity` doesn't map it.
- PUT `/ticket/{id}` skips `createdDate` and `user` updates (see `TicketMapper.updateTicketFromDto`).
- `UserRole` implements `GrantedAuthority` directly — `getAuthority()` returns the string `"ROLE_ADMIN"` etc., not the enum name.
- `JwtAuthenticationFilter` does **not** reject missing/invalid tokens; it just continues and lets Spring Security block via `JwtAuthenticationEntryPoint` (returns 401 `ErrorDto`).
- `@PreAuthorize` is used in controllers for role checks (e.g. `hasRole('ADMIN')` for DELETE, `hasAnyRole('CLIENT','ADMIN')` for POST/PUT). `@EnableMethodSecurity` is in `SecurityConfig`.
- CORS is wide open (`*` origins, GET/POST/PUT/DELETE/OPTIONS) in `SecurityConfig` — fine for dev, replace before prod.

## What this repo does NOT have

- No `docker-compose.yml` (gitignored — see above).
- No linter or formatter (no Spotless, Checkstyle, SpotBugs).
- No CI workflow (no `.github/`).
- No SpringDoc/Swagger annotations on DTOs beyond controllers (controllers use `@Operation`/`@ApiResponses` extensively).
- No Lombok `@Data`/`@Builder` on DTOs — manual constructors and getters/setters.
- No mapstruct.

Swagger UI (when running): `/api/v1/swagger-ui/index.html`.
