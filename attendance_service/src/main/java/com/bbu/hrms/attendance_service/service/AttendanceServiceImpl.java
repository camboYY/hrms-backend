package com.bbu.hrms.attendance_service.service;

import com.bbu.hrms.attendance_service.dto.AttendanceResponse;
import com.bbu.hrms.attendance_service.entity.Attendance;
import com.bbu.hrms.attendance_service.entity.AttendanceStatus;
import com.bbu.hrms.attendance_service.repository.AttendanceRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository repo;

    @Transactional
    public Attendance markAttendanceByLeaveEvent(Long employeeId, LocalDate date, String leaveStatus) {
        Attendance attendance = repo.findByEmployeeIdAndDate(employeeId, date)
                .orElse(Attendance.builder()
                        .employeeId(employeeId)
                        .date(date)
                        .build());

        switch (leaveStatus.toUpperCase()) {
            case "APPROVED":
                attendance.setStatus(AttendanceStatus.ON_LEAVE);
                break;
            case "REJECTED":
                attendance.setStatus(AttendanceStatus.ABSENT);
                break;
            default:
                attendance.setStatus(AttendanceStatus.PRESENT);
        }
        return repo.save(attendance);
    }

    @Transactional
    @Override
    public Attendance markAttendance(Long employeeId, String note, String location) {

        Attendance record =  repo.findByEmployeeIdAndDate(employeeId, LocalDateTime.now().toLocalDate())
                .orElseGet(() -> Attendance.builder()
                        .employeeId(employeeId)
                        .status(AttendanceStatus.PRESENT)
                        .date(LocalDateTime.now().toLocalDate())
                        .note(note)
                        .location(location)
                        .build()
                );

        if (record.getStatus().equals(AttendanceStatus.ON_LEAVE)) {
            throw new RuntimeException("Employee is on leave");
        }

        if (record.getCheckInTime() != null ) {
            throw new RuntimeException("Employee is already checked in");
        }

        record.setCheckInTime(LocalDateTime.now());


        return repo.save(record);
    }

    @Transactional
    public Attendance checkOut(Long employeeId, String note) {
        LocalDate today = LocalDate.now();

        Attendance attendance = repo
                .findByEmployeeIdAndDate(employeeId, today)
                .orElseThrow(() ->
                        new IllegalStateException("Not checked in")
                );

        if (attendance.getCheckOutTime() != null) {
            throw new IllegalStateException("Already checked out");
        }

        attendance.setCheckOutTime(LocalDateTime.now());
        attendance.setNote(note);

        return repo.save(attendance);
    }

    @Transactional(readOnly = true)
    public List<Attendance> myAttendance(
            Long employeeId,
            LocalDate from,
            LocalDate to
    ) {
        return repo.findByEmployeeIdAndDateBetween(
                employeeId, from, to
        );
    }
}
