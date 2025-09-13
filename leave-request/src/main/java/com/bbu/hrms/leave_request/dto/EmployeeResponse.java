package com.bbu.hrms.leave_request.dto;

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
    private String employeeCode;
    private String fullName;
    private String gender;
    private LocalDate dob;
    private String maritalStatus;
    private LocalDate hireDate;
    private String jobTitle;
    private String departmentName;
    private String managerName;
    private String status;
}
