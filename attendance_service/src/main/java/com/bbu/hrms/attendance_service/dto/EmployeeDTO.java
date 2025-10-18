package com.bbu.hrms.attendance_service.dto;

import lombok.Data;

@Data
public class EmployeeDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String positionName;
}
