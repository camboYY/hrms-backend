package com.bbu.hrms.user_service.mapper;



import com.bbu.hrms.user_service.dto.EmployeeRequest;
import com.bbu.hrms.user_service.dto.EmployeeResponse;
import com.bbu.hrms.user_service.model.Employee;

public class EmployeeMapper {

    public static Employee toEntity(EmployeeRequest request) {
        Employee e = new Employee();
        e.setUserId(request.getUserId());
        e.setEmployeeCode(request.getEmployeeCode());
        e.setFirstName(request.getFirstName());
        e.setLastName(request.getLastName());
        e.setGender(request.getGender());
        e.setDob(request.getDob());
        e.setMaritalStatus(request.getMaritalStatus());
        e.setHireDate(request.getHireDate());
        e.setJobTitle(request.getJobTitle());
        e.setStatus(request.getStatus());
        // department and manager should be set in service layer
        return e;
    }

    public static EmployeeResponse toResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .employeeCode(employee.getEmployeeCode())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .gender(employee.getGender())
                .dob(employee.getDob())
                .maritalStatus(employee.getMaritalStatus())
                .hireDate(employee.getHireDate())
                .jobTitle(employee.getJobTitle())
                .departmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : null)
                .managerName(employee.getManager() != null ?
                        employee.getManager().getFirstName() + " " + employee.getManager().getLastName() : null)
                .status(employee.getStatus())
                .build();
    }
}

