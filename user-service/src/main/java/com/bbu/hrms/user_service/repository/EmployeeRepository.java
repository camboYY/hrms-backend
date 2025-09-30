package com.bbu.hrms.user_service.repository;

import com.bbu.hrms.user_service.model.Employee;
import com.bbu.hrms.user_service.model.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmployeeCode(String code);
    boolean existsByEmployeeCode(String employeeCode);
    Page<Employee> findByStatus(EmployeeStatus status, Pageable pageable);
    List<Employee> findByManagerId(Long managerId);

    Long countByStatus(EmployeeStatus employeeStatus);

    List<Employee> findByPositionId(Long positionId);

    @Query("""
    SELECT e FROM Employee e
    LEFT JOIN e.position p
    WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR (p IS NOT NULL AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
""")
    List<Employee> searchByNameOrPosition(@Param("keyword") String keyword);

}
