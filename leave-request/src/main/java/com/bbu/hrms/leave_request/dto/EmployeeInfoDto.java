package com.bbu.hrms.leave_request.dto;

public record EmployeeInfoDto(
        Long id,
        String employeeCode,
        String fullName,
        String department,
        String position,
        String email,
        String phone
) {}
