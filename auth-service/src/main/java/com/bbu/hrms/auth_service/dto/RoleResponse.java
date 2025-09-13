package com.bbu.hrms.auth_service.dto;

import java.util.List;

public record RoleResponse(Long id, String name, List<PermissionResponse> permissions) {
}
