package com.bbu.hrms.payroll.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PayrollItemDTO {
    private Long id;
    private String type; // ALLOWANCE / DEDUCTION
    private String name;
    private BigDecimal amount;
    private Long payrollId;
}
