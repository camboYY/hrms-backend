package com.bbu.hrms.auth_service.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public class ChangeUserRoleRequest {
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;

    private Set<String> newRoles; // e.g. ["ROLE_ADMIN", "ROLE_MANAGER"]

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<String> getNewRoles() {
        return newRoles;
    }

    public void setNewRoles(Set<String> newRoles) {
        this.newRoles = newRoles;
    }
}
