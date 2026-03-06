package com.dibimbing.medicareflow.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dibimbing.medicareflow.dto.PaginationMeta;
import com.dibimbing.medicareflow.dto.request.RoleUpdateRequest;
import com.dibimbing.medicareflow.dto.request.UserUpdateRequest;
import com.dibimbing.medicareflow.dto.response.UserResponse;
import com.dibimbing.medicareflow.helper.ResponseHelper;
import com.dibimbing.medicareflow.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for managing users, including administrators, doctors, and patients.")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "") String type,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
            
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

        Page<UserResponse> result = userService.getAll(type.toLowerCase(), pageable);

        PaginationMeta meta = PaginationMeta.builder()
                .page(result.getNumber() + 1)
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();

        return ResponseHelper.successOK(result.getContent(), "Success get all" + userType, meta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return ResponseHelper.successOK(userService.getUserById(id), "Success get user detail");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody UserUpdateRequest req) {
        return ResponseHelper.successOK(userService.updateUser(id, req), "Success update user profile");
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<?> updateRole(@PathVariable UUID id, @RequestBody RoleUpdateRequest req) {
        return ResponseHelper.successOK(userService.updateRole(id, req), "Success update user role");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        Boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseHelper.error("User not found or already deleted", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/deleted")
    public ResponseEntity<?> getAllDeletedUser(
            @PageableDefault(sort = "deleted_at", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<UserResponse> result = userService.getAllDeletedUser(pageable);

        PaginationMeta meta = PaginationMeta.builder()
                .page(result.getNumber() + 1)
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();

        return ResponseHelper.successOK(result.getContent(), "Success get all deleted users", meta);
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restore(@PathVariable UUID id) {
        Boolean isRestored = userService.restoreUser(id);
        if (isRestored) {
            return ResponseHelper.successOK(null, "Success restore user");
        }
        return ResponseHelper.error("User not found or already restored", HttpStatus.NOT_FOUND);
    }
}
