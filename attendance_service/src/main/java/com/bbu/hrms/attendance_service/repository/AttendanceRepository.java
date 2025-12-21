package com.bbu.hrms.attendance_service.repository;

import com.bbu.hrms.attendance_service.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByEmployeeId(Long employeeId);
    Optional<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);
    List<Attendance> findByEmployeeIdAndDateBetween(
            Long employeeId, LocalDate from, LocalDate to
    );
}
