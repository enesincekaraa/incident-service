package com.enesincekara.pulseops.incident.api.exception;

import com.enesincekara.pulseops.incident.api.base.ApiError;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IncidentNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(OffsetDateTime.now(),404,"Not Found",ex.getMessage()));
    }

    @ExceptionHandler({IncidentVersionMismatchException.class, OptimisticLockingFailureException.class})
    public ResponseEntity<ApiError> handleConflict(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError(OffsetDateTime.now(),409,"Conflict",ex.getMessage()));
    }
    @ExceptionHandler(IncidentClosedException.class)
    public ResponseEntity<ApiError> handleClosed(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError(OffsetDateTime.now(), 409, "Conflict", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest()
                .body(new ApiError(OffsetDateTime.now(), 400, "Bad Request", "Validation failed"));
    }
}
