package com.bbu.hrms.auth_service.mapper;

import com.bbu.hrms.auth_service.dto.PermissionResponse;
import com.bbu.hrms.auth_service.dto.RoleResponse;
import com.bbu.hrms.auth_service.dto.UserRequest;
import com.bbu.hrms.auth_service.dto.UserResponse;
import com.bbu.hrms.auth_service.model.Permission;
import com.bbu.hrms.auth_service.model.Role;
import com.bbu.hrms.auth_service.model.User;

public class UserMapper {

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getStatus(),
                user.getRoles().stream().map(UserMapper::toResponse).toList()

        );
    }

    public static User toEntity(UserRequest request) {
        User u = new User();
        u.setUsername(request.username());
        u.setEmail(request.email());
        u.setStatus(request.status());
        u.setPassword(request.password());
        return u;
    }

    public static void updateEntity(User user, UserRequest request) {
        if (request.password() != null) {
            user.setPassword(request.password());
        }
        if (request.email() != null) {
            user.setEmail(request.email());
        }
        if (request.status() != null) {
            user.setStatus(request.status());
        }
        if (request.username() != null) {
            user.setUsername(request.username());
        }

    }

    public static PermissionResponse toResponse(Permission p) {
        return new PermissionResponse(p.getId(), p.getName());
    }

    public static RoleResponse toResponse(Role r) {
        return new RoleResponse(
                r.getId(),
                r.getName(),
                r.getPermissions().stream().map(UserMapper::toResponse).toList()
        );
    }

}

