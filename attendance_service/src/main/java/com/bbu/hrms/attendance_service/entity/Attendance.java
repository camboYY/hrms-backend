package com.bbu.hrms.attendance_service.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId; // mapped from User Service

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    private LocalDate date; //workDate

    private String note;

    private String location;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status; // PRESENT, ON_LEAVE, ABSENT, LATE, HALF_DAY

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}


