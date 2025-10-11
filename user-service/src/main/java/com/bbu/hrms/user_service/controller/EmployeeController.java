package com.bbu.hrms.user_service.controller;

import com.bbu.hrms.user_service.client.LeaveFeignClient;
import com.bbu.hrms.user_service.dto.*;
import com.bbu.hrms.user_service.model.EmployeeStatus;
import com.bbu.hrms.user_service.service.EmployeeServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Tag(name = "Employees", description = "Manage employees, contacts and documents")
public class EmployeeController {

    private final EmployeeServiceInterface service;

    @Operation(summary = "fetch employees under a manager supervision")
    @GetMapping("/{managerId}/pending-leaves")
    public ResponseEntity<List<LeaveRequestDTO>> getEmployeesByManager(
            @PathVariable Long managerId) {
        return ResponseEntity.ok(service.getEmployeesByManager(managerId));
    }

    @Operation(summary = "Create employee")
    @PostMapping
    public ResponseEntity<EmployeeDTO> create(@Valid @RequestBody EmployeeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @Operation(summary = "Update employee by id")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> update(@PathVariable Long id, @Valid @RequestBody EmployeeDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Get employee by id")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @Operation(summary = "Delete employee by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get total number of active employees")
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @Operation(summary = "Fetch employees by p")
    @GetMapping("/position/{positionId}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByPosition(@PathVariable Long positionId) {
        return ResponseEntity.ok(service.getEmployeesByPosition(positionId));
    }

    @Operation(summary = "Search employees by name or position")
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeDTO>> searchByNameOrPosition(@RequestParam String query) {
        return ResponseEntity.ok(service.searchByNameOrPosition(query));
    }

    @Operation(summary = "List employees with pagination and optional status filter")
    @GetMapping
    public ResponseEntity<PagedResponse<EmployeeDTO>> list(
            @RequestParam(required = false) EmployeeStatus status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
            ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EmployeeDTO> pageResult = service.list(status, pageable);

        return ResponseEntity.ok(new PagedResponse<>(
                pageResult.getContent(),
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isLast())
        );
    }

    // Contacts
    @Operation(summary = "List contacts for an employee")
    @GetMapping("/{employeeId}/contacts")
    public ResponseEntity<List<EmployeeContactDTO>> listContacts(@PathVariable Long employeeId) {
        return ResponseEntity.ok(service.listContacts(employeeId));
    }

    @Operation(summary = "Add a contact to an employee")
    @PostMapping("/{employeeId}/contacts")
    public ResponseEntity<EmployeeContactDTO> addContact(@PathVariable Long employeeId,
                                                         @Valid @RequestBody EmployeeContactDTO dto) {
        dto.setEmployeeId(employeeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addContact(dto));
    }

    @Operation(summary = "Delete contact by id")
    @DeleteMapping("/contacts/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long contactId) {
        service.deleteContact(contactId);
        return ResponseEntity.noContent().build();
    }

    // Documents
    @Operation(summary = "List documents for an employee")
    @GetMapping("/{employeeId}/documents")
    public ResponseEntity<List<EmployeeDocumentDTO>> listDocuments(@PathVariable Long employeeId) {
        return ResponseEntity.ok(service.listDocuments(employeeId));
    }

    @Operation(summary = "Add a document to an employee")
    @PostMapping("/{employeeId}/documents")
    public ResponseEntity<EmployeeDocumentDTO> addDocument(@PathVariable Long employeeId,
                                                           @Valid @RequestBody EmployeeDocumentDTO dto) {
        dto.setEmployeeId(employeeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addDocument(dto));
    }

    @Operation(summary = "Delete document by id")
    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long documentId) {
        service.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }
}
