package com.bbu.hrms.auth_service.dto;

import java.util.List;

public record UserResponse(
        Long id,
        String username,
        String email,
        String status,
        List<RoleResponse> roles
) {}