package com.dibimbing.medicareflow.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.dibimbing.medicareflow.dto.BaseResponse;

public class ResponseHelper {
    public static <T> ResponseEntity<BaseResponse<T>> success(T data, String message) {
        return ResponseEntity.ok(
                BaseResponse.<T>builder()
                        .responseStatus(HttpStatus.OK.value())
                        .message(message)
                        .data(data)
                        .build()
        );
    }

    public static <T> ResponseEntity<BaseResponse<T>> success(String message) {
        return ResponseEntity.ok(
                BaseResponse.<T>builder()
                        .responseStatus(HttpStatus.OK.value())
                        .message(message)
                        .build()
        );
    }

    public static <T> ResponseEntity<BaseResponse<T>> custom(T data, String message, HttpStatus status) {
        return ResponseEntity.status(status).body(
                BaseResponse.<T>builder()
                        .responseStatus(status.value())
                        .message(message)
                        .data(data)
                        .build()
        );
    }

    public static <T> ResponseEntity<BaseResponse<T>> error(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(
                BaseResponse.<T>builder()
                        .responseStatus(status.value())
                        .message(message)
                        .build()
        );
    }
}
