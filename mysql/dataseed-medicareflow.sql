USE medicare_flow;

-- =====================================
-- USER ACCOUNT
-- =====================================

-- ADMIN (5)
INSERT INTO user_account VALUES
(UUID_TO_BIN(UUID()),'admin_arif','arif@medicare.com','password','ADMIN',1,NULL,NOW(),NOW()),
(UUID_TO_BIN(UUID()),'admin_budi','budi@medicare.com','password','ADMIN',1,NULL,NOW(),NOW()),
(UUID_TO_BIN(UUID()),'admin_citra','citra@medicare.com','password','ADMIN',1,NULL,NOW(),NOW()),
(UUID_TO_BIN(UUID()),'admin_dewi','dewi@medicare.com','password','ADMIN',1,NULL,NOW(),NOW()),
(UUID_TO_BIN(UUID()),'admin_eko','eko@medicare.com','password','ADMIN',1,NULL,NOW(),NOW());

-- PATIENT USER (5)
INSERT INTO user_account VALUES
(UUID_TO_BIN(UUID()),'andi','andi@mail.com','password','PATIENT',1,NULL,NOW(),NOW()),
(UUID_TO_BIN(UUID()),'bella','bella@mail.com','password','PATIENT',1,NULL,NOW(),NOW()),
(UUID_TO_BIN(UUID()),'cahyo','cahyo@mail.com','password','PATIENT',1,NULL,NOW(),NOW()),
(UUID_TO_BIN(UUID()),'dina','dina@mail.com','password','PATIENT',1,NULL,NOW(),NOW()),
(UUID_TO_BIN(UUID()),'farhan','farhan@mail.com','password','PATIENT',1,NULL,NOW(),NOW());

-- DOCTOR USER (5)
INSERT INTO user_account VALUES
(UUID_TO_BIN(UUID()),'dr_sari','sari@medicare.com','password','DOCTOR',1,NULL,NOW(),NOW()),
(UUID_TO_BIN(UUID()),'dr_rizal','rizal@medicare.com','password','DOCTOR',1,NULL,NOW(),NOW()),
(UUID_TO_BIN(UUID()),'dr_lina','lina@medicare.com','password','DOCTOR',1,NULL,NOW(),NOW()),
(UUID_TO_BIN(UUID()),'dr_yusuf','yusuf@medicare.com','password','DOCTOR',1,NULL,NOW(),NOW()),
(UUID_TO_BIN(UUID()),'dr_nina','nina@medicare.com','password','DOCTOR',1,NULL,NOW(),NOW());

-- =====================================
-- PATIENT PROFILE
-- =====================================
INSERT INTO patient
SELECT UUID_TO_BIN(UUID()), id,
CASE username
WHEN 'andi' THEN 'Andi Saputra'
WHEN 'bella' THEN 'Bella Maharani'
WHEN 'cahyo' THEN 'Cahyo Pratama'
WHEN 'dina' THEN 'Dina Lestari'
WHEN 'farhan' THEN 'Farhan Hidayat'
END,
'081234567890', NULL, NOW(), NOW()
FROM user_account WHERE role='PATIENT';

-- =====================================
-- DOCTOR PROFILE
-- =====================================
INSERT INTO doctor
SELECT UUID_TO_BIN(UUID()), id,
CASE username
WHEN 'dr_sari' THEN 'dr. Sari'
WHEN 'dr_rizal' THEN 'dr. Rizal'
WHEN 'dr_lina' THEN 'dr. Lina'
WHEN 'dr_yusuf' THEN 'dr. Yusuf'
WHEN 'dr_nina' THEN 'dr. Nina'
END,
CASE username
WHEN 'dr_sari' THEN 'Umum'
WHEN 'dr_rizal' THEN 'Anak'
WHEN 'dr_lina' THEN 'Gigi'
WHEN 'dr_yusuf' THEN 'Kulit'
WHEN 'dr_nina' THEN 'Jantung'
END,
'ACTIVE', NULL, NOW(), NOW()
FROM user_account WHERE role='DOCTOR';

