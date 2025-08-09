package com.bbu.hrms.user_service.dto;

import com.bbu.hrms.user_service.model.Role;
import com.bbu.hrms.user_service.model.User;

import java.util.stream.Collectors;

public record EmployeeDto(Long id, String username, String email) {
    public static UserDto fromEntity(User user) {
        return new UserDto(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getStatus(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()),
                user.getAuthUserId()
        );
    }
}
