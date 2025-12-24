package com.bbu.hrms.payroll.repository;

import com.bbu.hrms.payroll.entity.Payroll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    Page<Payroll> findAllByPayrollMonth(
            LocalDate payrollMonth,
            Pageable pageable
    );
    Optional<Payroll> findByEmployeeIdAndPayrollMonth(Long employeeId,  LocalDate payrollMonth);
}

