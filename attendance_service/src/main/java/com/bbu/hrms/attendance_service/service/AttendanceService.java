package com.bbu.hrms.attendance_service.service;


import com.bbu.hrms.attendance_service.dto.AttendanceDTO;

import java.util.List;

public interface AttendanceService {
    AttendanceDTO markAttendance(Long employeeId, String status);
    List<AttendanceDTO> getAttendanceByEmployee(Long employeeId);
}
