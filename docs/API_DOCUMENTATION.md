# 📖 API Documentation

Once the application is running, the interactive API documentation is automatically generated and served via **Swagger UI** (powered by SpringDoc OpenAPI 3).

- **Swagger UI:** `http://localhost:8080/docs/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8080/docs/api-docs`

> All endpoints (except public auth routes) require a valid **Bearer JWT token** in the `Authorization` header.

---

## 🔐 Authentication — `/api/auth`

| Method | Endpoint             | Access    | Description                                                            |
| ------ | -------------------- | --------- | ---------------------------------------------------------------------- |
| `POST` | `/api/auth/register` | Public\*  | Register a new PATIENT account. \*Admin only can register DOCTOR/ADMIN |
| `POST` | `/api/auth/login`    | Public    | Authenticate and receive a JWT token                                   |
| `POST` | `/api/auth/logout`   | All roles | Invalidate current JWT (token blacklisted in Redis)                    |
| `GET`  | `/api/auth/my`       | All roles | Retrieve the currently authenticated user's profile                    |

---

## 👥 User Management — `/api/users`

> 🔒 **ADMIN only**

| Method   | Endpoint                                       | Description                              |
| -------- | ---------------------------------------------- | ---------------------------------------- |
| `GET`    | `/api/users?role=ADMIN\|DOCTOR\|PATIENT`       | List all users, filtered by role enum    |
| `GET`    | `/api/users/{id}`                              | Get a specific user's details            |
| `PUT`    | `/api/users/{id}`                              | Update user profile data                 |
| `PUT`    | `/api/users/{id}/role`                         | Change a user's role                     |
| `DELETE` | `/api/users/{id}`                              | Soft-delete a user                       |
| `GET`    | `/api/users/deleted`                           | List all soft-deleted users              |
| `PUT`    | `/api/users/{id}/restore`                      | Restore a soft-deleted user              |
| `POST`   | `/api/doctors/{username}/services/{serviceId}` | Assign a consultation type to a doctor   |
| `DELETE` | `/api/doctors/{username}/services/{serviceId}` | Remove a consultation type from a doctor |

---

## 🩺 Consultation Types — `/api/consultationtypes`

| Method  | Endpoint                                   | Access                 | Description                                         |
| ------- | ------------------------------------------ | ---------------------- | --------------------------------------------------- |
| `GET`   | `/api/consultationtypes`                   | ADMIN                  | List all consultation types (paginated)             |
| `GET`   | `/api/consultationtypes/{id}`              | ADMIN                  | Get a specific consultation type                    |
| `GET`   | `/api/consultationtypes/doctor/{username}` | ADMIN, DOCTOR, PATIENT | Get consultation types offered by a specific doctor |
| `POST`  | `/api/consultationtypes`                   | ADMIN                  | Create a new consultation type                      |
| `PUT`   | `/api/consultationtypes/{id}`              | ADMIN                  | Update a consultation type                          |
| `PATCH` | `/api/consultationtypes/{id}/status`       | ADMIN                  | Activate or deactivate a consultation type          |

---

## 📅 Work Schedule — `/api/work-schedule`

> 🔒 **ADMIN only**

| Method   | Endpoint                                  | Description                             |
| -------- | ----------------------------------------- | --------------------------------------- |
| `POST`   | `/api/work-schedule`                      | Create a new work schedule for a doctor |
| `GET`    | `/api/work-schedule?username=&dayofweek=` | List all work schedules (filterable)    |
| `PUT`    | `/api/work-schedule/{id}`                 | Update a work schedule                  |
| `DELETE` | `/api/work-schedule/{id}`                 | Soft-delete a work schedule             |
| `GET`    | `/api/work-schedule/deleted`              | List all soft-deleted work schedules    |
| `PUT`    | `/api/work-schedule/{id}/restore`         | Restore a soft-deleted work schedule    |

---

## 🕐 Time Slots — `/api/slot-time`

> 🔒 **ADMIN only**

| Method  | Endpoint                                                | Description                                          |
| ------- | ------------------------------------------------------- | ---------------------------------------------------- |
| `GET`   | `/api/slot-time?username=&slotDate=&status=&dayOfWeek=` | List time slots with optional filters                |
| `POST`  | `/api/slot-time/generate`                               | Manually trigger slot generation for the next 7 days |
| `PATCH` | `/api/slot-time/{id}/block`                             | Block a specific time slot                           |

**Slot Status Values:** `AVAILABLE`, `BOOKED`, `BLOCKED`

---

## 📋 Appointments — `/api/appointments`

| Method  | Endpoint                        | Access          | Description                                    |
| ------- | ------------------------------- | --------------- | ---------------------------------------------- |
| `POST`  | `/api/appointments`             | PATIENT         | Book a new appointment using a slot ID         |
| `GET`   | `/api/appointments`             | ADMIN           | List all appointments (paginated)              |
| `GET`   | `/api/appointments/my`          | PATIENT, DOCTOR | List the authenticated user's own appointments |
| `PATCH` | `/api/appointments/{id}/status` | ADMIN           | Update status (follows state machine rules)    |

**Appointment Status & Transitions:**

- `PENDING` → `CONFIRMED` or `CANCELLED`.
- `CONFIRMED` → `COMPLETED` (via Record), `CANCELLED`, or `NO_SHOW` (System).
- **Terminal States:** `COMPLETED`, `CANCELLED`, `NO_SHOW` cannot be changed.

---

## 📝 Consultation Records — `/api/appointments`

| Method | Endpoint                                    | Access                 | Description                                                     |
| ------ | ------------------------------------------- | ---------------------- | --------------------------------------------------------------- |
| `POST` | `/api/appointments/{appointmentId}/records` | ADMIN, DOCTOR          | Create a consultation record for a completed appointment        |
| `GET`  | `/api/appointments/{appointmentId}/records` | ADMIN, DOCTOR, PATIENT | Retrieve the consultation record for a specific appointment     |
| `GET`  | `/api/appointments/records/my`              | DOCTOR, PATIENT        | Retrieve all consultation records belonging to the current user |

---

## 📌 Typical Usage Flow

```
1. POST /api/auth/login                              → Obtain JWT token
2. GET  /api/slot-time?username=dr_john&slotDate=2026-03-10  → Browse available slots
3. POST /api/appointments                            → Book a slot (Patient)
4. PATCH /api/appointments/{id}/status?status=CONFIRMED      → Doctor confirms
5. POST /api/appointments/{id}/records               → Doctor creates consultation record
6. GET  /api/appointments/records/my                 → Patient reviews their record
```
