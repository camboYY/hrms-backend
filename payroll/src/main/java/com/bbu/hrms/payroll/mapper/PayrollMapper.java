package com.bbu.hrms.payroll.mapper;

import com.bbu.hrms.payroll.dto.PayrollDTO;
import com.bbu.hrms.payroll.dto.PayrollItemDTO;
import com.bbu.hrms.payroll.dto.SalaryStructureDTO;
import com.bbu.hrms.payroll.entity.Payroll;
import com.bbu.hrms.payroll.entity.PayrollItem;
import com.bbu.hrms.payroll.entity.SalaryStructure;

import java.util.List;
import java.util.stream.Collectors;

public class PayrollMapper {

    // Payroll entity -> PayrollDTO
    public static PayrollDTO toDTO(Payroll payroll) {
        if (payroll == null) return null;

        List<PayrollItemDTO> items = payroll.getItems() != null
                ? payroll.getItems().stream()
                .map(PayrollMapper::toDTO)
                .collect(Collectors.toList())
                : List.of();

        return PayrollDTO.builder()
                .id(payroll.getId())
                .employeeId(payroll.getEmployeeId())
                .payrollMonth(payroll.getPayrollMonth())
                .grossSalary(payroll.getGrossSalary())
                .tax(payroll.getTax())
                .nssf(payroll.getNssf())
                .netSalary(payroll.getNetSalary())
                .status(payroll.getStatus())
                .createdAt(payroll.getCreatedAt())
                .items(items)
                .build();
    }

    // PayrollItem entity -> PayrollItemDTO
    public static PayrollItemDTO toDTO(PayrollItem item) {
        if (item == null) return null;

        return PayrollItemDTO.builder()
                .id(item.getId())
                .payrollId(item.getPayroll().getId())
                .type(item.getType())
                .name(item.getName())
                .amount(item.getAmount())
                .build();
    }

    // SalaryStructure entity -> SalaryStructureDTO
    public static SalaryStructureDTO toDTO(SalaryStructure structure) {
        if (structure == null) return null;

        return SalaryStructureDTO.builder()
                .id(structure.getId())
                .employeeId(structure.getEmployeeId())
                .basicSalary(structure.getBasicSalary())
                .taxRate(structure.getTaxRate())
                .nssfRate(structure.getNssfRate())
                .effectiveFrom(structure.getEffectiveFrom())
                .build();
    }

}
