package com.bbu.hrms.leave_request.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "leave_balances")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Column(name = "allocated_days", nullable = false)
    private Integer allocatedDays;

    @Column(name = "used_days", nullable = false)
    private Integer usedDays;

    @Column(name = "remaining_days", nullable = false)
    private Integer remainingDays;
}

