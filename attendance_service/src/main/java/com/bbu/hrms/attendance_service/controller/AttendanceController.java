package com.bbu.hrms.attendance_service.controller;

import com.bbu.hrms.attendance_service.dto.AttendanceDTO;
import com.bbu.hrms.attendance_service.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService service;

    @PostMapping("/{employeeId}")
    public ResponseEntity<AttendanceDTO> markAttendance(
            @PathVariable Long employeeId,
            @RequestParam(defaultValue = "PRESENT") String status) {
        return ResponseEntity.ok(service.markAttendance(employeeId, status));
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<List<AttendanceDTO>> getAttendance(@PathVariable Long employeeId) {
        return ResponseEntity.ok(service.getAttendanceByEmployee(employeeId));
    }
}
