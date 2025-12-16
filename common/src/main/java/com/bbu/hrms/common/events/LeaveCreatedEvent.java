package com.bbu.hrms.common.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class LeaveCreatedEvent implements Serializable {
    private Long employeeId;
    private Long approverId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String leaveType;
    private String status;
    private String message;
}
