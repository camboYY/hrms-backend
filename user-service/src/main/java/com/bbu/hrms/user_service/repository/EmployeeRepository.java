package com.bbu.hrms.user_service.repository;

import com.bbu.hrms.user_service.model.Employee;
import com.bbu.hrms.user_service.model.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmployeeCode(String code);
    boolean existsByEmployeeCode(String employeeCode);
    Page<Employee> findByStatus(EmployeeStatus status, Pageable pageable);
    List<Employee> findByManagerId(Long managerId);

}
