-- =====================================
-- DATABASE
-- =====================================
CREATE DATABASE IF NOT EXISTS medicare_flow
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE medicare_flow;

-- =====================================
-- USER ACCOUNT
-- =====================================
CREATE TABLE user_account (
  id BINARY(16) PRIMARY KEY,
  username VARCHAR(100) UNIQUE,
  email VARCHAR(150) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role ENUM('ADMIN','PATIENT','DOCTOR') NOT NULL,
  is_active BOOLEAN DEFAULT TRUE,
  deleted_at TIMESTAMP NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  INDEX idx_user_role (role),
  INDEX idx_user_active (is_active),
  INDEX idx_user_deleted (deleted_at)
) ENGINE=InnoDB;

-- =====================================
-- PATIENT
-- =====================================
CREATE TABLE patient (
  id BINARY(16) PRIMARY KEY,
  user_id BINARY(16) UNIQUE NOT NULL,
  name VARCHAR(150) NOT NULL,
  phone VARCHAR(30),
  deleted_at TIMESTAMP NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_patient_user
    FOREIGN KEY (user_id) REFERENCES user_account(id),

  INDEX idx_patient_deleted (deleted_at)
) ENGINE=InnoDB;

-- =====================================
-- DOCTOR
-- =====================================
CREATE TABLE doctor (
  id BINARY(16) PRIMARY KEY,
  user_id BINARY(16) UNIQUE NOT NULL,
  name VARCHAR(150) NOT NULL,
  specialization VARCHAR(150),
  status ENUM('ACTIVE','INACTIVE','ON_LEAVE') NOT NULL,
  deleted_at TIMESTAMP NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_doctor_user
    FOREIGN KEY (user_id) REFERENCES user_account(id),

  INDEX idx_doctor_status (status),
  INDEX idx_doctor_specialization (specialization),
  INDEX idx_doctor_deleted (deleted_at)
) ENGINE=InnoDB;

-- =====================================
-- CONSULTATION SERVICE
-- =====================================
CREATE TABLE consultation_service (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  duration_minutes INT NOT NULL,
  fee DECIMAL(12,2) NOT NULL,
  is_active BOOLEAN DEFAULT TRUE,
  deleted_at TIMESTAMP NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  INDEX idx_service_is_active (is_active),
  INDEX idx_service_deleted (deleted_at)
) ENGINE=InnoDB;

-- =====================================
-- DOCTOR SERVICE (MANY TO MANY)
-- =====================================
CREATE TABLE doctor_service (
  doctor_id BINARY(16) NOT NULL,
  service_id BIGINT NOT NULL,

  PRIMARY KEY (doctor_id, service_id),

  CONSTRAINT fk_ds_doctor
    FOREIGN KEY (doctor_id) REFERENCES doctor(id),

  CONSTRAINT fk_ds_service
    FOREIGN KEY (service_id) REFERENCES consultation_service(id)
) ENGINE=InnoDB;

-- =====================================
-- WORKING SCHEDULE
-- =====================================
CREATE TABLE working_schedule (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  doctor_id BINARY(16) NOT NULL,
  day_of_week ENUM(
    'MONDAY','TUESDAY','WEDNESDAY',
    'THURSDAY','FRIDAY','SATURDAY','SUNDAY'
  ) NOT NULL,
  start_time TIME NOT NULL,
  end_time TIME NOT NULL,
  deleted_at TIMESTAMP NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_ws_doctor
    FOREIGN KEY (doctor_id) REFERENCES doctor(id),

  INDEX idx_ws_doctor_day (doctor_id, day_of_week)
) ENGINE=InnoDB;

-- =====================================
-- TIME SLOT
-- =====================================
CREATE TABLE time_slot (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  doctor_id BINARY(16) NOT NULL,
  slot_date DATE NOT NULL,
  start_time TIME NOT NULL,
  end_time TIME NOT NULL,
  status ENUM('AVAILABLE','BOOKED','BLOCKED') NOT NULL,
  deleted_at TIMESTAMP NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_slot_doctor
    FOREIGN KEY (doctor_id) REFERENCES doctor(id),

  INDEX idx_slot_doctor_date (doctor_id, slot_date),
  INDEX idx_slot_availability (doctor_id, slot_date, status),
  INDEX idx_slot_status (status),
  INDEX idx_slot_deleted (deleted_at)
) ENGINE=InnoDB;

-- =====================================
-- APPOINTMENT
-- =====================================
CREATE TABLE appointment (
  id BINARY(16) PRIMARY KEY,
  patient_id BINARY(16) NOT NULL,
  doctor_id BINARY(16) NOT NULL,
  service_id BIGINT NOT NULL,
  time_slot_id BIGINT UNIQUE NOT NULL,
  status ENUM(
    'PENDING','CONFIRMED','COMPLETED',
    'CANCELLED','NO_SHOW'
  ) NOT NULL,
  booked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_appt_patient
    FOREIGN KEY (patient_id) REFERENCES patient(id),

  CONSTRAINT fk_appt_doctor
    FOREIGN KEY (doctor_id) REFERENCES doctor(id),

  CONSTRAINT fk_appt_service
    FOREIGN KEY (service_id) REFERENCES consultation_service(id),

  CONSTRAINT fk_appt_slot
    FOREIGN KEY (time_slot_id) REFERENCES time_slot(id),

  INDEX idx_appt_patient (patient_id),
  INDEX idx_appt_doctor (doctor_id),
  INDEX idx_appt_status (status),
  INDEX idx_appt_booked (booked_at),
  INDEX idx_appt_deleted (deleted_at)
) ENGINE=InnoDB;

-- =====================================
-- CONSULTATION RECORD
-- =====================================
CREATE TABLE consultation_record (
  id BINARY(16) PRIMARY KEY,
  appointment_id BINARY(16) UNIQUE NOT NULL,
  summary TEXT,
  recommendation TEXT,
  follow_up_date DATE,
  deleted_at TIMESTAMP NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_record_appointment
    FOREIGN KEY (appointment_id) REFERENCES appointment(id),

  INDEX idx_record_followup (follow_up_date)
) ENGINE=InnoDB;