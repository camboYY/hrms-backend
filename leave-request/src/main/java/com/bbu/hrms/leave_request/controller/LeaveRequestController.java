package com.bbu.hrms.leave_request.controller;


import com.bbu.hrms.leave_request.dto.LeaveRequestDto;
import com.bbu.hrms.leave_request.dto.LeaveResponse;
import com.bbu.hrms.leave_request.helper.PagedResponse;
import com.bbu.hrms.leave_request.model.LeaveRequest;
import com.bbu.hrms.leave_request.model.LeaveStatus;
import com.bbu.hrms.leave_request.service.LeaveRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/leaves")
@Tag(name = "API HRMS leave request", description = "Manage leave request detail")
public class LeaveRequestController {

    private final LeaveRequestService service;

    public LeaveRequestController(LeaveRequestService service) {
        this.service = service;
    }

    @GetMapping("/count/pending/manager/{managerId}")
    @Operation(summary = "Get pending leave count for manager's subordinates")
    public ResponseEntity<Map<String, Long>> getPendingLeaveCountForManager(@PathVariable Long managerId) {
        long count = service.countPendingLeavesForManager(managerId);
        return ResponseEntity.ok(Map.of("pendingLeaveRequests", count));
    }


    @GetMapping("/count/by-employee/{employeeId}")
    @Operation(summary = "Get total number of leave requests by an employee")
    public ResponseEntity<Map<String, Long>> countMyLeaves(@PathVariable Long employeeId) {
        long count = service.countLeavesByEmployeeId(employeeId);
        return ResponseEntity.ok(Map.of("totalLeaveRequests", count));
    }

    @GetMapping("/pending/{managerId}")
    @Operation(summary = "Get all pending leave requests for a manager's subordinates")
    public ResponseEntity<List<LeaveResponse>> getPendingRequestsForManager(
            @PathVariable Long managerId) {
        List<LeaveRequest> pendingRequests = service.getPendingRequestsForManager(managerId);
        return ResponseEntity.ok(pendingRequests.stream()
                .map(LeaveResponse::fromEntity).toList());
    }

    @GetMapping("/recent")
    @Operation(summary = "Get recent leave requests (global)")
    public ResponseEntity<List<LeaveResponse>> getRecentLeaves(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(service.getRecentLeaves(limit));
    }

    @GetMapping("/recent/{employeeId}")
    @Operation(summary = "Get recent leave requests by employee")
    public ResponseEntity<List<LeaveResponse>> getRecentLeavesForEmployee(
            @PathVariable Long employeeId,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok().body( service.getRecentLeavesForEmployee(employeeId, limit));
    }

    @PostMapping
    @Operation(summary = "Create a new leave request")
    public ResponseEntity<LeaveResponse> createLeave(@Validated @RequestBody LeaveRequestDto request) {
        return ResponseEntity.ok(service.createLeave(request));
    }

    @GetMapping("/my")
    @Operation(summary = "Get leave requests by employee")
    public ResponseEntity<List<LeaveResponse>> getMyLeaves(@RequestParam Long employeeId) {
        return ResponseEntity.ok(service.getEmployeeLeaves(employeeId));
    }

    // get all pending leaves request
    @GetMapping("/pending")
    @Operation(summary = "Get all pending leave requests")
    public ResponseEntity<PagedResponse<LeaveResponse>> getAllPendingLeavesRequest(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<LeaveResponse> responses = service.getAllLeaves(page, size);

        return ResponseEntity.ok( new PagedResponse<>(responses));
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve a leave request")
    public ResponseEntity<LeaveResponse> approveLeave(@PathVariable Long id) {
        return ResponseEntity.ok(service.updateStatus(id, LeaveStatus.APPROVED));
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject a leave request")
    public ResponseEntity<LeaveResponse> rejectLeave(@PathVariable Long id) {
        return ResponseEntity.ok(service.updateStatus(id, LeaveStatus.REJECTED));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel a leave request")
    public ResponseEntity<?> cancelLeave(@PathVariable Long id, @RequestParam Long employeeId) {
        service.cancelLeave(id, employeeId);
        return ResponseEntity.ok(Map.of("message", "Leave request cancelled successfully","status", "success"));
    }
}
