# 📖 API Documentation & Key Endpoints

Once the application is running, the interactive API documentation is automatically generated and beautifully presented via Swagger UI.

- **Swagger UI:** `http://localhost:8080/docs/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8080/docs/api-docs`

---

## 📌 Key Endpoints Example

Below are some essential endpoints used in the standard flow:

- `POST /api/auth/login` - Authenticate and receive JWT.
- `GET /api/slot-time?username=dr_smith&slotDate=2026-03-05` - Retrieve generated time slots for a specific doctor and date.
- `POST /api/appointments` - Book a specific time slot using the slot's ID.
- `GET /api/appointments/my` - Get the logged-in user's appointments (Patient viewing their own).
- `PATCH /api/appointments/{id}/status` - Doctor/Admin updates an appointment status (e.g., to `COMPLETED`).
