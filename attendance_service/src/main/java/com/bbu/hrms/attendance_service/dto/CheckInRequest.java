package com.bbu.hrms.attendance_service.dto;

import lombok.Data;

@Data
public class CheckInRequest {
    private String note;
    private String location;
}
