package com.dibimbing.medicareflow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dibimbing.medicareflow.dto.BaseResponse;
import com.dibimbing.medicareflow.helper.ResponseHelper;

@ControllerAdvice
public class GlobalExceptionHandler {

     @ExceptionHandler(NotFoundException.class)
     public <T> ResponseEntity<BaseResponse<T>> handleNotFoundException(NotFoundException ex) {
          return ResponseHelper.error(ex.getMessage(), HttpStatus.NOT_FOUND);
     }

     @ExceptionHandler(BusinessException.class)
     public <T> ResponseEntity<BaseResponse<T>> handleBusinessException(BusinessException ex) {
          return ResponseHelper.error(ex.getMessage(), HttpStatus.BAD_REQUEST);
     }

     @ExceptionHandler(ConflictException.class)
     public <T> ResponseEntity<BaseResponse<T>> handleConflictException(ConflictException ex) {
          return ResponseHelper.error(ex.getMessage(), HttpStatus.CONFLICT);
     }

     @ExceptionHandler(Exception.class)
     public <T> ResponseEntity<BaseResponse<T>> handleException(Exception ex) {
          return ResponseHelper.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
     }
}
