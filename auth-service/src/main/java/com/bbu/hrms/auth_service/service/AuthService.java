package com.bbu.hrms.auth_service.service;

import com.bbu.hrms.auth_service.Utility.JwtUtil;
import com.bbu.hrms.auth_service.dto.*;
import com.bbu.hrms.auth_service.exception.InvalidTokenException;
import com.bbu.hrms.auth_service.exception.NewPasswordRequiredException;
import com.bbu.hrms.auth_service.exception.UserNotFoundException;
import com.bbu.hrms.auth_service.exception.UsernameAlreadyTakenException;
import com.bbu.hrms.auth_service.model.User;
import com.bbu.hrms.auth_service.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    /**
     * Handles login requests.
     *
     * @param req the {@link AuthRequest} containing the username and password
     * @return an {@link AuthResponse} containing the JWT token, or a
     *         {@link RuntimeException} if the username or password is invalid
     */
    public AuthResponse login(AuthRequest req) {
        User user = userRepo.findByUsername(req.username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!passwordEncoder.matches(req.password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken);
    }

    public void updateUserRoles(Long userId, Set<String> newRoles) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String rolesString = newRoles.stream()
                .map(String::trim)
                .collect(Collectors.joining(","));

        user.setRoles(rolesString);
        userRepo.save(user);
    }

    /**
     * Handles registration requests.
     *
     * @param req the {@link RegisterRequest} containing the user details
     * @throws UsernameAlreadyTakenException if the username is already taken
     */
    public RegisterResponse register(RegisterRequest req) {
        if (userRepo.existsByUsername(req.username)) {
            throw new UsernameAlreadyTakenException(req.username);
        }
        User user = new User();
        user.setUsername(req.username);
        user.setPassword(passwordEncoder.encode(req.password));
        user.setRoles(req.roles);
        user.setEmail(req.email);
       return new RegisterResponse("User registered successfully", userRepo.save(user).getId());
    }

    /**
     * Refreshes a JWT token.
     *
     * @param request a {@link Map} containing the key-value pair "token" : <old token>
     * @return an {@link AuthResponse} containing the new JWT token if the old token is valid, or a
     *         {@link MessageResponse} explaining the error if the old token is invalid or expired
     */
    public AuthResponse refreshJwtToken(RefreshTokenRequest request) {
        String oldToken = request.getToken();
        if (oldToken == null || oldToken.isEmpty()) {
            throw new InvalidTokenException("Token is required");
        }

        try {
            Claims claims = jwtUtil.parseClaims(oldToken);
            String username = claims.getSubject();
            Optional<User> user = userRepo.findByUsername(username);
            if (user.isEmpty()) {
                throw new InvalidTokenException("Invalid token user");
            }

            String accessToken = jwtUtil.generateToken(user.get());
            String refreshToken = jwtUtil.generateRefreshToken(user.get());

            return new AuthResponse(accessToken, refreshToken);
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid or expired token");
        }
    }

    /**
     * Handles a request to get the current logged in user's information.
     *
     * @param request the {@link HttpServletRequest} containing the JWT token
     * @return a {@link Map} containing the user's username and roles if the token is valid, or a
     *         {@link MessageResponse} explaining the error if the token is invalid or expired
     */
    public Object getCurrentUser(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        if (token == null) {
            throw new InvalidTokenException("Token missing");
        }
        try {
            Claims claims = jwtUtil.parseClaims(token);
            String username = claims.getSubject();
            Optional<User> user = userRepo.findByUsername(username);
            return user.<Object>map(value -> Map.of(
                    "username", value.getUsername(),
                    "roles", value.getRoles()
            )).orElseGet(() -> new UserNotFoundException("User not found"));
            // Don't send password or sensitive info
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid token");
        }
    }

    /**
     * Checks if a given token is valid.
     *
     * @param token the token to check
     * @return a {@link Map} containing a boolean value for "valid" and a string for "error" if the token is invalid. The "error" key is only present if the token is invalid.
     */
    public Object validateToken(String token) {
        try {
            jwtUtil.parseClaims(token);
            return Map.of("valid", true);
        } catch (Exception e) {
            return Map.of("valid", false, "error", e.getMessage());
        }
    }

    /**
     * Changes the password for the currently authenticated user.
     *
     * @param request the {@link HttpServletRequest} containing the JWT token
     * @param body a {@link Map} containing the "oldPassword" and "newPassword"
     * @return a {@link MessageResponse} indicating the success or failure of the operation
     * @throws InvalidTokenException if the token is missing or invalid, or if the old or new passwords are not provided
     */
    public Object changePassword(HttpServletRequest request, Map<String, String> body) {
        String token = jwtUtil.resolveToken(request);
        if (token == null) {
            throw new InvalidTokenException("Token missing");
        }

        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");

        if (oldPassword == null || newPassword == null) {
            throw new InvalidTokenException("Old and new passwords required");
        }

        try {
            Claims claims = jwtUtil.parseClaims(token);
            String username = claims.getSubject();
            Optional<User> user = userRepo.findByUsername(username);
            if (user.isEmpty()) {
                throw new UserNotFoundException("User not found");
            }

            if (!passwordEncoder.matches(oldPassword, user.get().getPassword())) {
                return new MessageResponse("Old password incorrect");
            }

            user.get().setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user.get());

            return new MessageResponse("Password changed successfully");
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid token");
        }
    }

    /**
     * Resets a user's password.
     *
     * @param body a {@link ResetPasswordRequest} containing the username and newPassword
     * @return a {@link MessageResponse} indicating the success or failure of the operation
     * @throws NewPasswordRequiredException if no username or newPassword is provided
     * @throws UserNotFoundException if no user exists with the given username
     */
    public Object resetPassword(ResetPasswordRequest body) {
        String username = body.getUsername();
        String newPassword = body.getNewPassword();

        if (username == null || newPassword == null) {
            throw new NewPasswordRequiredException("Username and newPassword required");
        }

        Optional<User> user = userRepo.findByUsername(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        user.get().setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user.get());

        return new MessageResponse("Password reset successfully");
    }
}

