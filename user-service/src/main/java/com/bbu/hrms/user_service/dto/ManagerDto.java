package com.bbu.hrms.user_service.dto;

import com.bbu.hrms.user_service.model.User;

public record ManagerDto(Long id, String username, String email, String firstName, String lastName, String phoneNumber) {
    public static ManagerDto fromEntity(User manager) {
        return new ManagerDto(manager.getId(), manager.getUsername(), manager.getEmail(), manager.getFirstName(), manager.getLastName(), manager.getPhoneNumber());
    }
}
