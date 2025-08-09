package com.bbu.hrms.leave_request.service;

import com.bbu.hrms.leave_request.client.EmployeeClient;
import com.bbu.hrms.leave_request.dto.EmployeeInfoDto;
import com.bbu.hrms.leave_request.dto.LeaveRequestDto;
import com.bbu.hrms.leave_request.dto.LeaveResponse;
import com.bbu.hrms.leave_request.exception.LeaveNotFoundException;
import com.bbu.hrms.leave_request.exception.NotAuthorizedToCancelLeaveException;
import com.bbu.hrms.leave_request.model.LeaveRequest;
import com.bbu.hrms.leave_request.model.LeaveStatus;
import com.bbu.hrms.leave_request.repository.LeaveRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveRequestService {
    private final LeaveRequestRepository repository;
    private final EmployeeClient employeeClient;
    private final LeaveApprovalPublisher leaveApprovalPublisher;

    public LeaveRequestService(LeaveRequestRepository repository , EmployeeClient employeeClient, LeaveApprovalPublisher leaveApprovalPublisher) {
        this.repository = repository;
        this.employeeClient = employeeClient;
        this.leaveApprovalPublisher = leaveApprovalPublisher;
    }

    /**
     * Creates a new leave request.
     *
     * @param request The leave request object containing details like employee ID, leave type, start and end dates, etc.
     * @return The saved leave request object.
     */
    public LeaveResponse createLeave(LeaveRequestDto request) {

        return LeaveResponse.fromEntity(repository.save(request.toEntity()));
    }

    public List<LeaveRequest> getPendingRequestsForManager(Long managerId) {
        var subordinates = employeeClient.getSubordinates(managerId);
        var subordinateIds = subordinates.stream()
                .map(EmployeeInfoDto::id)
                .toList();
        return repository.findByEmployeeIdInAndStatus(subordinateIds, LeaveStatus.PENDING);
    }

/**
 * Retrieves a list of leave responses for a specific employee.
 *
 * @param employeeId The ID of the employee for whom leave responses are to be retrieved.
 * @return A list of leave responses for the specified employee.
 */
    public List<LeaveResponse> getEmployeeLeaves(Long employeeId) {
        return LeaveResponse.fromEntities(repository.findByEmployeeId(employeeId));
    }

    /**
     * Retrieves all pending leave requests with pagination.
     *
     * @param page page number (0-based)
     * @param size number of records per page
     * @return a page of pending leave responses
     */
    public Page<LeaveResponse> getAllLeaves(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        return repository.findByStatus(LeaveStatus.PENDING, pageable).map(LeaveResponse::fromEntity);
    }

/**
 * Updates the status of a leave request identified by the given ID.
 *
 * @param id The ID of the leave request to update.
 * @param status The new status to set for the leave request.
 * @return The updated leave request object with the new status.
 * @throws RuntimeException if the leave request with the given ID is not found.
 */
    public LeaveResponse updateStatus(Long id, LeaveStatus status) {
        LeaveRequest request = repository.findById(id)
                .orElseThrow(() -> new LeaveNotFoundException("Leave not found"));
        if(request.getStatus() == LeaveStatus.APPROVED || request.getStatus() == LeaveStatus.REJECTED) {
            throw new LeaveNotFoundException("Leave already approved or rejected");
        }
        request.setStatus(status);
        leaveApprovalPublisher.sendLeaveApprovalNotification("Leave approved for employee: "+request.getEmployeeId());

        return LeaveResponse.fromEntity(repository.save(request));
    }

    /*
     *  count leave by employee id
     */
    public long countLeavesByEmployeeId(Long employeeId) {
        return repository.countByEmployeeId(employeeId);
    }

    public long countPendingLeavesForManager(Long managerId) {
        List<EmployeeInfoDto> subordinateIds = employeeClient.getSubordinates(managerId);
        if (subordinateIds.isEmpty()) return 0;
        return repository.countPendingLeavesForEmployees(subordinateIds.stream().map(EmployeeInfoDto::id).toList());
    }

    /**
     * Cancels a leave request for a given employee.
     *
     * @param id The ID of the leave request to be cancelled.
     * @param employeeId The ID of the employee requesting the cancellation.
     * @throws RuntimeException if the leave request is not found or if the employee is not authorized to cancel it.
     */
    public void cancelLeave(Long id, Long employeeId) {
        LeaveRequest request = repository.findById(id)
                .orElseThrow(() -> new LeaveNotFoundException("Leave not found"));
        if (!request.getEmployeeId().equals(employeeId) || request.getStatus() != LeaveStatus.PENDING) {
            throw new NotAuthorizedToCancelLeaveException("Not authorized to cancel this leave");
        }
        request.setStatus(LeaveStatus.CANCELLED);
        repository.save(request);
    }

    public List<LeaveResponse> getRecentLeaves(int limit) {
        return repository.findAllByOrderByStartDateDesc(PageRequest.of(0, limit)).stream().map(LeaveResponse::fromEntity).toList();
    }

    // ✅ get latest N leave requests for a specific employee
    public List<LeaveResponse> getRecentLeavesForEmployee(Long employeeId, int limit) {
        return repository.findByEmployeeIdOrderByStartDateDesc(employeeId, PageRequest.of(0, limit)).stream().map(LeaveResponse::fromEntity).toList();
    }
}
