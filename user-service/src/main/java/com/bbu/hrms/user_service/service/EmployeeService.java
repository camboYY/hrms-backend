package com.bbu.hrms.user_service.service;

import com.bbu.hrms.user_service.dto.EmployeeRequest;
import com.bbu.hrms.user_service.dto.EmployeeResponse;
import com.bbu.hrms.user_service.dto.PagedResponse;
import com.bbu.hrms.user_service.mapper.EmployeeMapper;
import com.bbu.hrms.user_service.model.Employee;
import com.bbu.hrms.user_service.repository.DepartmentRepository;
import com.bbu.hrms.user_service.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
    public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    // Paging support
    public PagedResponse<EmployeeResponse> getAllEmployees(Pageable pageable) {
        Page<Employee> employees = employeeRepository.findAll(pageable);
               return new PagedResponse<>(
                       employees.getContent().stream().map(EmployeeMapper::toResponse).toList(),
                employees.getNumber(),
                employees.getSize(),
                employees.getTotalElements(),
                       employees.getTotalPages(),
                       employees.isLast()
               );

    }

    public EmployeeResponse getEmployeeById(Long id) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return EmployeeMapper.toResponse(emp);
    }

    public EmployeeResponse createEmployee(EmployeeRequest request) {
        Employee emp = EmployeeMapper.toEntity(request);

        if (request.getDepartmentId() != null) {
            emp.setDepartment(departmentRepository.findById(request.getDepartmentId()).orElse(null));
        }
        // manager lookup can also be added similarly
        Employee saved = employeeRepository.save(emp);
        return EmployeeMapper.toResponse(saved);
    }

    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (request.getUserId() != null) {
            emp.setUserId(request.getUserId());
        }
        if (request.getManagerId() != null) {
            Employee manager = employeeRepository.findById(request.getManagerId()).orElse(null);
            emp.setManager(manager);
        }
        if (request.getDepartmentId() != null) {
            emp.setDepartment(departmentRepository.findById(request.getDepartmentId()).orElse(null));
        }
       if (request.getDob() != null) {
           emp.setDob(request.getDob());
       }
       if (request.getHireDate() != null) {
           emp.setHireDate(request.getHireDate());
       }
       if (request.getMaritalStatus() != null) {
           emp.setMaritalStatus(request.getMaritalStatus());
       }
       if(emp.getFirstName() != null) {
           emp.setFirstName(request.getFirstName());
       }
       if(emp.getLastName() != null) {
           emp.setLastName(request.getLastName());
       }
       if(emp.getGender() != null) {
           emp.setGender(request.getGender());
       }
       if(emp.getJobTitle() != null) {
           emp.setJobTitle(request.getJobTitle());
       }
       if(emp.getStatus() != null) {
           emp.setStatus(request.getStatus());
       }
       if(emp.getEmployeeCode() != null ) {
           emp.setEmployeeCode(request.getEmployeeCode());
       }

        Employee saved = employeeRepository.save(emp);
        return EmployeeMapper.toResponse(saved);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public List<EmployeeResponse> getEmployeesByManager(Long managerId) {
        List<Employee> employees = employeeRepository.findByManagerId(managerId);
        return employees.stream()
                .map(emp -> {
                    EmployeeResponse dto = new EmployeeResponse();
                    dto.setId(emp.getId());
                    dto.setDob(emp.getDob());
                    dto.setEmployeeCode(emp.getEmployeeCode());
                    dto.setFirstName(emp.getFirstName());
                    dto.setLastName(emp.getLastName());
                    dto.setGender(emp.getGender());
                    dto.setDob(emp.getDob());
                    dto.setMaritalStatus(emp.getMaritalStatus());
                    dto.setHireDate(emp.getHireDate());
                    dto.setJobTitle(emp.getJobTitle());
                    dto.setStatus(emp.getStatus());
                    dto.setDepartmentName(emp.getDepartment() != null ? emp.getDepartment().getName() : null);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}

