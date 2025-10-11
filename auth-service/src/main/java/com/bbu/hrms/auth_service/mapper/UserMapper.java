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
        u.setUsername(request.getUsername());
        u.setEmail(request.getEmail());
        u.setStatus(request.getStatus());
        u.setPassword(request.getPassword());
        return u;
    }

    public static void updateEntity(User user, UserRequest request) {
        if (request.getPassword() != null) {
            user.setPassword(request.getPassword());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
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

