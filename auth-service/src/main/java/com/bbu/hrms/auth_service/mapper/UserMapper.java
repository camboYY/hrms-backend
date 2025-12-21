package com.bbu.hrms.auth_service.mapper;

import com.bbu.hrms.auth_service.dto.PermissionResponse;
import com.bbu.hrms.auth_service.dto.RoleResponse;
import com.bbu.hrms.auth_service.dto.UserRequest;
import com.bbu.hrms.auth_service.dto.UserResponse;
import com.bbu.hrms.auth_service.model.Permission;
import com.bbu.hrms.auth_service.model.Role;
import com.bbu.hrms.auth_service.model.User;
import com.bbu.hrms.common.events.model.AuthUser;

import java.util.stream.Collectors;

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
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(request.getPassword());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user.setEmail(request.getEmail());
        }
        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            user.setStatus(request.getStatus());
        }
        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
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

    public static AuthUser mapToAuthUser(User user) {

        AuthUser dto = new AuthUser();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());

        dto.setRoles(
                user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
        );

        dto.setPermissions(
                user.getRoles()
                        .stream()
                        .flatMap(r -> r.getPermissions().stream())
                        .map(Permission::getName)
                        .collect(Collectors.toSet())
        );

        return dto;
    }

}

