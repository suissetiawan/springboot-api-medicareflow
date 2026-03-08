# 🗺️ Roadmap

This document tracks planned improvements and future features for the MedicareFlow system.

## ✅ Implemented

- [x] Role-Based Access Control (ADMIN, DOCTOR, PATIENT)
- [x] JWT Authentication with Redis-backed token blacklisting
- [x] Automated time slot generation based on doctor work schedules
- [x] Appointment booking with slot availability validation
- [x] Appointment status lifecycle management
- [x] Consultation record creation by doctors
- [x] Soft deletion with restore capability (Users, Work Schedules)
- [x] Unique appointment reference numbers
- [x] Swagger UI / OpenAPI documentation
- [x] CI/CD pipeline via GitHub Actions + Docker Hub
- [x] Containerized deployment with Docker Compose

## 🔜 Planned

- [ ] **Notification System:** Email and/or push notifications for appointment confirmations, reminders, and status changes.
- [ ] **Payment Gateway Integration:** Support for secure online payment of consultation fees before a booking is confirmed.
- [ ] **Telemedicine Integration:** Third-party meeting link generation (e.g., Google Meet, Zoom) for virtual consultations.
- [ ] **Patient Medical History:** A dedicated endpoint to retrieve a patient's full consultation history and medical records across all appointments.
- [ ] **Doctor Rating & Review:** Allow patients to rate doctors after a completed consultation.
- [ ] **Advanced Search & Filtering:** Search doctors by specialization, available date, and consultation type from a single endpoint.
