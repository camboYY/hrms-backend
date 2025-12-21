package com.bbu.hrms.leave_request.controller;



import com.bbu.hrms.leave_request.dto.LeaveBalanceDTO;
import com.bbu.hrms.leave_request.dto.LeaveRequestDTO;
import com.bbu.hrms.leave_request.dto.LeaveTypeDTO;
import com.bbu.hrms.leave_request.model.LeaveRequest;
import com.bbu.hrms.leave_request.service.LeaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("leaves")
@RequiredArgsConstructor
@Tag(name = "Leave Management", description = "Manage leave types, requests, and balances")
public class LeaveRequestController {

    private final LeaveService service;

    // --- Leave Types ---
    @Operation(summary = "Create a new leave type")
    @PostMapping("/types")
    public ResponseEntity<LeaveTypeDTO> createType(@Valid @RequestBody LeaveTypeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createLeaveType(dto));
    }

    @Operation(summary = "List all leave types")
    @GetMapping("/types")
    public ResponseEntity<List<LeaveTypeDTO>> listTypes() {
        return ResponseEntity.ok(service.listLeaveTypes());
    }

    // --- Leave Requests ---
    @Operation(summary = "Request new leave")
    @PostMapping("/requests")
    public ResponseEntity<LeaveRequestDTO> requestLeave(@Valid @RequestBody LeaveRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.requestLeave(dto));
    }

    @Operation(summary = "Approve leave request")
    @PostMapping("/requests/{id}/approve")
    public ResponseEntity<LeaveRequestDTO> approve(@PathVariable Long id,
                                                   @RequestParam Long approverId) {
        return ResponseEntity.ok(service.approveLeave(id, approverId));
    }

    @Operation(summary = "Reject leave request")
    @PostMapping("/requests/{id}/reject")
    public ResponseEntity<LeaveRequestDTO> reject(@PathVariable Long id,
                                                  @RequestParam Long approverId) {
        return ResponseEntity.ok(service.rejectLeave(id, approverId));
    }

    @Operation(summary = "List leave requests for an employee")
    @GetMapping("/requests")
    public ResponseEntity<List<LeaveRequestDTO>> listRequests(@RequestParam Long employeeId) {
        return ResponseEntity.ok(service.listRequests(employeeId));
    }

    @Operation(summary = "Cancel leave request")
    @PostMapping("/requests/{id}/cancel")
    public ResponseEntity<LeaveRequestDTO> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelRequest(id));
    }

    @Operation(summary = "Get recent leave requests for an employee")
    @GetMapping("/requests/recent")
    public ResponseEntity<List<LeaveRequestDTO>> getRecentRequests(@RequestParam Long employeeId) {
        return ResponseEntity.ok(service.getRecentRequests(employeeId));
    }

    // --- Leave Balances ---
    @Operation(summary = "Allocate leave balance for an employee")
    @PostMapping("/balances")
    public ResponseEntity<LeaveBalanceDTO> allocate(@Valid @RequestBody LeaveBalanceDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.allocateLeave(dto));
    }

    @Operation(summary = "Get leave balance for an employee and type")
    @GetMapping("/balances")
    public ResponseEntity<LeaveBalanceDTO> getBalance(@RequestParam Long employeeId,
                                                      @RequestParam Long leaveTypeId) {
        return ResponseEntity.ok(service.getBalance(employeeId, leaveTypeId));
    }

    // --- Leave Requests for Manager ---

    // Example: GET /api/leaves?employeeIds=1,2,3&status=PENDING
    @GetMapping
    public List<LeaveRequestDTO> getLeaves(
            @RequestParam(required = false, name = "employeeIds") List<Long> employeeIds,
            @RequestParam(required = false) String status
    ) {
        return service.getByEmployeeIdsAndStatus(employeeIds, status);
    }
}

