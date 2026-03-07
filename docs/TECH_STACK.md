# 🛠️ Tech Stack & Project Structure

## Technology Stack

| Category                | Technology                                  | Version                    |
| ----------------------- | ------------------------------------------- | -------------------------- |
| Language                | Java                                        | 21                         |
| Framework               | Spring Boot                                 | 4.0.3                      |
| Security                | Spring Security + JWT (`jjwt`)              | 0.11.5                     |
| Persistence             | Spring Data JPA + Hibernate                 | (Spring Boot managed)      |
| Database                | MySQL                                       | 8                          |
| Cache / Key-Value Store | Redis (Data Caching & Token Blacklisting)   | (Spring Boot managed)      |
| API Documentation       | SpringDoc OpenAPI (Swagger UI)              | 3.0.1                      |
| Build Tool              | Maven                                       | (via Maven Wrapper `mvnw`) |
| Boilerplate Reduction   | Lombok                                      | (Spring Boot managed)      |
| Bean Validation         | Spring Validation (Jakarta Bean Validation) | (Spring Boot managed)      |
| Containerization        | Docker + Docker Compose                     | -                          |
| CI/CD                   | GitHub Actions                              | -                          |

---

## 📂 Project Structure

```text
src/main/java/com/dibimbing/medicareflow/
├── config/         # Spring Security, JWT Filter, Redis, OpenAPI, Scheduler configs
├── controller/     # REST API Controllers (entry points for HTTP requests)
├── dto/            # Data Transfer Objects
│   ├── request/    # Incoming request payloads
│   └── response/   # Outgoing response payloads
├── entity/         # JPA domain models (mapped to database tables)
│   └── base/       # Shared base entities (BaseUuidEntity, BaseLongEntity)
├── enums/          # Enumerations: Role, AppointmentStatus, SlotStatus, DayOfWeek, DoctorStatus
├── exception/      # Custom exceptions, GlobalExceptionHandler, and security handlers
├── helper/         # Utility classes (ResponseHelper, etc.)
├── repository/     # Spring Data JPA interfaces
└── service/        # Business logic and transaction management
```

---

## 🐳 Docker & Deployment

The application ships as a Docker image and can be run using the provided `docker-compose.yml`.

```yaml
# docker-compose.yml (simplified)
services:
  app:
    image: ${DOCKERHUB_USERNAME}/medicareflow-api:latest
    ports:
      - "8081:8080"
    env_file:
      - .env
```

> The app container joins a shared Docker network (`financial-tracker-network`) which also hosts the external MySQL and Redis instances.
