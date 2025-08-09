package com.bbu.hrms.user_service.controller;

import com.bbu.hrms.user_service.dto.*;
import com.bbu.hrms.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management Info", description = "Manage user profile details")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Create a new user
     *
     * @param request the {@link CreateUserRequest} containing user details
     * @return a {@link ResponseEntity} containing the created user
     */
    @Operation(summary = "Create new user")
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Validated @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    /**
     * Fetch a user by ID
     *
     * @param id the ID of the user to fetch
     * @return a {@link ResponseEntity} containing the user with the given ID
    */

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Get all users")
    @GetMapping
    public ResponseEntity<PagedResponse<UserDto>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "id,asc") String[] sort) {
        // Example: sort=id,desc
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @Operation(summary = "Assign role to user")
    @PostMapping("/{authUserId}/roles")
    public ResponseEntity<UserDto> assignRole(@PathVariable Long authUserId, @Validated @RequestBody AssignRoleRequest request) {
        return ResponseEntity.ok(userService.assignRole(authUserId, request));
    }

    @PostMapping("/{authUserId}/unassign-role")
    public ResponseEntity<UserDto> unassignRole(
            @PathVariable Long authUserId,
            @Valid @RequestBody AssignRoleRequest request
    ) {
        UserDto updatedUser = userService.unassignRole(authUserId, request);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Get current logged-in user")
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        return ResponseEntity.ok(userService.getCurrentUser(username));
    }
}
