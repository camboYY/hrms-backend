package com.bbu.hrms.attendance_service.service;


import com.bbu.hrms.attendance_service.dto.AttendanceResponse;
import com.bbu.hrms.attendance_service.entity.Attendance;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    AttendanceResponse markAttendance(Long employeeId);
    Attendance checkOut(Long employeeId, String note);
    List<Attendance> myAttendance(
            Long employeeId,
            LocalDate from,
            LocalDate to
    );
    Attendance markAttendanceByLeaveEvent(Long employeeId, LocalDate date, String leaveStatus);
}
