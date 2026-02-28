# 🚀 Getting Started & Deployment

## Prerequisites

- JDK 17 / 21
- MySQL Server
- Redis Server (local or via Docker)
- Maven

## Steps to Run Locally

1. **Clone the repository**

   ```bash
   git clone <your-repo-url>
   cd api-medicareflow
   ```

2. **Start Redis & MySQL** (Example using Docker)

   ```bash
   docker run --name redis-medicare -p 6379:6379 -d redis
   docker run --name mysql-medicare -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=medicare_flow -p 3306:3306 -d mysql:8
   ```

3. **Configure Environment Variables**
   Set the required environment variables in your IDE or `.env` file before launching the application:

   ```properties
   # Database
   DB_URL=jdbc:mysql://localhost:3306/medicare_flow
   DB_USERNAME=root
   DB_PASSWORD=your_secure_password

   # Redis
   REDIS_HOST=localhost
   REDIS_PORT=6379

   # JWT
   JWT_SECRET=your_very_long_secure_secret_key_base64_encoded_string
   JWT_EXPIRATION=86400000 # Example: 24 hours in milliseconds
   ```

4. **Build and Run**
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   ```
