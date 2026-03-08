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
- **Global Redis Caching:** Data retrieval across all core services (appointments, schedules, doctors, records) is accelerated using Spring Cache and Redis, mitigating database load for heavily read endpoints.
- **Concurrency Safety:** Enforces strict unique constraints (`doctor_id`, `slot_date`, `start_time`) to unequivocally prevent double-booking scenarios.
- **Data Integrity & Security:** Utilizes `UUID` for non-sequential, secure primary keys, implements widespread `Soft Deletion` to maintain historical data without clutter, and uses **Redis** for stateful, immediate JWT token invalidation (Blacklisting).
- **Robust Validation & Error Handling:** Global request payload validation using `jakarta.validation` constraints (`@NotBlank`, `@NotNull`, `@Size`, `@Email`), coupled with a centralized generic exception handler to return clean, standardized error messages.

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

## 📚 Project Documentation Preview

To help you navigate the codebase and understand the business logic, we provide a comprehensive set of guides in the `docs/` directory:

- ✨ **[Features & System Flow](docs/FEATURES_AND_FLOW.md)**: A high-level overview of our **Role-Based Access Control** (RBAC), the **Smart Scheduling Engine** logic, a visual Mermaid flowchart of our end-to-end system, and the appointment booking lifecycle.
- 🏗️ **[System Architecture & Tech Stack](docs/ARCHITECTURE.md)**: Deep dive into the technical design, covering our **layered project structure**, CI/CD pipelines via GitHub Actions, secure tunnel-based deployment infrastructure, and the specific frameworks/versions used.
- 🔐 **[Roles & Permissions](docs/ROLES.md)**: Detailed mapping of available roles (`ADMIN`, `DOCTOR`, `PATIENT`) to specific API endpoints and restricted actions.
- 🗄️ **[Database Design](docs/DATABASE.md)**: Comprehensive overview of our schema, including core entities like `UserAccount`, `Doctor`, and `Appointment`, plus a link to our full Mermaid ERD.
- 📖 **[API Documentation](docs/API_DOCUMENTATION.md)**: Quick reference for the most important REST endpoints, including base paths, authentication requirements, and common status codes.
- 🚀 **[Getting Started](docs/GETTING_STARTED.md)**: Everything you need to get the project running locally, from cloning the repo and setting up **MySQL/Redis** to configuring environment variables.

---

## 👨‍💻 Author

Built as a comprehensive backend portfolio project to demonstrate mastery of Spring Boot, architecture design, and RESTful API best practices.

**Suis Setiawan**

- LinkedIn: [https://www.linkedin.com/in/suis-setiawan-79a984236/]
- GitHub: [https://github.com/suissetiawan]
- Portfolio: [https://suissetiawan.my.id]
