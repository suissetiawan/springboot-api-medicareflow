# Role & Permission Documentation

> This document describes each role available in the **MedicareFlow** system and the actions each role is permitted to perform.

---

## Available Roles

The MedicareFlow system has **3** roles:

| Role      | Description                                                                                                     |
| --------- | --------------------------------------------------------------------------------------------------------------- |
| `ADMIN`   | System administrator with full access to all management features.                                               |
| `DOCTOR`  | Medical professional providing consultation services. Can manage appointments and patient consultation records. |
| `PATIENT` | End user of the platform. Can book appointments and view their own consultation history.                        |

---

## 1. ADMIN

### Description

The `ADMIN` role is the highest-level manager in the system. Admins are responsible for managing all users, doctors, patients, doctor work schedules, consultation types, and time slots. Admins can monitor all activity across the platform.

### Permissions

#### 🔐 Authentication (Public)

| Action              | Endpoint                  | Access    |
| ------------------- | ------------------------- | --------- |
| Register an account | `POST /api/auth/register` | Public    |
| Login               | `POST /api/auth/login`    | Public    |
| Logout              | `POST /api/auth/logout`   | All roles |
| View own profile    | `GET /api/auth/my`        | All roles |

---

#### 👥 User Management (`/api/users`)

| Action                          | Endpoint                                     | Method   |
| ------------------------------- | -------------------------------------------- | -------- |
| List all users (filter by type) | `GET /api/users?type=admin\|doctor\|patient` | `GET`    |
| Get user details                | `GET /api/users/{id}`                        | `GET`    |
| Update user profile             | `PUT /api/users/{id}`                        | `PUT`    |
| Update user role                | `PUT /api/users/{id}/role`                   | `PUT`    |
| Soft-delete a user              | `DELETE /api/users/{id}`                     | `DELETE` |
| List all soft-deleted users     | `GET /api/users/deleted`                     | `GET`    |
| Restore a deleted user          | `PUT /api/users/{id}/restore`                | `PUT`    |

> ⚠️ Endpoints under `/api/users/**`, `/api/doctors/**`, and `/api/patients/**` are **ADMIN only**.

---

#### 📅 Doctor Work Schedule Management (`/api/work-schedule`)

| Action                                           | Endpoint                              | Method   |
| ------------------------------------------------ | ------------------------------------- | -------- |
| Create a work schedule                           | `POST /api/work-schedule`             | `POST`   |
| List all work schedules (filter by username/day) | `GET /api/work-schedule`              | `GET`    |
| Update a work schedule                           | `PUT /api/work-schedule/{id}`         | `PUT`    |
| Delete a work schedule                           | `DELETE /api/work-schedule/{id}`      | `DELETE` |
| List deleted work schedules                      | `GET /api/work-schedule/deleted`      | `GET`    |
| Restore a deleted work schedule                  | `PUT /api/work-schedule/{id}/restore` | `PUT`    |

---

#### 🕐 Time Slot Management (`/api/slot-time`)

| Action                                                   | Endpoint                          | Method  |
| -------------------------------------------------------- | --------------------------------- | ------- |
| List all time slots (filter by username/date/status/day) | `GET /api/slot-time`              | `GET`   |
| Manually generate slots for the next 7 days              | `POST /api/slot-time/generate`    | `POST`  |
| Block a specific time slot                               | `PATCH /api/slot-time/{id}/block` | `PATCH` |

---

#### 🩺 Consultation Type Management (`/api/consultationtypes`)

| Action                           | Endpoint                                       | Method  |
| -------------------------------- | ---------------------------------------------- | ------- |
| List all consultation types      | `GET /api/consultationtypes`                   | `GET`   |
| Get consultation type details    | `GET /api/consultationtypes/{id}`              | `GET`   |
| Get consultation types by doctor | `GET /api/consultationtypes/doctor/{username}` | `GET`   |
| Create a consultation type       | `POST /api/consultationtypes`                  | `POST`  |
| Update a consultation type       | `PUT /api/consultationtypes/{id}`              | `PUT`   |
| Update consultation type status  | `PATCH /api/consultationtypes/{id}/status`     | `PATCH` |

> ⚠️ CRUD operations on consultation types are **ADMIN only**. The `GET /doctor/{username}` endpoint is accessible to all roles.

---

#### 📋 Appointment Management (`/api/appointments`)

| Action                                      | Endpoint                              | Method  |
| ------------------------------------------- | ------------------------------------- | ------- |
| List all appointments                       | `GET /api/appointments`               | `GET`   |
| Update appointment status                   | `PATCH /api/appointments/{id}/status` | `PATCH` |
| View consultation record for an appointment | `GET /api/appointments/{id}/records`  | `GET`   |
| Create a consultation record                | `POST /api/appointments/{id}/records` | `POST`  |

---

## 2. DOCTOR

### Description

The `DOCTOR` role represents medical professionals providing consultation services. Doctors can view appointments assigned to them, update appointment statuses, create and view consultation records, and review their own consultation history.

### Permissions

#### 🔐 Authentication

