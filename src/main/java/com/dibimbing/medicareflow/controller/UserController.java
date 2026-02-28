package com.dibimbing.medicareflow.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dibimbing.medicareflow.dto.PaginationMeta;
import com.dibimbing.medicareflow.dto.response.UserResponse;
import com.dibimbing.medicareflow.helper.ResponseHelper;
import com.dibimbing.medicareflow.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "") String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
            
        String userType = " users";
        switch (type.toLowerCase()) {
            case "admin":
                userType = " admins";
                break;
            case "doctor":
                userType = " doctors";
                break;
            case "patient":
                userType = " patients";
                break;
            default:
                if (type.isEmpty()) {
                    userType = " users";
                } else {
                    return ResponseHelper.error("Invalid type", HttpStatus.BAD_REQUEST);
                }
                break;
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponse> result = userService.getAll(type.toLowerCase(), pageable);

        PaginationMeta meta = PaginationMeta.builder()
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();

        return ResponseHelper.successOK(result.getContent(), "Success get all" + userType, meta);
    }
}
