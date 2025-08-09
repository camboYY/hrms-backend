package com.bbu.hrms.leave_request.repository;


import com.bbu.hrms.leave_request.model.LeaveRequest;
import com.bbu.hrms.leave_request.model.LeaveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    /**
     * Retrieves a list of all leave responses for a given employee.
     *
     * @param employeeId The ID of the employee whose leave requests are to be retrieved.
     * @return A list of all leave response for the given employee.
     */
    List<LeaveRequest> findByEmployeeId(Long employeeId);

    Page<LeaveRequest> findByStatus(LeaveStatus status, Pageable pageable);
    List<LeaveRequest> findByEmployeeIdOrderByStartDateDesc(Long employeeId, Pageable pageable);

    // ✅ Get recent leave requests sorted by startDate desc
    List<LeaveRequest> findAllByOrderByStartDateDesc(Pageable pageable);

    List<LeaveRequest> findByEmployeeIdInAndStatus(List<Long> employeeIds, LeaveStatus status);

    // ✅ Get total number of leave requests for a specific employee
    long countByEmployeeId(Long employeeId);

    @Query("SELECT COUNT(l) FROM LeaveRequest l WHERE l.employeeId IN :employeeIds AND l.status = 'PENDING'")
    long countPendingLeavesForEmployees(@Param("employeeIds") List<Long> employeeIds);

}
