package com.bbu.hrms.leave_request.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LeaveNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleLeaveNotFoundException(LeaveNotFoundException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "LEAVE_NOT_FOUND");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body); // 404 Not found
    }

    @ExceptionHandler(LeaveTypeSettingExistsException.class)
    public ResponseEntity<Map<String, String>> handleLeaveTypeSettingExistsException(LeaveTypeSettingExistsException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "LEAVE_TYPE_SETTING_EXISTS");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body); // 409 Conflict
    }

    @ExceptionHandler(LeaveBalanceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleLeaveBalanceNotFoundException(LeaveBalanceNotFoundException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "LEAVE_BALANCE_NOT_FOUND");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body); // 404 Not found
    }

    @ExceptionHandler(LeaveTypeSettingNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleLeaveTypeSettingNotFoundException(LeaveTypeSettingNotFoundException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "LEAVE_TYPE_SETTING_NOT_FOUND");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body); // 404 Not found
    }

    @ExceptionHandler(InsufficientLeaveBalanceException.class)
    public ResponseEntity<Map<String, String>> handleInsufficientLeaveBalanceException(InsufficientLeaveBalanceException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "INSUFFICIENT_LEAVE_BALANCE");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body); // 403 Forbidden
    }

    @ExceptionHandler(InvalidLeaveTypeException.class)
    public ResponseEntity<Map<String, String>> handleInvalidLeaveTypeException(InvalidLeaveTypeException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "INVALID_LEAVE_TYPE");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body); // 400 Bad Request
    }

    @ExceptionHandler(NotAuthorizedToCancelLeaveException.class)
    public ResponseEntity<Map<String, String>> handleNotAuthorizedToCancelLeaveException(NotAuthorizedToCancelLeaveException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "NOT_AUTHORIZED_TO_CANCEL_LEAVE");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body); // 403 Forbidden
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
    }}
