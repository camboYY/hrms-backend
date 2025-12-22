package com.bbu.hrms.payroll.service;

import com.bbu.hrms.payroll.dto.PayrollItemDTO;
import com.bbu.hrms.payroll.entity.Payroll;
import com.bbu.hrms.payroll.entity.PayrollItem;
import com.bbu.hrms.payroll.entity.SalaryStructure;
import com.bbu.hrms.payroll.repository.PayrollRepository;
import com.bbu.hrms.payroll.repository.SalaryStructureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayrollService {

    private final SalaryStructureRepository salaryRepo;
    private final PayrollRepository payrollRepo;

    /**
     * Generate payroll for a given employee and month
     *
     * @param employeeId Employee ID
     * @param payrollMonth First day of the payroll month
     * @return PayrollDTO
     */
    @Transactional
    public Payroll generatePayroll(Long employeeId, LocalDate payrollMonth) {

        SalaryStructure salary = salaryRepo
                .findTopByEmployeeIdOrderByEffectiveFromDesc(employeeId)
                .orElseThrow(() -> new RuntimeException("Salary not found"));

        // Base salary only, allowances handled via PayrollItem
        BigDecimal gross = salary.getBasicSalary();

        BigDecimal tax = gross.multiply(salary.getTaxRate());
        BigDecimal nssf = gross.multiply(salary.getNssfRate());
        BigDecimal net = gross.subtract(tax).subtract(nssf);

        Payroll payroll = new Payroll();
        payroll.setEmployeeId(employeeId);
        payroll.setPayrollMonth(payrollMonth);
        payroll.setGrossSalary(gross);
        payroll.setTax(tax);
        payroll.setNssf(nssf);
        payroll.setNetSalary(net);
        payroll.setStatus("GENERATED");

        return payrollRepo.save(payroll);
    }

    /**
     * Get payslip for an employee for a given month
     */
    @Transactional(readOnly = true)
    public Payroll getPayslip(Long employeeId, LocalDate payrollMonth) {
        return payrollRepo
                .findByEmployeeIdAndPayrollMonth(employeeId, payrollMonth)
                .orElseThrow(() -> new RuntimeException("Payslip not found"));
    }

    /**
     * Add payroll items (ALLOWANCE/DEDUCTION) to a payroll
     */
    @Transactional
    public Payroll addPayrollItems(Long payrollId, List<PayrollItemDTO> items) {
        Payroll payroll = payrollRepo.findById(payrollId)
                .orElseThrow(() -> new RuntimeException("Payroll not found"));

        for (PayrollItemDTO itemDTO : items) {
            PayrollItem item = PayrollItem.builder()
                    .payroll(payroll)
                    .type(itemDTO.getType())
                    .name(itemDTO.getName())
                    .amount(itemDTO.getAmount())
                    .build();
            payroll.getItems().add(item);

            // Adjust gross/net for allowances and deductions
            if ("ALLOWANCE".equalsIgnoreCase(item.getType())) {
                payroll.setGrossSalary(payroll.getGrossSalary().add(item.getAmount()));
                payroll.setNetSalary(payroll.getNetSalary().add(item.getAmount()));
            } else if ("DEDUCTION".equalsIgnoreCase(item.getType())) {
                payroll.setNetSalary(payroll.getNetSalary().subtract(item.getAmount()));
            }
        }

        payroll = payrollRepo.save(payroll);
        return  payroll;
    }

    /**
     * Process payroll: mark as PAID
     */
    @Transactional
    public Payroll processPayroll(Long payrollId) {
        Payroll payroll = payrollRepo.findById(payrollId)
                .orElseThrow(() -> new RuntimeException("Payroll not found"));

        if (!"GENERATED".equals(payroll.getStatus()) && !"APPROVED".equals(payroll.getStatus())) {
            throw new RuntimeException("Payroll already processed or invalid status");
        }

        payroll.setStatus("PAID");
        payroll.setCreatedAt(LocalDate.now().atStartOfDay()); // optional: record pay date

        return payrollRepo.save(payroll);
    }
}

