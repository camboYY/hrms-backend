package com.bbu.hrms.auth_service.controler;

import com.bbu.hrms.auth_service.dto.*;
import com.bbu.hrms.auth_service.exception.InvalidTokenException;
import com.bbu.hrms.auth_service.exception.NewPasswordRequiredException;
import com.bbu.hrms.auth_service.exception.UserNotFoundException;
import com.bbu.hrms.auth_service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and token management")
public class AuthController {

    @Autowired
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Handles login requests.
     *
     * @param request the {@link AuthRequest} containing the username and password
     * @return a {@link ResponseEntity} containing an {@link AuthResponse} with the JWT token if credentials are valid,
     *         or a response with a 401 status if the credentials are invalid
     */
    @Operation(summary = "Login user", description = "Authenticate user and return JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * Registers a new user in the system.
     *
     * @param request the {@link RegisterRequest} containing the user details
     * @return a {@link ResponseEntity} with a status of 200 if the user is successfully registered,
     *         or a status of 400 if the request is invalid
     */
    @Operation(summary = "Register new user", description = "Registers a new user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
      return ResponseEntity.ok().body(authService.register(request));
    }

    /**
     * Generates a new JWT token using the given refresh token.
     *
     * @param request a {@link RefreshTokenRequest} containing the refresh token
     * @return a {@link ResponseEntity} containing the new JWT token if the refresh token is valid,
     *         or a response with a 401 status if the refresh token is invalid or expired
     */
    @Operation(summary = "Refresh JWT token", description = "Generate a new JWT token using refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshJwtToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshJwtToken(request));
    }

    /**
     * Fetch the currently logged-in user info using JWT token.
     *
     * @param request the request containing the JWT token
     * @return a ResponseEntity containing the user's username and roles if the token is valid,
     *         or a MessageResponse explaining the error if the token is invalid or expired
     */
    @Operation(summary = "Get current user", description = "Fetch the currently logged-in user info using JWT token")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        return ResponseEntity.ok(authService.getCurrentUser(request));
    }

    /**
     * Validates a JWT token by checking its signature and expiration date.
     *
     * @param token the JWT token to validate
     * @return a ResponseEntity containing a map with a single key-value pair: "valid" : true if the token is valid, false otherwise.
     *         If the token is invalid, the response will also contain a "message" key with a human-readable error message.
     */
    @Operation(summary = "Validate JWT token", description = "Validate the given JWT token (used by API Gateway)")
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        return ResponseEntity.ok(authService.validateToken(token));
    }

    /**
     * Changes the password for the currently authenticated user.
     *
     * @param request the request containing the JWT token
     * @param body a {@link Map} containing the "oldPassword" and "newPassword"
     * @return a {@link ResponseEntity} indicating the success or failure of the operation
     * @throws InvalidTokenException if the token is missing or invalid, or if the old or new passwords are not provided
     */
    @Operation(summary = "Change password", description = "Change password for the logged-in user")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(HttpServletRequest request, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(authService.changePassword(request, body));
    }

    /**
     * Resets a user's password.
     *
     * <p>This endpoint uses a simplified flow that requires only the username and new password.
     * It is intended for use by the frontend, and is not intended to be used directly by users.
     *
     * @param body a {@link ResetPasswordRequest} containing the username and newPassword
     * @return a {@link ResponseEntity} indicating the success or failure of the operation
     * @throws NewPasswordRequiredException if no username or newPassword is provided
     * @throws UserNotFoundException if no user exists with the given username
     */
    @Operation(summary = "Reset password", description = "Reset user password (simplified flow)")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest body) {
        return ResponseEntity.ok(authService.resetPassword(body));
    }

    @PutMapping("/change-roles")
    @Operation(summary = "Change user roles", description = "Change user roles")
    public ResponseEntity<?> changeUserRoles(@Validated @RequestBody ChangeUserRoleRequest request) {
        authService.updateUserRoles(request.getUserId(), request.getNewRoles());
        return ResponseEntity.ok(Map.of("message", "User roles updated successfully"));
    }
}
