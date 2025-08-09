package com.bbu.hrms.user_service.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeCodeExistException.class)
    public ResponseEntity<Map<String, String>> handleUsernameTaken(EmployeeCodeExistException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "EMPLOYEE_CODE_EXIST");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body); // 409 Conflict
    }

    /**
     * Handles {@link EmployeeNotFoundException}s thrown by various parts of the application.
     *
     * <p>This exception is thrown when a user attempts to log in with a username that doesn't exist.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with a 401 status and a JSON body containing an "error" key set to "USER_NOT_FOUND" and a "message" key set to the exception message
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEmployeeNotFound(EmployeeNotFoundException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "EMPLOYEE_NOT_FOUND");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "USER_NOT_FOUND");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleRoleNotFound(RoleNotFoundException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "ROLE_NOT_FOUND");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    /**
     * Handles any exceptions not explicitly covered by specific exception handlers.
     *
     * <p>This method provides a generic response for unexpected server errors.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with a 500 status and a JSON body containing an "error" key set to "INTERNAL_SERVER_ERROR" and a "message" key set to the exception message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "INTERNAL_SERVER_ERROR");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body); // 500 Internal Server Error
    }

    // Handle validation errors of the request payload
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
