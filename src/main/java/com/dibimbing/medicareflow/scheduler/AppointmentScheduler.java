package com.dibimbing.medicareflow.scheduler;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dibimbing.medicareflow.entity.Appointment;
import com.dibimbing.medicareflow.enums.AppointmentStatus;
import com.dibimbing.medicareflow.repository.AppointmentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppointmentScheduler {

    private final AppointmentRepository appointmentRepository;

    /**
     * ubah status appointment menjadi NO_SHOW jika sudah lewat waktu.
     * berjalan setiap 15 menit.
     */
    @Scheduled(cron = "0 0/15 * * * *")
    @Transactional
    public void autoMarkNoShow() {
        log.info("Running automatic NO_SHOW check...");
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        List<Appointment> candidates = appointmentRepository.findNoShowCandidates(today, now);
        
        if (!candidates.isEmpty()) {
            log.info("Found {} appointments to mark as NO_SHOW", candidates.size());
            for (Appointment appointment : candidates) {
                appointment.setStatus(AppointmentStatus.NO_SHOW);
                appointmentRepository.save(appointment);
                log.info("Appointment {} marked as NO_SHOW", appointment.getReferenceNumber());
            }
        } else {
            log.info("No appointments found to mark as NO_SHOW");
        }
        log.info("Automatic NO_SHOW check completed");
    }
}
