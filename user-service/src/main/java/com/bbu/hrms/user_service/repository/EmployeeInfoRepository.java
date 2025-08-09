package com.bbu.hrms.user_service.repository;

import com.bbu.hrms.user_service.dto.EmployeeInfoDto;
import com.bbu.hrms.user_service.model.EmployeeInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;


public interface EmployeeInfoRepository extends JpaRepository<EmployeeInfo, Long> {
    /**
     * Checks if an employee exists with the given employee code.
     *
     * @param employeeCode the employee code to check for existence
     * @return true if an employee exists with the given employee code, false otherwise
     */
    boolean existsByEmployeeCode(String employeeCode);

    EmployeeInfo findByEmployeeCode(String employeeCode);

    Page<EmployeeInfo> findByEmployeeCodeContainingIgnoreCase(String employeeCode, Pageable pageable);

    List<EmployeeInfo> findByFullNameContainingIgnoreCase(String fullName);

    List<EmployeeInfo> findByManagerId(Long managerId);

    // Find distinct departments
    @Query("SELECT DISTINCT e.department FROM EmployeeInfo e WHERE e.department IS NOT NULL")
    List<String> findDistinctDepartments();

}
