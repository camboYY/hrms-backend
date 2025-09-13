package com.bbu.hrms.leave_request.repository;

import com.bbu.hrms.leave_request.model.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    Optional<LeaveBalance> findByEmployeeIdAndLeaveType_Id(Long employeeId, Long leaveTypeId);
}
