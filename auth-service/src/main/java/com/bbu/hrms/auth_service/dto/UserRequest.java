package com.bbu.hrms.auth_service.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank(message = "Username is required") String username,
        @Email(message = "Invalid email format") String email,
        @NotBlank(message = "Status is required") String status,
        @NotBlank(message = "Password is required") String password
) {}
