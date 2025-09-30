package com.bbu.hrms.auth_service.controler;

import com.bbu.hrms.auth_service.dto.UserRequest;
import com.bbu.hrms.auth_service.dto.UserResponse;
import com.bbu.hrms.auth_service.dto.UserRoleRequest;
import com.bbu.hrms.auth_service.helper.PagedResponse;
import com.bbu.hrms.auth_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management")
public class UserController {
    private final UserService service;

    @Operation(summary = "Get all users")
    @GetMapping
    public ResponseEntity<PagedResponse<UserResponse>> list(@RequestParam(defaultValue = "0") Integer page,
                                                            @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponse> pageResult = service.all(pageable);

        return ResponseEntity.ok().body(new PagedResponse<UserResponse>(
                pageResult.getContent(),
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isLast()));
    }

    @Operation(summary = "Get user by id")
    @GetMapping("/{id}")
    public  ResponseEntity<UserResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.get(id));
    }

    @Operation(summary = "Create a new user")
    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest u) {
        return ResponseEntity.ok().body(service.create(u));
    }

    @Operation(summary = "Update a user")
    @PutMapping("/{id}")
    public  ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody UserRequest u) {
        return ResponseEntity.ok().body(service.update(id, u));
    }

    @Operation(summary = "Delete a user")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Assign role to user")
    @PostMapping("/{userId}/roles")
    public ResponseEntity<UserResponse> assignRole(
            @PathVariable Long userId,
            @RequestBody UserRoleRequest request) {
        return ResponseEntity.ok(service.assignRoleToUser(userId, request.roleId()));
    }

    @Operation(summary = "Remove role from user")
    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<UserResponse> removeRole(
            @PathVariable Long userId,
            @PathVariable Long roleId) {
        return ResponseEntity.ok(service.removeRoleFromUser(userId, roleId));
    }
}
