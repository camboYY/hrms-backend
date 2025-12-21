package com.bbu.hrms.attendance_service.dto;

import lombok.*;

import java.time.LocalDate;

@Data
public class EmployeeDTO {
    private Long id;
    private String employeeCode;

    private String firstName;
    private String lastName;

    private String gender;
    private LocalDate dob;
    private String maritalStatus;
    private LocalDate hireDate;
    private String jobTitle;
    private String departmentName;
    private String managerName;
    private Long userId;

    private Long departmentId; // optional
    private Long positionId;   // optional
    private Long managerId;    // nullable

    private String positionName;

    private EmployeeStatus status;

}


enum EmployeeStatus { ACTIVE, RESIGNED, TERMINATED }