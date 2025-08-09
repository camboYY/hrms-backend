package com.bbu.hrms.user_service.repository;

import com.bbu.hrms.user_service.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
