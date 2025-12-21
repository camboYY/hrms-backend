package com.bbu.hrms.attendance_service.controller;

import com.bbu.hrms.attendance_service.client.UserClient;
import com.bbu.hrms.attendance_service.dto.AttendanceResponse;
import com.bbu.hrms.attendance_service.dto.CheckOutRequest;
import com.bbu.hrms.attendance_service.dto.EmployeeDTO;
import com.bbu.hrms.attendance_service.entity.Attendance;
import com.bbu.hrms.attendance_service.service.AttendanceService;
import com.bbu.hrms.common.events.Utility.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/attendances")
@RequiredArgsConstructor
@Tag(name = "Attendance API", description = "Operations related to attendance")
public class AttendanceController {

    private final AttendanceService service;
    private final UserClient userClient;
    private final JwtUtil jwtUtil; // From Auth Service or a shared library

    @Operation(summary = "Mark attendance")
    @PostMapping("/{employeeId}")
    public ResponseEntity<AttendanceResponse> markAttendance(
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(service.markAttendance(employeeId));
    }

    @Operation(summary = "Check out")
    @PostMapping("/check-out")
    public AttendanceResponse checkOut(
            HttpServletRequest servletRequest,
            @RequestBody CheckOutRequest request
    ) {
        Long employeeId = resolveEmployeeId(servletRequest);

        Attendance a = service.checkOut(
                employeeId,
                request.getNote()
        );
        return map(a);
    }

    @Operation(summary = "Get my attendance")
    @GetMapping("/me")
    public List<AttendanceResponse> myAttendance(
            HttpServletRequest servletRequest,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {
        return service
                .myAttendance(resolveEmployeeId(servletRequest), from, to)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    // -------------------------
    // Helper methods
    // -------------------------
    private AttendanceResponse map(Attendance a) {
        return AttendanceResponse.builder()
                .id(a.getId())
                .date(a.getDate())
                .checkInTime(a.getCheckInTime())
                .checkOutTime(a.getCheckOutTime())
                .status(a.getStatus())
                .note(a.getNote())
                .location(a.getLocation())
                .build();
    }


    private Long resolveEmployeeId(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        if (token == null) throw new IllegalStateException("JWT token missing");

        Long authUserId = jwtUtil.extractClaim(token, claims -> claims.get("userId", Integer.class)).longValue();

        // Call User Service through API Gateway
        String authHeader = request.getHeader("Authorization");
        EmployeeDTO employee = userClient.getEmployeeByAuthUserId(authHeader, authUserId);

        return employee.getId();
    }

}
