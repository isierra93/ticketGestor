# Spec — CoreDesk API

> Documentación del estado actual del código. Última revisión: 2026-07-23.

---

## Resumen

API REST para la gestión de tickets de soporte (issue tracker). Nombre comercial: **CoreDesk**. Implementada en Spring Boot 4.0.1 / Java 25. La URL de producción es `https://coredesk.isierra93.site/api/v1`. Todas las rutas tienen prefijo `/api/v1` (configurado en `server.servlet.context-path`).

---

## Entidades

### `User` — tabla `usuarios`

| Campo      | Tipo       | Restricciones                        |
|------------|------------|--------------------------------------|
| `id`       | Long       | PK, auto-increment                   |
| `email`    | String     | Unique                               |
| `password` | String     | BCrypt en base de datos              |
| `userRole` | `UserRole` | Enum, almacenado como STRING en DB   |

**Enum `UserRole`:**

| Valor   | Authority string |
|---------|-----------------|
| ADMIN   | ROLE_ADMIN      |
| AGENT   | ROLE_AGENT      |
| CLIENT  | ROLE_CLIENT     |

> `UserRole` implementa `GrantedAuthority` directamente — su método `getAuthority()` devuelve el string de Authority (ej. `"ROLE_ADMIN"`).

---

### `Ticket` — tabla `tickets`

| Campo            | Tipo             | Restricciones / Defaults            |
|------------------|------------------|-------------------------------------|
| `id`             | Long             | PK, auto-increment                  |
| `tkNumber`       | Long             | Número de ticket del negocio; validado como único en service (no hay constraint DB declarado) |
| `site`           | String           |                                     |
| `ticketPriority` | `TicketPriority` | Enum, almacenado como STRING         |
| `createdDate`    | LocalDateTime    | Default: `LocalDateTime.now()` al instanciar la entidad |
| `description`    | String           |                                     |
| `ticketState`    | `TicketState`    | Default: `ABIERTO` al instanciar la entidad |
| `type`           | String           |                                     |
| `user`           | `User`           | FK `user_id` → `usuarios.id` (`@ManyToOne`) |

**Enum `TicketPriority`:** `URGENTE`, `ALTA`, `MEDIA`, `BAJA`

**Enum `TicketState`:** `ABIERTO`, `ASIGNADO`, `EN_CURSO`, `CERRADO`

---

### `Comentario` — tabla `comentarios`

| Campo        | Tipo           | Restricciones / Defaults                              |
|--------------|----------------|-------------------------------------------------------|
| `id`         | Long           | PK, auto-increment                                   |
| `createdAt`  | LocalDateTime  | Default: `LocalDateTime.now()` al instanciar la entidad |
| `comentario` | String         | Requerido, no nulo, no vacío                         |
| `user`       | `User`         | FK `user_id` → `usuarios.id` (`@ManyToOne`), NOT NULL |
| `ticket`     | `Ticket`       | FK `ticket_id` → `tickets.id` (`@ManyToOne`), NOT NULL |

---

## Endpoints

### Auth — `/auth` (público, sin JWT)

#### `POST /auth/register`

Registra un nuevo usuario.

**Request body:**
```json
{ "email": "usuario@ejemplo.com", "password": "password123" }
```

**Respuestas:**

| Código | Caso |
|--------|------|
| 201    | Usuario creado (sin body) |
| 400    | email o password nulos/vacíos, o contienen espacios |
| 409    | El email ya está en uso |

---

#### `POST /auth/login`

Autentica un usuario y devuelve un JWT.

**Request body:**
```json
{ "email": "usuario@ejemplo.com", "password": "password123" }
```

**Respuesta 200:**
```json
{ "token": "<jwt>" }
```

| Código | Caso |
|--------|------|
| 200    | Credenciales válidas; devuelve `TokenDto` |
| 400    | JSON malformado |
| 401    | Email o contraseña incorrectos |

---

### Tickets — `/ticket` (requieren JWT)

Todos los endpoints envían/reciben `Content-Type: application/json`. El JWT va en el header `Authorization: Bearer <token>`.

---

#### `GET /ticket`

Devuelve todos los tickets.

**Respuesta 200:** array de `TicketDto`.

---

#### `GET /ticket/{id}`

Devuelve un ticket por su `id` (PK de base de datos).

| Código | Caso |
|--------|------|
| 200    | Ticket encontrado; devuelve `TicketDto` |
| 404    | No existe ticket con ese `id` |

---

#### `POST /ticket`

