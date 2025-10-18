package com.bbu.hrms.attendance_service.service;

import com.bbu.hrms.attendance_service.dto.AttendanceDTO;
import com.bbu.hrms.attendance_service.entity.Attendance;
import com.bbu.hrms.attendance_service.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository repo;

    @Override
    public AttendanceDTO markAttendance(Long employeeId, String status) {
        Attendance attendance = Attendance.builder()
                .employeeId(employeeId)
                .status(status)
                .checkInTime(LocalDateTime.now())
                .build();

        repo.save(attendance);
        return new AttendanceDTO(attendance.getId(), employeeId, attendance.getCheckInTime(), status);
    }

    @Override
    public List<AttendanceDTO> getAttendanceByEmployee(Long employeeId) {
        return repo.findByEmployeeId(employeeId)
                .stream()
                .map(a -> new AttendanceDTO(a.getId(), a.getEmployeeId(), a.getCheckInTime(), a.getStatus()))
                .toList();
    }
}
