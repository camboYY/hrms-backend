package com.bbu.hrms.payroll.repository;

import com.bbu.hrms.payroll.entity.SalaryStructure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalaryStructureRepository extends JpaRepository<SalaryStructure, Long> {

    Optional<SalaryStructure> findTopByEmployeeIdOrderByEffectiveFromDesc(Long employeeId);
}
