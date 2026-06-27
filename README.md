# Spring Boot Auth Boilerplate

[![SonarQube Analysis](https://github.com/TheSmoothRere/spring-boot-boilerplate/actions/workflows/sonar.yml/badge.svg)](https://github.com/TheSmoothRere/spring-boot-boilerplate/actions/workflows/sonar.yml)
[![SonarQube Cloud](https://sonarcloud.io/images/project_badges/sonarcloud-highlight.svg)](https://sonarcloud.io/summary/new_code?id=TheSmoothRere_spring-boot-boilerplate)

A production-ready **Spring Boot 4.x** authentication boilerplate with session-based security, PostgreSQL, Redis, and Flyway migrations. Built for developers who want a solid auth foundation without starting from zero.

## Features

- 🔐 **Session-based Authentication** - Secure HTTP-only cookies with Redis backing
- 🛡️ **CSRF Protection** - Cookie-based tokens accessible to SPAs via `X-CSRF-TOKEN` header
- 👥 **Role-Based Access Control** - `USER` / `ADMIN` roles with method-level security (`@PreAuthorize`)
- ✅ **Input Validation** - Bean Validation on all request DTOs
- 📦 **Standardized API Responses** - Consistent `ApiResponse<T>` wrapper for all endpoints
- 🚨 **Global Error Handling** - Structured error codes (`VALIDATION_ERROR`, `ENTITY_EXISTS`, `UNAUTHORIZED`, `ACCESS_DENIED`, `INTERNAL_SERVER_ERROR`)
- 🗄️ **Database Migrations** - Flyway with PostgreSQL enums, triggers, and default data
- 📊 **Observability Ready** - Structured logging, health endpoints, JaCoCo coverage

## Tech Stack

| Component | Version |
|-----------|---------|
| Java | 25 (toolchain) |
| Spring Boot | 4.1.0 |
| Spring Security | 6.x |
| Spring Data JPA | 3.x |
| Spring Session | 3.x (Redis) |
| Flyway | 11.x |
| PostgreSQL | 16+ |
| Redis | 7+ |
| Lombok | Latest |
| JUnit | 5 (Jupiter) |
| JaCoCo | Code coverage |

## Quick Start

### Prerequisites

- Java 25+
- PostgreSQL 16+
- Redis 7+

### Run Locally

```bash
# Set environment variables
export DATABASE_URL=jdbc:postgresql://localhost:5432/yourdb
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=postgres
export REDIS_URL=redis://localhost:6379

# Run with dev profile (shows SQL, allows HTTP cookies)
./gradlew bootRun --args='--spring.profiles.active=dev'
```

**Server starts at:** `http://localhost:8181`

### Run with Docker (Optional)

```bash
# Start PostgreSQL and Redis
docker compose up -d

# Then run the application
./gradlew bootRun --args='--spring.profiles.active=dev'
```

## Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `DATABASE_URL` | `jdbc:postgresql://localhost:5432/postgres` | PostgreSQL JDBC URL |
| `DATABASE_USERNAME` | `postgres` | Database username |
| `DATABASE_PASSWORD` | `postgres` | Database password |
| `REDIS_URL` | `redis://localhost:6379` | Redis connection URL |

### Profiles

| Profile | Use Case | Cookie Secure | SQL Logging |
|---------|----------|---------------|-------------|
| `default` | Production | `true` (HTTPS only) | `false` |
| `dev` | Local development | `false` (HTTP allowed) | `true` |

Activate dev profile:
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

## API Reference

### Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `GET` | `/api/v1/auth/status` | Health check | No |
| `POST` | `/api/v1/auth/register` | Register new user | No |
| `POST` | `/api/v1/auth/login` | Login & create session | No |
| `POST` | `/api/v1/auth/logout` | Invalidate session | Yes |

### Request/Response Examples

#### Register
```bash
curl -X POST http://localhost:8181/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "johndoe", "password": "securePassword123"}'
```

**Response (201 Created)**
```json
{
  "success": true,
  "message": "Operation successful",
  "timestamp": "2026-01-15T10:30:00Z",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "username": "johndoe"
  }
}
```

#### Login
```bash
curl -X POST http://localhost:8181/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -c cookies.txt \
  -d '{"username": "johndoe", "password": "securePassword123"}'
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "Operation successful",
  "timestamp": "2026-01-15T10:30:00Z",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "username": "johndoe",
    "roles": ["ROLE_USER"]
  }
}
```

#### Access Protected Resource (with CSRF)
```bash
# 1. Get CSRF token from cookie (after login)
# 2. Include in subsequent requests
curl -X GET http://localhost:8181/api/v1/protected \
  -H "X-CSRF-TOKEN: <token-from-cookie>" \
  -b cookies.txt
```

#### Logout
```bash
curl -X POST http://localhost:8181/api/v1/auth/logout \
  -H "X-CSRF-TOKEN: <token-from-cookie>" \
  -b cookies.txt -c cookies.txt
```

### Error Response Format

All errors follow a consistent structure:

```json
{
  "success": false,
  "message": "Validation failed",
  "timestamp": "2026-01-15T10:30:00Z",
  "error": {
    "code": "VALIDATION_ERROR",
    "details": "Validation failed",
    "errors": {
      "username": "Username must be between 3 and 100 characters",
      "password": "Password must be between 8 and 72 characters"
    }
  }
}
```

**Common Error Codes**

| Code | HTTP Status | Description |
|------|-------------|-------------|
| `VALIDATION_ERROR` | 400 | Request validation failed |
| `ENTITY_EXISTS` | 409 | Resource already exists (e.g., duplicate username) |
| `ENTITY_NOT_FOUND` | 404 | Resource not found |
| `UNAUTHORIZED` | 401 | Invalid/missing credentials |
| `ACCESS_DENIED` | 403 | Insufficient permissions |
| `INTERNAL_SERVER_ERROR` | 500 | Unexpected server error |

## Database Schema

### Tables

```sql
-- auth.roles
id (UUID PK), name (VARCHAR 60 UK), description (TEXT), created_at, updated_at

-- auth.users
id (UUID PK), username (VARCHAR 100 UK), password (TEXT), status (ENUM), created_at, updated_at

-- auth.user_roles
user_id (FK), role_id (FK), created_at (PK composite)
```

### Default Data

| Role | Description |
|------|-------------|
| `ADMIN` | System administrator with full access |
| `USER` | Standard application user (assigned on registration) |

### User Statuses

| Status | Description |
|--------|-------------|
| `ACTIVE` | Normal authenticated access |
| `INACTIVE` | Soft-disabled, cannot authenticate |
| `LOCKED` | Security lock (e.g., failed login attempts) |

### Flyway Migration

Schema initialized via `V1__init_auth.sql`:
- Custom `updated_at` trigger function
- PostgreSQL enum for `user_status`
- Tables with UUID PKs, cascading FKs
- Default roles inserted on first run

## Security Architecture

| Aspect | Implementation |
|--------|----------------|
| Password Encoding | BCrypt (cost 10) |
| Session Storage | Redis (Spring Session) |
| Session Timeout | 30 minutes |
| Session Cookie | `SESSION` (HTTP-only, Secure, SameSite=LAX) |
| CSRF Protection | `CookieCsrfTokenRepository` (HttpOnly=false for SPA access) |
| CSRF Header | `X-CSRF-TOKEN` |
| Max Sessions | 1 per user (`maxSessionsPreventsLogin=true`) |
| Session Fixation | Migration strategy |
| Method Security | `@EnableMethodSecurity` + `@PreAuthorize` |
| Public Endpoints | `/api/v1/auth/**` |

### CSRF Flow for SPAs

1. After login, `XSRF-TOKEN` cookie is set (accessible to JavaScript)
2. Read token from cookie: `document.cookie.split('; ').find(c => c.startsWith('XSRF-TOKEN=')).split('=')[1]`
3. Include in requests: `headers['X-CSRF-TOKEN'] = token`
4. Server validates token on state-changing requests

## Project Structure

```
src/main/java/io/github/thesmoothrere/boilerplate/
├── advice/
│   ├── RestExceptionAdvice.java      # Global exception → ApiResponse
│   └── RestResponseAdvice.java       # Auto-wrap @RestController responses
├── config/
│   ├── SecurityConfig.java           # Spring Security filter chain
│   └── SessionConfig.java            # Redis HTTP session
├── controller/
│   └── AuthController.java           # /api/v1/auth endpoints
├── entity/
│   ├── User.java                     # JPA entity (users table)
│   ├── Role.java                     # JPA entity (roles table)
│   ├── UserStatus.java               # ACTIVE/INACTIVE/LOCKED enum
│   └── RoleConstants.java            # USER, ADMIN constants
├── filter/
│   └── CsrfCookieFilter.java         # Ensures CSRF cookie is set
├── model/
│   ├── request/
│   │   ├── RegisterRequest.java      # username, password (validated)
│   │   └── LoginRequest.java         # username, password
│   └── response/
│       ├── AuthResponse.java         # id, username, roles
│       └── ApiResponse.java          # Standardized wrapper
├── repository/
│   ├── UserRepository.java           # findByUsername + EntityGraph
│   └── RoleRepository.java           # findByName
├── service/
│   ├── AuthService.java              # register(), login()
│   ├── AppUserDetailsService.java    # UserDetailsService impl
│   └── AppUserDetails.java           # UserDetails with roles
└── SpringBootBoilerplateApplication.java
```

## Testing & Quality

```bash
# Run all tests with coverage
./gradlew test jacocoTestReport

# View HTML coverage report
open build/reports/jacoco/test/html/index.html

# Generate Javadoc
./gradlew javadoc
# Output: build/docs/javadoc/

# SonarQube analysis (requires SONARQUBE_TOKEN env var)
./gradlew sonar
```

### Quality Gates

- **SonarCloud**: Code quality, security, maintainability
- **JaCoCo**: Line/branch coverage targets
- **Javadoc**: All public APIs documented

## Extending the Boilerplate

### Add Custom Roles
1. Add constant to `RoleConstants.java`
2. Create Flyway migration (e.g., `V2__add_roles.sql`)
3. Use `@PreAuthorize("hasRole('CUSTOM_ROLE')")` on endpoints

### Secure Endpoints
```java
@GetMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public List<UserDto> getAllUsers() { ... }
```

### Custom Error Codes
Extend `RestExceptionAdvice` with new `@ExceptionHandler` methods.

### Pagination/Meta in Responses
Extend `ApiResponse<T>` with `Pageable` or custom metadata fields.

## Deployment Notes

### Health Checks
- `GET /api/v1/auth/status` returns `200 OK` when running
- Spring Boot Actuator available if `spring-boot-starter-actuator` added

### Graceful Shutdown
- Configure `server.shutdown=graceful` in `application.yaml`
- Spring Session handles Redis connection cleanup

### Docker Build
```dockerfile
FROM eclipse-temurin:25-jre
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style
- Run `./gradlew check` before committing
- Follow existing Javadoc conventions
- Maintain test coverage

## License

MIT License - feel free to use in commercial and private projects.

---

**Built with** ❤️ **using Spring Boot 4.x, Java 25, and modern security practices.**