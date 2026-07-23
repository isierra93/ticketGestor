# Ticket Gestor

API REST para la gestión de tickets de soporte, desarrollada con Spring Boot.

## Tecnologías

- Java 25
- Spring Boot 4.0.1
- Spring Security + JWT
- Spring Data JPA
- MySQL 8.0
- Docker Compose

## Requisitos previos

- Java 25+
- Maven 3.x
- Docker y Docker Compose

## Configuración

Levanta la base de datos y ejecuta la aplicación:

```bash
docker compose up -d        # MySQL en localhost:3306
./mvnw spring-boot:run      # app en localhost:8080/api/v1
```

En Windows:
```powershell
.\mvnw.cmd spring-boot:run
```

La instancia de MySQL viene con:
- **Base de datos:** `db-ticket-gestor`
- **Puerto:** `3306`
- **Usuario:** `root`
- **Contraseña:** `1234`

## Endpoints

### Autenticación

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/auth/register` | Registrar nuevo usuario |
| POST | `/auth/login` | Iniciar sesión (retorna JWT) |

#### Ejemplo de registro/login:
```json
{
  "email": "usuario@ejemplo.com",
  "password": "contraseña123"
}
```

### Tickets

Todos los endpoints de tickets requieren autenticación JWT en el header:
```
Authorization: Bearer <token>
```

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/ticket` | Listar todos los tickets |
| GET | `/ticket/{id}` | Obtener ticket por ID |
| POST | `/ticket` | Crear nuevo ticket |
| PUT | `/ticket/{id}` | Actualizar ticket |
| DELETE | `/ticket/{id}` | Eliminar ticket |

#### Modelo de Ticket:
```json
{
  "tkNumber": 12345,
  "site": "Sitio A",
  "priority": "ALTA",
  "description": "Descripción del problema",
  "state": "ABIERTO",
  "type": "Incidencia"
}
```

## Estructura del proyecto

```
src/main/java/com/soluciones/ticketgestor/
├── controllers/     # Controladores REST
├── dtos/            # Objetos de transferencia
├── exceptions/      # Manejo de excepciones
├── mappers/         # Mappers Entity ↔ DTO
├── models/          # Entidades JPA
├── repositories/    # Repositorios JPA
├── security/        # Configuración JWT y seguridad
└── services/        # Lógica de negocio
```

## Licencia

Este proyecto está en desarrollo.
