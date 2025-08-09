package com.bbu.hrms.auth_service.exception;

import com.bbu.hrms.auth_service.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link UsernameAlreadyTakenException}s thrown by {@link AuthService#register}.
     *
     * <p>This exception is thrown when a user attempts to register with a username that is already taken.
     *
     * @param ex the exception thrown
     * @return a response with a 409 status (Conflict) and a JSON body containing the error key "USERNAME_TAKEN" and the exception's message.
     */
    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<Map<String, String>> handleUsernameTaken(UsernameAlreadyTakenException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "USERNAME_TAKEN");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body); // 409 Conflict
    }

    /**
     * Handles {@link InvalidTokenException}s thrown by various parts of the application.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with a 401 status and a JSON body containing an "error" key set to "INVALID_TOKEN" and a "message" key set to the exception message
     */
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Map<String, String>> handleInvalidToken(InvalidTokenException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "INVALID_TOKEN");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body); // 401 Unauthorized
    }

    /**
     * Handles {@link UserNotFoundException}s thrown by various parts of the application.
     *
     * <p>This exception is thrown when a user attempts to log in with a username that doesn't exist.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with a 401 status and a JSON body containing an "error" key set to "USER_NOT_FOUND" and a "message" key set to the exception message
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "USER_NOT_FOUND");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    /**
     * Handles {@link NewPasswordRequiredException}s thrown by {@link AuthService#changePassword}.
     *
     * <p>This exception is thrown when a user attempts to change their password without providing a new password.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with a 401 status and a JSON body containing an "error" key set to "NEW_PASSWORD_REQUIRED" and a "message" key set to the exception message
     */
    @ExceptionHandler(NewPasswordRequiredException.class)
    public ResponseEntity<Map<String, String>> handleNewPasswordRequired(NewPasswordRequiredException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "NEW_PASSWORD_REQUIRED");
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
}
