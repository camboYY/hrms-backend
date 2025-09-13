package com.bbu.hrms.auth_service.dto;

import java.util.List;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        Long userId,
        List<String> roles
) {}
