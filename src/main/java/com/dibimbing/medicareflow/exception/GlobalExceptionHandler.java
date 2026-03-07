package com.dibimbing.medicareflow.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dibimbing.medicareflow.dto.BaseResponse;
import com.dibimbing.medicareflow.helper.ResponseHelper;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.ObjectNotFoundException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

     @ExceptionHandler({
          NotFoundException.class, 
          EntityNotFoundException.class, 
          JpaObjectRetrievalFailureException.class,
          ObjectNotFoundException.class
     })
     public <T> ResponseEntity<BaseResponse<T>> handleNotFoundException(Exception ex) {
          log.error("Resource not found: {}", ex.getMessage());
          return ResponseHelper.error(ex.getMessage(), HttpStatus.NOT_FOUND);
     }

     @ExceptionHandler(BusinessException.class)
     public <T> ResponseEntity<BaseResponse<T>> handleBusinessException(BusinessException ex) {
          log.error("Business exception: {}", ex.getMessage());
          return ResponseHelper.error(ex.getMessage(), HttpStatus.BAD_REQUEST);
     }

     @ExceptionHandler(ConflictException.class)
     public <T> ResponseEntity<BaseResponse<T>> handleConflictException(ConflictException ex) {
          log.error("Conflict exception: {}", ex.getMessage());
          return ResponseHelper.error(ex.getMessage(), HttpStatus.CONFLICT);
     }

     @ExceptionHandler(ForbiddenException.class)
     public <T> ResponseEntity<BaseResponse<T>> handleForbiddenException(ForbiddenException ex) {
          log.error("Forbidden exception: {}", ex.getMessage());
          return ResponseHelper.error(ex.getMessage(), HttpStatus.FORBIDDEN);
     }

     @ExceptionHandler(Exception.class)
     public <T> ResponseEntity<BaseResponse<T>> handleException(Exception ex) {
          log.error("Internal server error: ", ex);
          return ResponseHelper.error("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
     }

     @ExceptionHandler(BadRequestException.class)
     public <T> ResponseEntity<BaseResponse<T>> handleBadRequestException(BadRequestException ex) {
          log.error("Bad request exception: {}", ex.getMessage());
          return ResponseHelper.error(ex.getMessage(), HttpStatus.BAD_REQUEST);
     }
}
