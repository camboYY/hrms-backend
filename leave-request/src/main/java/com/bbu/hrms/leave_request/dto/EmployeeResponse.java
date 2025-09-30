package com.bbu.hrms.leave_request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {
    private Long id;
    @NotBlank(message = "Employee code is required")

    @NotBlank(message = "Employee code is required")
    private String employeeCode;

    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;

    private String gender;
    private LocalDate dob;
    private String maritalStatus;
    private LocalDate hireDate;
    private String jobTitle;
    private String departmentName;
    private String managerName;
    @NotNull(message = "User ID is required")
    private Long userId;

    private Long departmentId; // optional
    private Long positionId;   // optional
    private Long managerId;    // nullable

    private String positionName;

    @NotNull(message = "Status is required")
    private EmployeeStatus status;
}
 enum EmployeeStatus { ACTIVE, RESIGNED, TERMINATED }

