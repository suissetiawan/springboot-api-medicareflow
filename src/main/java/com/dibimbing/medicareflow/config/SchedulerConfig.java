package com.dibimbing.medicareflow.config;

import org.springframework.scheduling.annotation.Scheduled;

import com.dibimbing.medicareflow.service.SlotGeneratorService;

import lombok.RequiredArgsConstructor;



@RequiredArgsConstructor
public class SchedulerConfig {

    private final SlotGeneratorService slotGeneratorService;

    @Scheduled(cron = "${app.slot.generation-cron}")
    public void generateSlotTime() {
        slotGeneratorService.autoGenerateSlots();
    }

}
