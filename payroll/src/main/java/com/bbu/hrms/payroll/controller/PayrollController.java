package com.bbu.hrms.payroll.controller;

import com.bbu.hrms.payroll.dto.PayrollDTO;
import com.bbu.hrms.payroll.dto.PayrollGenerateRequestDTO;
import com.bbu.hrms.payroll.dto.PayrollItemDTO;
import com.bbu.hrms.payroll.entity.Payroll;
import com.bbu.hrms.payroll.mapper.PayrollMapper;
import com.bbu.hrms.payroll.service.PayrollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/payrolls")
@RequiredArgsConstructor
@Tag(name = "Payroll API", description = "Payroll operations")
public class PayrollController {

    private final PayrollService payrollService;

    @Operation(summary = "Generate Payroll for an employee")
    @PostMapping("/generate")
    public ResponseEntity<PayrollDTO> generate(@RequestBody PayrollGenerateRequestDTO request) {
        return  ResponseEntity.ok(PayrollMapper.toDTO(
                payrollService.generatePayroll(request.getEmployeeId(), request.getPayrollMonth())
        ));
    }

    @Operation(summary = "Get Payslip for an employee")
    @GetMapping("/payslip")
    public ResponseEntity<PayrollDTO> getPayslip(
            @RequestParam Long employeeId,
            @RequestParam LocalDate month) {

        return ResponseEntity.ok(PayrollMapper.toDTO(payrollService.getPayslip(employeeId, month)));
    }

    @Operation(summary = "Add payroll items (allowances or deductions) to a payroll")
    @PostMapping("/{payrollId}/items")
    public ResponseEntity<PayrollDTO> addPayrollItems(
            @PathVariable Long payrollId,
            @RequestBody List<PayrollItemDTO> items
    ) {
        Payroll updatedPayroll = payrollService.addPayrollItems(payrollId, items);
        return ResponseEntity.ok(PayrollMapper.toDTO(updatedPayroll));
    }

    @Operation(summary = "Pay a payroll")
    @PostMapping("/{payrollId}/pay")
    public ResponseEntity<PayrollDTO> payPayroll(@PathVariable Long payrollId) {
        Payroll paidPayroll = payrollService.processPayroll(payrollId);
        return ResponseEntity.ok(PayrollMapper.toDTO(paidPayroll));
    }

    @Operation(summary = "Get payrolls by month (paginated)")
    @GetMapping
    public ResponseEntity<Page<PayrollDTO>> getPayrollsByMonth(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam LocalDate month,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("employeeId").ascending()
        );

        Page<PayrollDTO> payrolls = payrollService
                .getPayrollsByMonth(month, pageable)
                .map(PayrollMapper::toDTO);

        return ResponseEntity.ok(payrolls);
    }

}
