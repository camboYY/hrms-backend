package com.bbu.hrms.user_service.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeContactDTO {
    private Long id;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    private String address;
    private String emergencyContact;
}