| Action           | Endpoint                | Access    |
| ---------------- | ----------------------- | --------- |
| Login            | `POST /api/auth/login`  | Public    |
| Logout           | `POST /api/auth/logout` | All roles |
| View own profile | `GET /api/auth/my`      | All roles |

---

#### 📋 Appointment Management (`/api/appointments`)

| Action                                      | Endpoint                              | Method  |
| ------------------------------------------- | ------------------------------------- | ------- |
| View my own appointments                    | `GET /api/appointments/my`            | `GET`   |
| Update appointment status                   | `PATCH /api/appointments/{id}/status` | `PATCH` |
| View consultation record for an appointment | `GET /api/appointments/{id}/records`  | `GET`   |
| Create a consultation record                | `POST /api/appointments/{id}/records` | `POST`  |

**Available appointment status values:**

- `PENDING` — awaiting confirmation
- `CONFIRMED` — appointment confirmed
- `COMPLETED` — consultation has been completed
- `CANCELLED` — appointment was cancelled
- `NO_SHOW` — patient did not attend

---

#### 📝 Consultation Records

| Action                                 | Endpoint                              | Method |
| -------------------------------------- | ------------------------------------- | ------ |
| View my own consultation records       | `GET /api/appointments/records/my`    | `GET`  |
| View record for a specific appointment | `GET /api/appointments/{id}/records`  | `GET`  |
| Create a new consultation record       | `POST /api/appointments/{id}/records` | `POST` |

---

#### 🩺 Consultation Types

| Action                            | Endpoint                                       | Method |
| --------------------------------- | ---------------------------------------------- | ------ |
| View consultation types by doctor | `GET /api/consultationtypes/doctor/{username}` | `GET`  |

---

> ❌ Doctors **cannot** access user management, work schedules, time slots, or consultation type CRUD operations.

---

## 3. PATIENT

### Description

The `PATIENT` role represents users who utilize the medical consultation service. Patients can register, book appointments with doctors, update their appointment status (within permitted limits), and view their consultation history and records.

### Permissions

#### 🔐 Authentication

| Action              | Endpoint                  | Access    |
| ------------------- | ------------------------- | --------- |
| Register an account | `POST /api/auth/register` | Public    |
| Login               | `POST /api/auth/login`    | Public    |
| Logout              | `POST /api/auth/logout`   | All roles |
| View own profile    | `GET /api/auth/my`        | All roles |

---

#### 📋 Appointment Management (`/api/appointments`)

| Action                                      | Endpoint                              | Method  |
| ------------------------------------------- | ------------------------------------- | ------- |
| Book a new appointment                      | `POST /api/appointments`              | `POST`  |
| View my own appointments                    | `GET /api/appointments/my`            | `GET`   |
| Update appointment status                   | `PATCH /api/appointments/{id}/status` | `PATCH` |
| View consultation record for an appointment | `GET /api/appointments/{id}/records`  | `GET`   |

> ℹ️ Patients may update appointment status (e.g., cancel via `CANCELLED`), but setting statuses such as `COMPLETED` or `NO_SHOW` is logically reserved for doctors and admins.

---

#### 📝 Consultation Records

| Action                                 | Endpoint                             | Method |
| -------------------------------------- | ------------------------------------ | ------ |
| View my own consultation records       | `GET /api/appointments/records/my`   | `GET`  |
| View record for a specific appointment | `GET /api/appointments/{id}/records` | `GET`  |

---

#### 🩺 Consultation Types

| Action                            | Endpoint                                       | Method |
| --------------------------------- | ---------------------------------------------- | ------ |
| View consultation types by doctor | `GET /api/consultationtypes/doctor/{username}` | `GET`  |

---

> ❌ Patients **cannot** create consultation records, or access user management, work schedules, time slots, or consultation type CRUD operations.

---

## Role Comparison Summary

| Feature / Action                  | ADMIN | DOCTOR | PATIENT |
| --------------------------------- | :---: | :----: | :-----: |
| Register & Login                  |  ✅   |   ✅   |   ✅    |
| View own profile                  |  ✅   |   ✅   |   ✅    |
| Manage all users (CRUD)           |  ✅   |   ❌   |   ❌    |
| Update user roles                 |  ✅   |   ❌   |   ❌    |
| Book an appointment               |  ❌   |   ❌   |   ✅    |
| View all appointments             |  ✅   |   ❌   |   ❌    |
| View own appointments             |  ❌   |   ✅   |   ✅    |
| Update appointment status         |  ✅   |   ✅   |   ✅    |
| Create consultation records       |  ✅   |   ✅   |   ❌    |
| View consultation records         |  ✅   |   ✅   |   ✅    |
| Manage consultation types (CRUD)  |  ✅   |   ❌   |   ❌    |
| View consultation types by doctor |  ✅   |   ✅   |   ✅    |
| Manage doctor work schedules      |  ✅   |   ❌   |   ❌    |
| Manage time slots                 |  ✅   |   ❌   |   ❌    |
| Generate time slots               |  ✅   |   ❌   |   ❌    |
| Block time slots                  |  ✅   |   ❌   |   ❌    |

---

_This document reflects the security configuration defined in `SecurityConfig.java` and the controller implementations of the current MedicareFlow system._
