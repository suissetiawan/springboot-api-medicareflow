package com.dibimbing.medicareflow.controller;

import com.dibimbing.medicareflow.helper.ResponseHelper;
import com.dibimbing.medicareflow.service.SlotGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/generate-slot")
@RequiredArgsConstructor
public class SlotController {

    private final SlotGeneratorService slotGeneratorService;

    @PostMapping
    public ResponseEntity<?> triggerManualSlotGeneration() {
        slotGeneratorService.autoGenerateSlots();
        return ResponseHelper.successOK(null, "Successfully triggered manual slot generation for the next 7 days");
    }
}
