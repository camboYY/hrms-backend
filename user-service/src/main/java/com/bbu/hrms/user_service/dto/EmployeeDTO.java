// EmployeeDTO.java
package com.bbu.hrms.user_service.dto;

import com.bbu.hrms.user_service.model.EmployeeStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {
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
    @NotNull (message = "User ID is required")
    private Long userId;

    private Long departmentId; // optional
    private Long positionId;   // optional
    private Long managerId;    // nullable

    private String positionName;

    @NotNull(message = "Status is required")
    private EmployeeStatus status;

}