Crea un nuevo ticket. El ticket queda asociado al usuario autenticado (extraído del JWT vía `@AuthenticationPrincipal`). **Requiere rol `ROLE_CLIENT` o `ROLE_ADMIN`** — los agentes (`ROLE_AGENT`) no pueden crear tickets.

**Request body (`SaveTicketDto`):**
```json
{
  "tkNumber": 12345,
  "site": "Sucursal A",
  "priority": "ALTA",
  "description": "Descripción del problema",
  "type": "Incidencia"
}
```

El `state` no se acepta en el body de creación — siempre nace como `ABIERTO`. La `createdDate` la fija la entidad en el momento de instanciación.

**Respuesta 201:** `TicketDto` en el body + header `Location: /ticket/{id}`.

| Código | Caso |
|--------|------|
| 201    | Creado correctamente |
| 400    | Algún campo requerido es nulo, o JSON malformado, o enum inválido |
| 403    | El usuario tiene `ROLE_AGENT` |
| 409    | Ya existe un ticket con ese `tkNumber` |

---

#### `PUT /ticket/{id}`

Actualiza todos los campos de un ticket existente. Localiza el ticket por `id` (PK), luego sobreescribe los campos con el DTO. **Requiere rol `ROLE_CLIENT` o `ROLE_ADMIN`** — los agentes deben usar `PATCH /ticket/{id}/state` para cambiar el estado.

**Request body (`TicketDto`):**
```json
{
  "tkNumber": 12345,
  "site": "Sucursal B",
  "priority": "MEDIA",
  "description": "Nueva descripción",
  "type": "Soporte",
  "state": "EN_CURSO"
}
```

Los campos `createdDate` y `user` no se actualizan (no están en `updateTicketFromDto`).

| Código | Caso |
|--------|------|
| 200    | Actualizado; devuelve `TicketDto` |
| 400    | Campo requerido nulo, o JSON malformado |
| 403    | El usuario tiene `ROLE_AGENT` |
| 404    | No existe ticket con ese `id` |
| 409    | El `tkNumber` enviado ya existe en otro ticket |

---

#### `PATCH /ticket/{id}/state`

Actualiza **únicamente el estado** de un ticket. Disponible para todos los roles autenticados, pero con restricción para agentes: **`ROLE_AGENT` solo puede establecer `EN_CURSO` o `CERRADO`**.

**Request body (`TicketStateUpdateDto`):**
```json
{ "state": "EN_CURSO" }
```

| Código | Caso |
|--------|------|
| 200    | Estado actualizado; devuelve `TicketDto` |
| 400    | `ROLE_AGENT` intentó establecer un estado distinto de `EN_CURSO` o `CERRADO` |
| 404    | No existe ticket con ese `id` |

---

#### `GET /ticket/{ticketId}/comentarios`

Devuelve todos los comentarios de un ticket, ordenados por `createdAt` ASC.

**Respuesta 200:** array de `ComentarioDto`.

| Código | Caso |
|--------|------|
| 200    | Lista de comentarios obtenida |
| 401    | Sin JWT válido |
| 404    | No existe ticket con ese `ticketId` |

---

#### `POST /ticket/{ticketId}/comentarios`

Agrega un comentario al ticket. El autor se toma del JWT (`@AuthenticationPrincipal`). Disponible para cualquier rol autenticado (incluso `ROLE_AGENT`).

**Request body (`SaveComentarioDto`):**
```json
{ "comentario": "Revisé el equipo, falta el repuesto X." }
```

**Respuesta 201:** `ComentarioDto` en el body + header `Location: /ticket/{ticketId}/comentarios/{id}`.

| Código | Caso |
|--------|------|
| 201    | Comentario creado |
| 400    | `comentario` nulo o vacío, o JSON malformado |
| 401    | Sin JWT válido |
| 404    | No existe ticket con ese `ticketId` |

---

#### `DELETE /ticket/{id}`

Elimina un ticket por su `id`. **Requiere rol `ROLE_ADMIN`.**

| Código | Caso |
|--------|------|
| 204    | Eliminado correctamente (sin body) |
| 401    | Sin JWT válido |
| 403    | JWT válido pero el usuario no tiene `ROLE_ADMIN` |
| 404    | No existe ticket con ese `id` |

---

## DTOs

