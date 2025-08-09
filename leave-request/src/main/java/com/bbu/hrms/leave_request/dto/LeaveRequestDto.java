package com.bbu.hrms.leave_request.dto;

import com.bbu.hrms.leave_request.model.LeaveRequest;
import com.bbu.hrms.leave_request.model.LeaveStatus;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;

public class LeaveRequestDto {

    @NotNull(message = "Employee ID is required")
    private Long employeeId; //  ID from Auth Service

    @NotBlank(message = "Leave type is required")
    @Pattern(regexp = "SICK|ANNUAL|UNPAID|MATERNITY|PATERNITY|CASUAL", message = "Leave type must be SICK, ANNUAL, MATERNITY, PATERNITY, CASUAL or UNPAID")
    private String leaveType;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date cannot be in the past")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date cannot be in the past")
    private LocalDate endDate;

    @Size(max = 255, message = "Reason must not exceed 255 characters")
    private String reason;

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    private LeaveStatus status;

    // --- Getters and Setters ---

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

    // --- Converter ---
    public LeaveRequest toEntity() {
        LeaveRequest leave = new LeaveRequest();
        leave.setEmployeeId(this.employeeId);
        leave.setLeaveType(this.leaveType);
        leave.setStartDate(this.startDate);
        leave.setEndDate(this.endDate);
        leave.setReason(this.reason);
        leave.setStatus(Objects.requireNonNullElse(this.status, LeaveStatus.PENDING));
        return leave;
    }
}
