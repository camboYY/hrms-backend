package com.bbu.hrms.notification_service.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class NotificationMessage implements Serializable {
    private Long employeeId;
    private Long approverId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String leaveType;
    private String status;
    private String message;
}
