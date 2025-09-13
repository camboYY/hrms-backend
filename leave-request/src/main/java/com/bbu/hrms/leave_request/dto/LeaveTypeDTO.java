package com.bbu.hrms.leave_request.dto;

import com.bbu.hrms.leave_request.model.LeaveType;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LeaveTypeDTO {
    private Long id;
    private String name; // leave type such as sick leave, annual leave, etc
    private Integer maxDaysPerYear;
    private Boolean carryForwardAllowed;
    private Boolean requiresApproval;


    public static LeaveTypeDTO fromEntity(LeaveType leaveType) {
        return LeaveTypeDTO.builder().id(leaveType.getId())
                .name(leaveType.getName())
                .maxDaysPerYear(leaveType.getMaxDaysPerYear())
                .carryForwardAllowed(leaveType.isCarryForwardAllowed())
                .requiresApproval(leaveType.isRequiresApproval())
                .build();
    }

    public static LeaveType toEntity(LeaveTypeDTO request) {
        return LeaveType.builder().id(request.getId())
                .name(request.getName())
                .maxDaysPerYear(request.getMaxDaysPerYear())
                .carryForwardAllowed(request.getCarryForwardAllowed())
                .requiresApproval(request.getRequiresApproval())
                .build();
    }

   }
