package com.bbu.hrms.payroll.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "salary_structure",
        uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "effective_from"})
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalaryStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "basic_salary", nullable = false)
    private BigDecimal basicSalary;

    @Column(name = "tax_rate", nullable = false)
    private BigDecimal taxRate = BigDecimal.valueOf(0.05);

    @Column(name = "nssf_rate", nullable = false)
    private BigDecimal nssfRate = BigDecimal.valueOf(0.02);

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;
}
