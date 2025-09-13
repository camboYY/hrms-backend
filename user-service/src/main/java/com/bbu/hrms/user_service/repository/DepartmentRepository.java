package com.bbu.hrms.user_service.repository;

import com.bbu.hrms.user_service.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByNameContaining(String name);

    Department findByName(String name);
}
