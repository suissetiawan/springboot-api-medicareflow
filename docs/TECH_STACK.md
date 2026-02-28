# 🛠️ Tech Stack & Project Structure

## Technology Stack

- **Language:** Java
- **Framework:** Spring Boot, Spring Security, Spring Data JPA
- **Database:** MySQL
- **Cache & Key-Value Store:** Redis
- **Documentation:** SpringDoc OpenAPI (Swagger UI)
- **Build Tool:** Maven

---

## 📂 Project Structure

```text
src/main/java/com/dibimbing/medicareflow/
├── config/       # Spring Security, Redis, OpenAPI, and other configurations
├── controller/   # REST API Endpoints
├── dto/          # Data Transfer Objects (Requests & Responses)
├── entity/       # JPA Domain Models mapping to database tables
├── enums/        # Enumerations for Roles, Statuses, Days
├── exception/    # Custom Exceptions and Global Exception Handler (@ControllerAdvice)
├── helper/       # Utility classes and shared helpers
├── repository/   # Spring Data JPA Interfaces
└── service/      # Business logic and transaction management
```
