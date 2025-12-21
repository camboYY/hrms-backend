package com.bbu.hrms.payroll.repository;

import com.bbu.hrms.payroll.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    Optional<Payroll> findByEmployeeIdAndMonth(Long employeeId,  LocalDate month);
}

