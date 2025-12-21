package com.bbu.hrms.payroll.dto;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class SalaryStructureDTO {

    private Long id;
    private Long employeeId;

    private BigDecimal basicSalary;

    private BigDecimal taxRate;
    private BigDecimal nssfRate;

    private LocalDate effectiveFrom;

}
