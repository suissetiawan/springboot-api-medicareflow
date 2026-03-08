# 🗄️ Database Design Overview

## Design Principles

The database schema is designed with the following principles in mind:

- **High Normalization:** Data is separated into dedicated tables to avoid redundancy and ensure consistency.
- **UUID Primary Keys:** User-facing entities (`user_account`, `patient`, `doctor`) use `UUID` as primary keys to prevent sequential ID enumeration attacks.
- **Auto-Increment Long IDs:** Operational entities (`appointment`, `time_slot`, `work_schedule`, `consultation_type`, `consultation_record`) use auto-incremented `LONG` (BIGINT) primary keys for performance efficiency.
- **Soft Deletion:** Core entities include a `deleted_at` timestamp column. Records are never physically deleted, preserving full audit trails.
- **Audit Timestamps:** All entities inherit `created_at` and `updated_at` columns from base entity classes.

---

## Core Entities

| Entity             | Table Name            | PK Type | Soft Delete |
| ------------------ | --------------------- | ------- | ----------- |
| UserAccount        | `user_account`        | UUID    | ✅          |
| Patient            | `patient`             | UUID    | ✅          |
| Doctor             | `doctor`              | UUID    | ✅          |
| ConsultationType   | `consultation_type`   | Long    | ❌          |
| WorkSchedule       | `work_schedule`       | Long    | ✅          |
| TimeSlot           | `time_slot`           | Long    | ❌          |
| Appointment        | `appointment`         | Long    | ❌          |
| ConsultationRecord | `consultation_record` | Long    | ❌          |

---

## Key Relationships

```
UserAccount (1) ──── (1) Patient
UserAccount (1) ──── (1) Doctor

Doctor (1) ──── (N) WorkSchedule
Doctor (1) ──── (N) ConsultationType
Doctor (1) ──── (N) TimeSlot

WorkSchedule / ConsultationType ──── (generates) ──── TimeSlot

Patient (1) ──── (N) Appointment
Doctor  (1) ──── (N) Appointment
TimeSlot (1) ──── (1) Appointment
ConsultationType (1) ──── (N) Appointment

Appointment (1) ──── (1) ConsultationRecord
```

---

## 🔗 Entity Relationship Diagram (ERD)

For a full visual representation of the schema:

🔗 **[View the Complete ERD on dbdiagram.io](https://dbdiagram.io/d/MediCareFlow-ERD-699333acbd82f5fce2d8e455)**
