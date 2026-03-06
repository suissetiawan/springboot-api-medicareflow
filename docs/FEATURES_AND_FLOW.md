# ✨ Core Features & Application Flow

## Core Features

- **Role-Based Access Control (RBAC):** Three distinct access levels — `ADMIN`, `DOCTOR`, and `PATIENT` — enforced via Spring Security and JWT-based authentication.
- **Automated Scheduling Engine:** The system automatically generates available time slots based on a doctor's registered work schedule and the duration defined in each consultation type. Slots are generated for the next 7 days on demand or via a scheduled job.
- **Robust Security & Auth:** JWT authentication using `jjwt` (v0.11.5) with Redis-backed token blacklisting, enabling immediate and secure logout.
- **Concurrency Protection:** Unique compound constraints on time slots (`doctor_id`, `slot_date`, `start_time`) prevent double-booking at the database level.
- **Appointment State Machine:** Time slot status transitions are strictly managed (`AVAILABLE` → `BOOKED` → `BLOCKED`), and appointment status follows a defined lifecycle (`PENDING` → `CONFIRMED` → `COMPLETED` / `CANCELLED` / `NO_SHOW`).
- **Data Integrity:** UUIDs are used as primary keys for core user entities to prevent enumeration attacks. Soft deletion (via `deleted_at` timestamps) is implemented across core entities to preserve historical data.
- **Unique Reference Numbers:** Every appointment is assigned a unique `referenceNumber` for external identification and audit purposes.
- **Comprehensive API Documentation:** Integrated Swagger UI (SpringDoc OpenAPI 3) available at `/docs/swagger-ui.html`.

---

## 🚦 Standard Usage Flow (Appointment Booking)

```
1. Admin Setup
   └─ Admin creates a Doctor profile and defines a ConsultationType
      (e.g., "General Consultation" with a 30-minute duration).

2. Schedule Configuration (Admin)
   └─ Admin registers a WorkSchedule for the doctor
      (e.g., every Monday, 09:00–12:00).

3. Slot Generation (Automated / Manual)
   └─ The Slot Generator creates individual AVAILABLE time slots
      based on the work schedule and consultation type duration.

4. Browse (Patient)
   └─ Patient calls GET /api/slot-time?username=dr_john&slotDate=2026-03-10
      to view available slots.

5. Book (Patient)
   └─ Patient sends POST /api/appointments with the chosen slot_id.
      The system verifies the slot is still AVAILABLE, creates the
      Appointment record with status PENDING, and marks the slot as BOOKED.

6. Confirmation (Doctor / Admin)
   └─ Doctor updates appointment status to CONFIRMED via
      PATCH /api/appointments/{id}/status.

7. Consultation & Record (Doctor)
   └─ After the consultation, the doctor creates a consultation record
      via POST /api/appointments/{id}/records.

8. Review (Patient)
   └─ Patient reviews their consultation history via
      GET /api/appointments/records/my.
```
