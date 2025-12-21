package com.bbu.hrms.payroll.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "payroll",
        uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "payroll_month"})
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "payroll_month", nullable = false)
    private LocalDate payrollMonth; // store first day of month

    @Column(name = "gross_salary", nullable = false)
    private BigDecimal grossSalary;

    @Column(name = "tax", nullable = false)
    private BigDecimal tax;

    @Column(name = "nssf", nullable = false)
    private BigDecimal nssf;

    @Column(name = "net_salary", nullable = false)
    private BigDecimal netSalary;

    @Column(name = "status", nullable = false)
    private String status = "GENERATED";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "payroll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PayrollItem> items;
}
