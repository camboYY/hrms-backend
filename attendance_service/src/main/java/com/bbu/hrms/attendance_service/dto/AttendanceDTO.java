package com.bbu.hrms.attendance_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AttendanceDTO {
    private Long id;
    private Long employeeId;
    private LocalDateTime checkInTime;
    private String status;
}
