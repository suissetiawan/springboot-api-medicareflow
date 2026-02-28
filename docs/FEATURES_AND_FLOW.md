# ✨ Core Features & Application Flow

## Fitur Utama Sistem

- **Role-Based Access Control (RBAC):** Distinct access levels for `ADMIN`, `DOCTOR`, and `PATIENT` using JWT strategy.
- **Automated Scheduling Engine:** Automatically generates available appointment slots based on a doctor's working schedule and predefined consultation type durations.
- **Robust Security & Auth:** JWT-based authentication with Redis-backed token blacklisting for secure and immediate logout capabilities.
- **Concurrency & Constraints:** Unique compound constraints on time slots (`doctor_id`, `slot_date`, `start_time`) to prevent double-booking.
- **State Machine for Booking:** Slot booking driven by status transitions (`AVAILABLE` ➔ `BOOKED` ➔ `BLOCKED`).
- **Data Integrity:** UUIDs for primary keys to prevent enumeration attacks and Soft Deletion implemented across core entities to preserve historical data.
- **Comprehensive API Documentation:** Integrated Swagger/OpenAPI for easy exploration and testing.

---

## 🚦 Example Usage Flow (Alur Booking)

1. **Setup (Admin):** Admin creates a `Doctor` profile and sets up a `ConsultationType` (e.g., 30 mins).
2. **Schedule (Admin/Doctor):** A `WorkSchedule` is defined for the Doctor (e.g., Monday 09:00-12:00).
3. **Generation:** The Slot Generator creates `AVAILABLE` time slots based on the schedule.
4. **Browse (Patient):** Patient queries `GET /api/slot-time` to see available slots for a specific doctor and date.
5. **Book (Patient):** Patient sends a `POST /api/appointments` with the chosen `slot_id`.
6. **Confirmation:** The system verifies the slot is still `AVAILABLE`, creates the `Appointment` record, and automatically updates the time slot status to `BOOKED`.
