package com.bbu.hrms.leave_request.dto;


import com.bbu.hrms.leave_request.model.LeaveBalance;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class LeaveBalanceDTO {
    private Long id;

    private Long employeeId;

    private Long leaveTypeId;

    private String leaveTypeName;

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
                .leaveTypeName(entity.getLeaveType().getName())
                .remainingDays(entity.getRemainingDays())
                .employeeName(entity.getLeaveType().getName())
                .build();
    }

}
