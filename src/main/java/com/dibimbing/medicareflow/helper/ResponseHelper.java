package com.dibimbing.medicareflow.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.dibimbing.medicareflow.dto.BaseResponse;

public class ResponseHelper {
    public static <T> ResponseEntity<BaseResponse<T>> successOK(T data, String message) {
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.<T>builder()
                        .responseStatus(HttpStatus.OK.value())
                        .message(message)
                        .data(data)
                        .build()
        );
    }

    public static <T> ResponseEntity<BaseResponse<T>> successCreated(T data, String message) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                BaseResponse.<T>builder()
                        .responseStatus(HttpStatus.CREATED.value())
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
