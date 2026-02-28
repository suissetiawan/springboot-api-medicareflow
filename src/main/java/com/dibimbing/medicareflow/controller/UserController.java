package com.dibimbing.medicareflow.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dibimbing.medicareflow.helper.ResponseHelper;
import com.dibimbing.medicareflow.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "") String type) {
            
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

        return ResponseHelper.successOK(userService.getAll(type.toLowerCase()), "Success get all" + userType);
    }
}
