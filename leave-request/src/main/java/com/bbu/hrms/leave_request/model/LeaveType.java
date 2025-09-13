package com.bbu.hrms.leave_request.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "leave_types")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class LeaveType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // Annual, Sick, etc.

    @Column(name = "max_days_per_year", nullable = false)
    private Integer maxDaysPerYear;
    private boolean requiresApproval;
    @Column(name = "carry_forward_allowed", nullable = false)
    private boolean carryForwardAllowed;
}