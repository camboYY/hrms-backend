package com.bbu.hrms.user_service.repository;

import com.bbu.hrms.user_service.model.EmployeeContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeContactRepository extends JpaRepository<EmployeeContact, Long> {
    List<EmployeeContact> findByEmployee_Id(Long employeeId);
}
