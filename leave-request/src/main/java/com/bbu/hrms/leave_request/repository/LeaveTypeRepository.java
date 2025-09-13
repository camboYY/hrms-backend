package com.bbu.hrms.leave_request.repository;

import com.bbu.hrms.leave_request.model.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {}

