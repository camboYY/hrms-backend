package com.bbu.hrms.user_service.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmployeeInfoUpdateRequest {
    @NotBlank(message = "Employee code is required")
    @Size(max = 20, message = "Employee code must not exceed 20 characters")
    private String employeeCode;
    private String position;

    private LocalDate joinDate;

    private BigDecimal salary;

    private String fullName;

    private String email;

    private String phone;

    private Long managerId; // optional, so no validation here

    private String department;

    public @NotBlank(message = "Employee code is required") @Size(max = 20, message = "Employee code must not exceed 20 characters") String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(@NotBlank(message = "Employee code is required") @Size(max = 20, message = "Employee code must not exceed 20 characters") String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

}
