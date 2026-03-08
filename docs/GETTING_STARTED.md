# 🚀 Getting Started & Deployment

## Prerequisites

- **JDK 21** or higher
- **MySQL 8** (local or remote)
- **Redis** (local or via Docker)
- **Maven** (or use the included `./mvnw` wrapper — no installation needed)
- **Docker & Docker Compose** (for containerized deployment)

---

## Option 1: Run Locally (without Docker)

### 1. Clone the repository

```bash
git clone <your-repo-url>
cd api-medicareflow
```

### 2. Start Redis & MySQL (quick setup via Docker)

```bash
docker run --name redis-medicare -p 6379:6379 -d redis
docker run --name mysql-medicare \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=medicare_flow \
  -p 3306:3306 -d mysql:8
```

### 3. Configure Environment Variables

Create a `.env` file (or copy from `env.example`) and set the following variables:

```properties
# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=medicare_flow
DB_USERNAME=root
DB_PASSWORD=your_secure_password

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# JWT
JWT_SECRET=your_very_long_secure_secret_key_hex_or_base64_encoded
JWT_EXPIRED_IN=1h
```

> ⚠️ `JWT_EXPIRED_IN` uses duration format (e.g., `1h`, `30m`). Do **not** use milliseconds.

### 4. Build and Run

```bash
./mvnw spring-boot:run
```

The application starts on **port 8080** by default.

---

## Option 2: Run with Docker Compose

The provided `docker-compose.yml` pulls the pre-built image from Docker Hub and runs just the API container. Ensure your `.env` file is configured correctly (pointing to your external MySQL and Redis instances).

```bash
docker compose up -d
```

> The application will be accessible at **port 8081** on the host machine (mapped to internal port 8080).

---

## Verify the Application

Once running, open your browser and navigate to:

- **Swagger UI:** `http://localhost:8080/docs/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8080/docs/api-docs`

---

## CI/CD Deployment

On every push to the main branch, **GitHub Actions** automatically:

1. Builds the bootable JAR using Maven
2. Builds a Docker image
3. Pushes the image to **Docker Hub** under `${DOCKERHUB_USERNAME}/medicareflow-api:latest`

The production server pulls the latest image and restarts the container via the `docker-compose.yml`.
