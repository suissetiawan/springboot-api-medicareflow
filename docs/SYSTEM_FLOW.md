# 📊 MediCareFlow System Flowchart

This document provides a visual representation of the core business logic and data flow within the MediCareFlow system.

---

## 🚦 End-to-End System Flow

The following diagram illustrates the complete lifecycle of a medical consultation, from system configuration to automated status management.

```mermaid
graph TD
    %% Roles
    subgraph Roles
        Admin["👤 Admin"]
        Doctor["🩺 Doctor"]
        Patient["👤 Patient"]
        System["🤖 System (Scheduler)"]
    end

    %% Admin Setup Phase
    Start((Start)) --> Admin
    Admin --> CreateDoc[Create Doctor Account]
    Admin --> CreateType[Define Consultation Types]
    Admin --> CreateSched[Register Work Schedule]

    CreateDoc --> DocTable[(Doctor Table)]
    CreateType --> TypeTable[(ConsultationType Table)]
    CreateSched --> SchedTable[(WorkSchedule Table)]

    %% Slot Generation
    SchedTable --> Gen[Slot Generator Scheduler]
    TypeTable --> Gen
    Gen --> |Automatic - 7 Days| Slots[(TimeSlot Table)]

    %% Patient Flow
    Patient --> Browse[Browse Available Slots]
    Slots --> Browse
    Browse --> Book[Post /api/appointments]
    Book --> |Status: PENDING| Appt[(Appointment Table)]
    Book --> |Mark: BOOKED| Slots

    %% Appointment Management
    Appt --> Confirm{Admin Confirm?}
    Admin --> |PATCH /status| Confirm
    Confirm --> |No| Cancel[Status: CANCELLED]
    Confirm --> |Yes| Confirmed[Status: CONFIRMED]

    %% Consultation
    Confirmed --> Consult[Consultation Happens]
    Consult --> Record[Doctor Creates Record]
    Doctor --> |POST /records| Record
    Record --> |Auto Update| Completed[Status: COMPLETED]
    Completed --> Appt

    %% Automation
    Confirmed --> |Time Passed?| System
    System --> |Check Candidates| NoShow[Auto Mark: NO_SHOW]
    NoShow --> Appt

    %% Final States
    Cancel --> End((End))
    Completed --> End
    NoShow --> End

    %% Styling
    classDef roles fill:#f9f,stroke:#333,stroke-width:2px;
    classDef table fill:#e1f5fe,stroke:#01579b,stroke-width:1px;
    classDef process fill:#fffde7,stroke:#fbc02d,stroke-width:1px;
    class Admin,Doctor,Patient,System roles;
    class DocTable,TypeTable,SchedTable,Slots,Appt table;
```

---

## 🔄 Key Process Descriptions

### 1. Automated Slot Generation

The system periodically reads the `WorkSchedule` (e.g., "Doctor John, Mondays 9-12") and the primary `ConsultationType` duration. It then slices the work window into individual `TimeSlot` records.

### 2. The Appointment State Machine

This is a strict lifecycle enforced by the `AppointmentService`:

- **PENDING**: Initial state upon booking.
- **CONFIRMED**: Approved by an administrator.
- **COMPLETED**: Automatically set when a `ConsultationRecord` is created.
- **CANCELLED**: Terminal state if the patient or admin rejects the booking.
- **NO_SHOW**: Terminal state if the appointment time passes without a record.

### 3. Automated Status Checks

To maintain data accuracy, a background **Scheduler** runs every 15 minutes to find `CONFIRMED` appointments whose `endTime` has passed the current server time and marks them as `NO_SHOW`.
