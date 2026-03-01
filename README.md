# 🏥 MediCareFlow - Medical Consultation Booking API

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2+-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-Latest-red.svg)](https://redis.io/)

## 📝 Project Overview

**MediCareFlow** is a comprehensive, production-ready backend API designed for a modern medical consultation booking system. Built with **Java Spring Boot**, it provides a scalable and secure foundation for managing the complex interactions between Doctors, Patients, and Clinic Administrators.

The core motivation behind this project is to create an automated scheduling engine where doctors can set recurring working hours, and the system dynamically generates bookable "Time Slots" based on specific consultation types (e.g., General Checkup 30 mins, Specialist 60 mins).

### 🌟 High-Level Capabilities:

- **Comprehensive Role Management:** Secure, distinct flows for `ADMIN`, `DOCTOR`, and `PATIENT` using JWT authentication.
- **Smart Scheduling:** Instead of manual slot creation, the system intelligently generates available appointment slots from a doctor's registered `WorkSchedule`.
- **Concurrency Safety:** Enforces strict unique constraints (`doctor_id`, `slot_date`, `start_time`) to unequivocally prevent double-booking scenarios.
- **Data Integrity & Security:** Utilizes `UUID` for non-sequential, secure primary keys, implements widespread `Soft Deletion` to maintain historical data without clutter, and uses **Redis** for stateful, immediate JWT token invalidation (Blacklisting).

This project serves as a robust backend portfolio, demonstrating the application of enterprise architecture patterns, RESTful principles, and modern security practices within the Spring ecosystem.

---

## 🏗️ System Architecture Overview

MediCareFlow is designed with a modern, secure, and automated infrastructure. Our architecture spans from code-to-cloud, incorporating:

- **Layered Code Structure:** Controllers, Services, and Repositories for clear separation of concerns.
- **Automated CI/CD:** GitHub Actions and Docker for seamless containerized delivery.
- **Secure Infrastructure:** Private networking via Tailscale and public exposure through Cloudflare Tunnels (no open inbound ports).
- **High Performance:** MySQL persistence with Redis caching for scale and speed.

> [!TIP]
> **For a full technical breakdown** including our end-to-end flow diagram and infrastructure details, please visit our **[Detailed Architecture Guide](docs/ARCHITECTURE.md)**.

---

---

## 📚 Detailed Documentation

For an in-depth look at how the system was built and how to operate it, please explore the detailed guides below:

1. ⚙️ **[Features & Application Flow](docs/FEATURES_AND_FLOW.md)**: Explore the core functionalities and step-by-step booking flow.
2. 🏗️ **[System Architecture](docs/ARCHITECTURE.md)**: Understand the technical design, including the Slot Generator logic and Redis token blacklisting.
3. 🗄️ **[Database Design](docs/DATABASE.md)**: Overview of the database schema and a link to the complete ERD.
4. 📖 **[API Documentation](https://demo1.suissetiawan.my.id/docs/swagger-ui/index.html)**: Explore the interactive Swagger UI for a complete list of endpoints and live testing.
5. 🚀 **[Getting Started & Deployment](docs/GETTING_STARTED.md)**: Step-by-step guide to running the project locally and configuring environment variables.
6. 🛠️ **[Tech Stack & Structure](docs/TECH_STACK.md)**: Details on the technologies used and folder organization.
7. 🗺️ **[Roadmap](docs/ROADMAP.md)**: Future development plans.

---

## 👨‍💻 Author

Built as a comprehensive backend portfolio project to demonstrate mastery of Spring Boot, architecture design, and RESTful API best practices.

**Suis Setiawan**

- LinkedIn: [https://www.linkedin.com/in/suis-setiawan-79a984236/]
- GitHub: [https://github.com/suissetiawan]
- Portfolio: [https://suissetiawan.my.id]
