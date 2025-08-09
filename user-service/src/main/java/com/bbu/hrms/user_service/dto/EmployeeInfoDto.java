package com.bbu.hrms.user_service.dto;


import com.bbu.hrms.user_service.model.EmployeeInfo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record EmployeeInfoDto(
        Long id,
        String employeeCode,
        String fullName,
        String department,
        String position,
        LocalDate joinDate,
        BigDecimal salary,
        String email,
        String phone,
        UserDto user,
        ManagerDto manager
) {
    public static EmployeeInfoDto fromEntity(EmployeeInfo emp) {
        return new EmployeeInfoDto(
                emp.getId(),
                emp.getEmployeeCode(),
                emp.getFullName(),
                emp.getDepartment(),
                emp.getPosition(),
                emp.getJoinDate(),
                emp.getSalary(),
                emp.getEmail(),
                emp.getPhone(),
                EmployeeDto.fromEntity(emp.getUser()),
                emp.getManager() != null ? ManagerDto.fromEntity(emp.getManager()) : null
        );
    }

    public static List<EmployeeInfoDto> fromEntities(List<EmployeeInfo> all) {
        return all.stream()
                .map(EmployeeInfoDto::fromEntity).collect(Collectors.toList());
    }
}


