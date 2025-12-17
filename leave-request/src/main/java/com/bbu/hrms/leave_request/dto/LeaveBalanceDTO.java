package com.bbu.hrms.leave_request.dto;


import com.bbu.hrms.leave_request.model.LeaveBalance;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeaveBalanceDTO {
    private Long id;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Leave type ID is required")
    private Long leaveTypeId;

    private Integer allocatedDays;
    private Integer usedDays;
    private Integer remainingDays;

    private String employeeName;

    public static LeaveBalanceDTO fromEntity(LeaveBalance entity) {
        return LeaveBalanceDTO.builder().id(entity.getId())
                .employeeId(entity.getEmployeeId())
                .leaveTypeId(entity.getLeaveType().getId())
                .allocatedDays(entity.getAllocatedDays())
                .usedDays(entity.getUsedDays())
                .remainingDays(entity.getRemainingDays())
                .employeeName(entity.getLeaveType().getName())
                .build();
    }
}