| DTO                    | Campos                                                                 | Usado en                  |
|------------------------|------------------------------------------------------------------------|---------------------------|
| `UserDto`              | email, password                                                        | body de /auth/register y /auth/login |
| `TokenDto`             | token                                                                  | respuesta de /auth/login  |
| `SaveTicketDto`        | tkNumber, site, priority, description, type                            | body de POST /ticket      |
| `TicketDto`            | tkNumber, site, priority, createdDate, description, state, type, userOwnerDto | respuesta de ticket; body de PUT /ticket |
| `TicketStateUpdateDto` | state                                                                  | body de PATCH /ticket/{id}/state |
| `SaveComentarioDto`    | comentario                                                             | body de POST /ticket/{id}/comentarios |
| `ComentarioDto`        | id, createdAt, comentario, userOwnerDto                                | respuesta de comentario    |
| `UserOwnerDto`         | email                                                                  | anidado en TicketDto y ComentarioDto |
| `ErrorDto`             | message, error, status, timestamp                                      | todas las respuestas de error |

---

## Reglas de negocio

### Registro de usuario

1. `email` no puede ser nulo ni vacío (`ResourceIncompleteException` → 400).
2. `email` no puede contener espacios (`InvalidDataFormatException` → 400).
3. `password` no puede ser nulo ni vacío (`ResourceIncompleteException` → 400).
4. `password` no puede contener espacios (`InvalidDataFormatException` → 400).
5. El email no puede estar ya registrado (`ResourceAlreadyExistsException` → 409).
6. Todos los usuarios registrados reciben el rol `CLIENT` por defecto.
7. La contraseña se hashea con BCrypt antes de persistirse.

### Creación de ticket

1. `tkNumber`, `site`, `priority`, `description`, `state` y `type` no pueden ser nulos (`ResourceIncompleteException` → 400). En la práctica, `state` siempre es `ABIERTO` por default de la entidad, por lo que la validación de `state` en el POST nunca falla.
2. `tkNumber` debe ser único entre todos los tickets (`ResourceAlreadyExistsException` → 409).
3. El ticket queda vinculado al usuario autenticado que hace el request.

### Actualización de ticket

1. Mismas validaciones de nulos que en la creación, incluyendo `state`.
2. `tkNumber` debe ser único entre los demás tickets (se excluye el propio ticket del chequeo, por lo que enviar el mismo `tkNumber` actual es válido).
3. Los campos `createdDate` y `user` no se modifican en ningún update.

### Permisos por rol sobre tickets

| Acción                          | CLIENT | AGENT                          | ADMIN |
|---------------------------------|--------|--------------------------------|-------|
| GET /ticket                     | ✅     | ✅                             | ✅    |
| GET /ticket/{id}                | ✅     | ✅                             | ✅    |
| POST /ticket                    | ✅     | ❌ (403)                       | ✅    |
| PUT /ticket/{id}                | ✅     | ❌ (403)                       | ✅    |
| PATCH /ticket/{id}/state        | ✅ (cualquier estado) | ✅ (solo EN_CURSO / CERRADO) | ✅ |
| GET /ticket/{id}/comentarios    | ✅     | ✅                           | ✅    |
| POST /ticket/{id}/comentarios   | ✅     | ✅                           | ✅    |
| DELETE /ticket/{id}             | ❌ (403) | ❌ (403)                     | ✅    |

La restricción de estados para `ROLE_AGENT` en el PATCH se valida en el controller: si el estado enviado no es `EN_CURSO` ni `CERRADO` devuelve `InvalidDataFormatException` → 400.

### Eliminación de ticket

Solo usuarios con `ROLE_ADMIN` pueden eliminar (`@PreAuthorize("hasRole('ADMIN')")`). Los usuarios registrados por defecto reciben `CLIENT`, por lo que no pueden eliminar.

### JWT

- Algoritmo: HS256.
- Claims: `sub` (email del usuario), `role` (string de authority, ej. `"ROLE_ADMIN"`), `iat`, `exp`.
- El filtro `JwtAuthenticationFilter` extrae el email del token, carga el usuario desde DB, valida firma y expiración, y si es válido establece la autenticación en el `SecurityContext`.
- Si no hay token (o es inválido), la request continúa sin autenticación. Spring Security la bloquea si el endpoint lo requiere y `JwtAuthenticationEntryPoint` devuelve un `ErrorDto` 401.

---

## Arquitectura

### Stack

- Spring Boot 4.0.1 / Java 25
- Spring Data JPA + MySQL 8 (Hibernate, `ddl-auto=update`)
- Spring Security stateless con JWT (jjwt 0.11.5)
- SpringDoc OpenAPI 3.0.0 (Swagger UI en `/api/v1/swagger-ui/index.html`)

### Flujo de una request autenticada

