package com.bbu.hrms.leave_request.repository;

import com.bbu.hrms.leave_request.model.LeaveType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaveTypeSettingRepository extends JpaRepository<LeaveType, Long> {
    Optional<LeaveType> findByName(String name);
    Page<LeaveType> findByNameContaining(String name, Pageable pageable);
}
