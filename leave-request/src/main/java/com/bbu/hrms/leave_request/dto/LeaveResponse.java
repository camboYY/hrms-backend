package com.bbu.hrms.leave_request.dto;

import com.bbu.hrms.leave_request.model.LeaveRequest;
import com.bbu.hrms.leave_request.model.LeaveStatus;

import java.time.LocalDate;
import java.util.List;

public class LeaveResponse {
    private Long id;
    private Long employeeId;
    private String leaveType; // SICK, ANNUAL, UNPAID, etc.
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LeaveStatus status;

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    public static List<LeaveResponse> fromEntities(List<LeaveRequest> byEmployeeId) {
        return byEmployeeId.stream().map(LeaveResponse::fromEntity).toList();
    }

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getLeaveType() {
        return leaveType;
    }
    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }

    // --- Converter Method ---
    public static LeaveResponse fromEntity(LeaveRequest leave) {
        LeaveResponse response = new LeaveResponse();
        response.setId(leave.getId());
        response.setEmployeeId(leave.getEmployeeId());
        response.setLeaveType(leave.getLeaveType());
        response.setStartDate(leave.getStartDate());
        response.setEndDate(leave.getEndDate());
        response.setReason(leave.getReason());
        response.setStatus(leave.getStatus());
        return response;
    }
}
