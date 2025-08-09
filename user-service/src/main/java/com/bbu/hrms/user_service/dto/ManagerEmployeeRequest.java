package com.bbu.hrms.user_service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ManagerEmployeeRequest {
    public @Size(max = 20, message = "Employee full name must not exceed 20 characters") @NotEmpty(message = "Employee full name is required") String getFullName() {
        return fullName;
    }

    public void setFullName(@Size(max = 20, message = "Employee full name must not exceed 20 characters") @NotEmpty(message = "Employee full name is required") String fullName) {
        this.fullName = fullName;
    }

    @Size(max = 20, message = "Employee full name must not exceed 20 characters")
    @NotEmpty(message = "Employee full name is required")
    private String fullName;
}
