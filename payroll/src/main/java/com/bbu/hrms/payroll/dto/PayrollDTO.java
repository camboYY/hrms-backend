package com.bbu.hrms.payroll.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PayrollDTO {
    private Long id;
    private Long employeeId;
    private LocalDate payrollMonth;
    private BigDecimal grossSalary;
    private BigDecimal tax;
    private BigDecimal nssf;
    private BigDecimal netSalary;
    private String status;
    private LocalDateTime createdAt;
    private List<PayrollItemDTO> items;

}
