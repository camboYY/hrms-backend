package com.bbu.hrms.auth_service.dto;


import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public class ChangeUserRoleRequest {
    @NotEmpty(message = "User ID is required")
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
