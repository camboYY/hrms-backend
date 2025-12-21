package com.bbu.hrms.auth_service.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
public class UserRequest {
    @NotBlank(message = "Username is required")
    private String username;
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Status is required")
    private String status;
    @NotBlank(message = "Password is required")
    private String password;
}
