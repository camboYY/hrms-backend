package com.bbu.hrms.leave_request.repository;


import com.bbu.hrms.leave_request.model.LeaveRequest;
import com.bbu.hrms.leave_request.model.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployeeId(Long employeeId);
    List<LeaveRequest> findByStatus(LeaveStatus status);
    // Simple IN query + optional status
    @Query("SELECT l FROM LeaveRequest l WHERE l.employeeId IN :employeeIds AND (:status IS NULL OR l.status = :status)")
    List<LeaveRequest> findByEmployeeIdsAndStatus(List<Long> employeeIds, LeaveStatus status);

}
