package com.bbu.hrms.user_service.controller;

import com.bbu.hrms.user_service.dto.*;
import com.bbu.hrms.user_service.model.Department;
import com.bbu.hrms.user_service.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Department")
@RestController
@RequestMapping("/departments")
public class DepartmentController {

   private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Operation(summary = "Get all departments by name")
    @GetMapping("/")
    public ResponseEntity<List<DepartmentResponse>> getDepartments(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(departmentService.getDepartmentsByName(name).stream().map(Department::toResponse).toList());
    }

    @Operation(summary = "Get department by id")
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id).toResponse());
    }

    @Operation(summary = "Check if department exists by name")
    @PostMapping("/name")
    public ResponseEntity<Boolean> departmentExists(@RequestParam(required = true) String name) {
        return ResponseEntity.ok().body(departmentService.departmentExists(name));
    }

    @Operation(summary = "Create department")
    @PostMapping("/")
    public ResponseEntity<MessageResponse> createDepartment(@Validated @RequestBody CreateDepartmentRequest request) {
        departmentService.createDepartment(request);
        return ResponseEntity.ok().body(new MessageResponse("Department created successfully"));
    }

    @Operation(summary = "Update department")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateDepartment(@PathVariable Long id, @Validated @RequestBody UpdateDepartmentRequest request) {
        departmentService.updateDepartment(id, request);
        return ResponseEntity.ok().body(new MessageResponse("Department updated successfully"));
    }

    @Operation(summary = "Delete department")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok().body(new MessageResponse("Department deleted successfully"));
    }

    @Operation(summary = "Get all departments paginated")
    @GetMapping("/all")
    public ResponseEntity<PagedResponse<DepartmentDTO>> getAllDepartments(@RequestParam(required = false) int page,
                                                                          @RequestParam(required = false) int size
                                                                     ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        PagedResponse<DepartmentDTO> response = departmentService.getAllDepartments(pageRequest);
        return ResponseEntity.ok(response);
    }
}
