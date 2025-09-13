package com.bbu.hrms.user_service.dto;

// EmployeeResponse.java

import com.bbu.hrms.user_service.model.EmployeeStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {
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
    private EmployeeStatus status;
}
