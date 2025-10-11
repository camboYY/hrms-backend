package com.bbu.hrms.leave_request.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class LeaveApprovedEvent implements Serializable {
    private Long employeeId;
    private Long approverId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String leaveType;
    private String status;

    // getters and setters
}