```
HTTP Request
  → JwtAuthenticationFilter        (extrae y valida JWT, puebla SecurityContext)
  → Spring Security (autorización)
  → Controller                     (mapea HTTP ↔ DTO)
  → Service interface / *Impl      (lógica y validaciones de negocio)
  → Repository (JPA)               (acceso a MySQL)
  → Mapper                         (entidad ↔ DTO en el controller)
  → HTTP Response
```

### Seguridad — rutas públicas vs protegidas

| Patrón                          | Acceso    |
|---------------------------------|-----------|
| `/auth/login`, `/auth/register` | Público   |
| `/swagger-ui/**`, `/v3/api-docs/**`, `/webjars/**` | Público |
| `GET /ticket`, `GET /ticket/{id}` | JWT válido (cualquier rol) |
| `POST /ticket`, `PUT /ticket/{id}` | JWT válido + `ROLE_CLIENT` o `ROLE_ADMIN` |
| `PATCH /ticket/{id}/state`      | JWT válido (cualquier rol; validación de estado en controller para `ROLE_AGENT`) |
| `GET /ticket/{id}/comentarios`  | JWT válido (cualquier rol) |
| `POST /ticket/{id}/comentarios` | JWT válido (cualquier rol) |
| `DELETE /ticket/{id}`           | JWT válido + `ROLE_ADMIN` |

**CORS:** orígenes `*`, métodos GET/POST/PUT/DELETE/OPTIONS, headers `Authorization` y `Content-Type`.

### Paquetes

```
com.soluciones.ticketgestor
├── controllers/     AuthController, TicketController
├── dtos/            ComentarioDto, ErrorDto, SaveComentarioDto, SaveTicketDto, TicketDto,
│                    TicketStateUpdateDto, TokenDto, UserDto, UserOwnerDto
├── exceptions/      GlobalExceptionHandler + 4 excepciones custom (Runtime)
├── mappers/         ComentarioMapper, TicketMapper, UserMapper  (conversión manual, sin MapStruct)
├── models/          Comentario, Ticket, User + enums TicketPriority, TicketState, UserRole
├── repositories/    ComentarioRepository, TicketRepository, UserRepository  (Spring Data JPA)
├── security/        JwtAuthenticationFilter, JwtAuthenticationEntryPoint, JwtUtils,
│                    SecurityConfig, OpenApiConfig
└── services/        ComentarioService/Impl, TicketService/Impl, UserService/Impl, UserDetailsServiceImpl
```

Cada servicio tiene interfaz + implementación marcada `@Primary`. `UserDetailsServiceImpl` implementa `UserDetailsService` de Spring Security (el username es el email).

### Mappers

- `TicketMapper.toDto(Ticket)` → incluye el usuario como `UserOwnerDto` (solo email).
- `TicketMapper.toEntity(SaveTicketDto, User)` → no mapea `state` ni `createdDate`; los defaults del campo de la entidad los proveen.
- `TicketMapper.updateTicketFromDto(TicketDto, Ticket)` → sobreescribe todos los campos excepto `createdDate` y `user`.
- `UserMapper.toDto(User)` → devuelve solo el email (no expone id, password, ni rol).
- `UserMapper.toEntity(UserDto)` → mapea solo email y password (el rol lo asigna `UserServiceImpl`).

### Manejo de errores

`GlobalExceptionHandler` (`@RestControllerAdvice`) centraliza el mapeo de excepciones a `ErrorDto`:

| Excepción                       | HTTP |
|---------------------------------|------|
| `ResourceAlreadyExistsException`| 409  |
| `ResourceNotFoundException`     | 404  |
| `ResourceIncompleteException`   | 400  |
| `InvalidDataFormatException`    | 400  |
| `HttpMessageNotReadableException` (enum inválido de Priority) | 400 con mensaje específico |
| `HttpMessageNotReadableException` (enum inválido de State)    | 400 con mensaje específico |
| `HttpMessageNotReadableException` (otros)                     | 400 genérico |
| `BadCredentialsException`       | 401  |
| `AccessDeniedException`         | 403  |

### Infraestructura

- Base de datos y credenciales inyectadas exclusivamente via variables de entorno (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET_KEY`, `JWT_EXPIRATION`).
- El `Dockerfile` hace una build multi-stage (Maven + JRE Alpine) y expone el puerto 8080.
- No hay `docker-compose.yml` en el repositorio; el README documenta los valores por defecto del entorno local (MySQL en `localhost:3306`, DB `db-ticket-gestor`, usuario `root`, contraseña `1234`).