-- =====================================
-- CONSULTATION SERVICE (3)
-- =====================================
INSERT INTO consultation_type (name,duration_minutes,fee,active,created_at,updated_at) VALUES
('Konsultasi Umum',30,150000,1,NOW(),NOW()),
('Konsultasi Spesialis',45,300000,1,NOW(),NOW()),
('Kontrol Rutin',20,100000,1,NOW(),NOW());

-- =====================================
-- DOCTOR SERVICE RELATION
-- =====================================
INSERT INTO doctor_service
SELECT d.id, s.id
FROM doctor d, consultation_type s
WHERE d.specialization='Umum'
AND s.name IN ('Konsultasi Umum','Kontrol Rutin');

INSERT INTO doctor_service
SELECT d.id, s.id
FROM doctor d, consultation_type s
WHERE d.specialization<>'Umum'
AND s.name IN ('Konsultasi Spesialis','Kontrol Rutin');

-- =====================================
-- WORKING SCHEDULE
-- weekday + weekend
-- =====================================
INSERT INTO working_schedule (doctor_id,day_of_week,start_time,end_time,created_at,updated_at)
SELECT id,'MONDAY','08:00','12:00',NOW(),NOW() FROM doctor;

INSERT INTO working_schedule (doctor_id,day_of_week,start_time,end_time,created_at,updated_at)
SELECT id,'WEDNESDAY','08:00','12:00',NOW(),NOW() FROM doctor;

INSERT INTO working_schedule (doctor_id,day_of_week,start_time,end_time,created_at,updated_at)
SELECT id,'SATURDAY','09:00','13:00',NOW(),NOW() FROM doctor;

-- =====================================
-- TIME SLOT
-- =====================================
INSERT INTO time_slot (doctor_id,slot_date,start_time,end_time,status,created_at,updated_at)
SELECT id,CURDATE(),'09:00','09:30','AVAILABLE',NOW(),NOW() FROM doctor;

INSERT INTO time_slot (doctor_id,slot_date,start_time,end_time,status,created_at,updated_at)
SELECT id,CURDATE(),'10:00','10:30','BLOCKED',NOW(),NOW() FROM doctor;

INSERT INTO time_slot (doctor_id,slot_date,start_time,end_time,status,created_at,updated_at)
SELECT id,CURDATE(),'11:00','11:30','AVAILABLE',NOW(),NOW() FROM doctor;

-- =====================================
-- APPOINTMENT (3 BOOKING SAJA)
-- =====================================
INSERT INTO appointment (
  id, patient_id, doctor_id, service_id, time_slot_id,
  status, booked_at, created_at, updated_at
)
SELECT
  UUID_TO_BIN(UUID()),
  p.id,
  d.id,
  s.id,
  ts.id,
  'CONFIRMED',
  NOW(), NOW(), NOW()
FROM patient p
JOIN doctor d
JOIN time_slot ts ON ts.doctor_id = d.id
JOIN doctor_service ds ON ds.doctor_id = d.id
JOIN consultation_type s ON s.id = ds.service_id
WHERE ts.status='AVAILABLE'
LIMIT 3;

-- update slot yang dipakai
UPDATE time_slot ts
JOIN appointment a ON a.time_slot_id = ts.id
SET ts.status='BOOKED';

-- =====================================
-- CONSULTATION RECORD (2 DATA)
-- =====================================
INSERT INTO consultation_record (
  id,
  appointment_id,
  summary,
  recommendation,
  follow_up_date,
  created_at,
  updated_at
)
SELECT
  UUID_TO_BIN(UUID()),
  id,
  'Pasien stabil',
  'Kontrol 2 minggu',
  DATE_ADD(CURDATE(), INTERVAL 14 DAY),
  NOW(),
  NOW()
FROM appointment
LIMIT 2;