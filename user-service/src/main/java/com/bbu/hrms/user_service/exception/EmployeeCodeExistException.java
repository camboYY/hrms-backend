package com.bbu.hrms.user_service.exception;

public class EmployeeCodeExistException extends RuntimeException {
    public EmployeeCodeExistException(String employeeCode) {
        super("Employee code already exists: " + employeeCode);
    }
}
