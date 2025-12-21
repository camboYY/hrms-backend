package com.bbu.hrms.payroll.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Data
public class PayrollGenerateRequestDTO {

    @NotNull
    private Long employeeId;

    @NotBlank
    private LocalDate payrollMonth; // YYYY-MM
}
