package com.bbu.hrms.user_service.dto;

// EmployeeRequest.java

import com.bbu.hrms.user_service.model.EmployeeStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequest {
    private Long userId;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate dob;
    private String maritalStatus;
    private LocalDate hireDate;
    private String jobTitle;
    private Long departmentId;
    private Long managerId;
    private EmployeeStatus status;
}
