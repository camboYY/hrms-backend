package com.bbu.hrms.user_service.controller;

import com.bbu.hrms.user_service.dto.EmployeeInfoDto;
import com.bbu.hrms.user_service.dto.EmployeeInfoRequest;
import com.bbu.hrms.user_service.dto.EmployeeInfoUpdateRequest;
import com.bbu.hrms.user_service.service.EmployeeInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/employee")
@Tag(name = "Employee Info", description = "Manage employee details")
public class EmployeeInfoController {

    private final EmployeeInfoService employeeService;

    public EmployeeInfoController(EmployeeInfoService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    @Operation(summary = "Get all employees")
    public ResponseEntity<Page<EmployeeInfoDto>> getAllEmployees(@RequestParam(required = false) String employeeCode,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeService.getAllEmployees(employeeCode, page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID")
    public ResponseEntity<EmployeeInfoDto> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PostMapping
    @Operation(summary = "Create new employee")
    public ResponseEntity<EmployeeInfoDto> createEmployee(@Validated @RequestBody EmployeeInfoRequest employee) {
        return ResponseEntity.ok(employeeService.createEmployee(employee));
    }

    @GetMapping("/departments/count")
    @Operation(summary = "Get total number of unique departments")
    public ResponseEntity<Map<String, Integer>> getTotalDepartments() {
        List<String> totalDepartments = employeeService.getTotalDepartments();
        return ResponseEntity.ok(Map.of("totalDepartments", totalDepartments.size()));
    }

    @GetMapping("/count")
    @Operation(summary = "Get total number of employees")
    public ResponseEntity<Map<String, Long>> getTotalEmployees() {
        long count = employeeService.getTotalEmployees();
        return ResponseEntity.ok(Map.of("totalEmployees", count));
    }

    @GetMapping("/search-manager")
    @Operation(summary = "Get manager employees by full name")
    public ResponseEntity<List<EmployeeInfoDto>> getManagerEmployees(@RequestParam(required = true) String fullName) {
        return ResponseEntity.ok(employeeService.getManagerEmployees(fullName));
    }

    @GetMapping("/subordinates/{managerId}")
    public List<EmployeeInfoDto> getSubordinates(@PathVariable Long managerId) {
        return employeeService.getSubordinates(managerId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update employee details")
    public ResponseEntity<EmployeeInfoDto> updateEmployee(@PathVariable Long id, @Validated @RequestBody EmployeeInfoUpdateRequest employee) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employee));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employee")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
