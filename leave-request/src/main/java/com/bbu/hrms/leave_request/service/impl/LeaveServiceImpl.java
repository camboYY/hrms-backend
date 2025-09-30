package com.bbu.hrms.leave_request.service.impl;


import com.bbu.hrms.leave_request.client.EmployeeClient;
import com.bbu.hrms.leave_request.dto.*;
import com.bbu.hrms.leave_request.model.LeaveBalance;
import com.bbu.hrms.leave_request.model.LeaveRequest;
import com.bbu.hrms.leave_request.model.LeaveStatus;
import com.bbu.hrms.leave_request.model.LeaveType;
import com.bbu.hrms.leave_request.repository.LeaveBalanceRepository;
import com.bbu.hrms.leave_request.repository.LeaveRequestRepository;
import com.bbu.hrms.leave_request.repository.LeaveTypeRepository;
import com.bbu.hrms.leave_request.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveServiceImpl implements LeaveService {

    private final LeaveTypeRepository leaveTypeRepo;
    private final LeaveRequestRepository leaveRequestRepo;
    private final LeaveBalanceRepository leaveBalanceRepo;
    private final EmployeeClient employeeClient;

    // --- Leave Types ---
    @Override
    public LeaveTypeDTO createLeaveType(LeaveTypeDTO dto) {
        LeaveType lt = LeaveType.builder()
                .name(dto.getName())
                .maxDaysPerYear(dto.getMaxDaysPerYear())
                .carryForwardAllowed(dto.getCarryForwardAllowed())
                .requiresApproval(dto.getRequiresApproval())
                .build();
        lt = leaveTypeRepo.save(lt);
        dto.setId(lt.getId());
        return dto;
    }

    @Override
    public List<LeaveTypeDTO> listLeaveTypes() {
       return leaveTypeRepo.findAll().stream()
               .map(LeaveTypeDTO::fromEntity).toList();
    }

    // --- Leave Requests ---
    @Override
    public LeaveRequestDTO requestLeave(LeaveRequestDTO dto) {
        LeaveType type = leaveTypeRepo.findById(dto.getLeaveTypeId()).orElseThrow();
        LeaveRequest req = LeaveRequest.builder()
                .employeeId(dto.getEmployeeId())
                .leaveType(type)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .reason(dto.getReason())
                .status(LeaveStatus.PENDING)
                .build();
        req = leaveRequestRepo.save(req);
        dto.setId(req.getId());
        dto.setStatus(req.getStatus());
        return dto;
    }

    @Override
    public LeaveRequestDTO approveLeave(Long requestId, Long approverId) {
        LeaveRequest req = leaveRequestRepo.findById(requestId).orElseThrow(()-> new RuntimeException("Request not found"));
        req.setStatus(LeaveStatus.APPROVED);
        req.setApproverId(approverId);

        // Update balance
        int days = (int) (req.getEndDate().toEpochDay() - req.getStartDate().toEpochDay() + 1);
        LeaveBalance bal = leaveBalanceRepo.findByEmployeeIdAndLeaveType_Id(req.getEmployeeId(), req.getLeaveType().getId())
                .orElseThrow(()-> new RuntimeException("Balance not found"));
        if(bal.getRemainingDays() < days) {
            throw new RuntimeException("Not enough leave balance");
        }
        bal.setUsedDays(bal.getUsedDays() + days);
        bal.setRemainingDays(bal.getAllocatedDays() - bal.getUsedDays());
        leaveBalanceRepo.save(bal);

        return toDTO(req);
    }

    @Override
    public LeaveRequestDTO rejectLeave(Long requestId, Long approverId) {
        LeaveRequest req = leaveRequestRepo.findById(requestId).orElseThrow();
        req.setStatus(LeaveStatus.REJECTED);
        req.setApproverId(approverId);
        return toDTO(req);
    }

    @Override
    public List<LeaveRequestDTO> listRequests(Long employeeId) {
        return leaveRequestRepo.findByEmployeeId(employeeId).stream().map(this::toDTO).toList();
    }

    @Override
    public LeaveRequestDTO cancelRequest(Long requestId) {
        LeaveRequest req = leaveRequestRepo.findById(requestId).orElseThrow();
        req.setStatus(LeaveStatus.CANCELLED);
        return toDTO(req);
    }

    @Override
    public List<LeaveRequestDTO> getRecentRequests(Long employeeId) {
        return leaveRequestRepo.findByEmployeeId(employeeId).stream().limit(5).map(this::toDTO).toList();
    }

    // --- Leave Balances ---
    @Override
    public LeaveBalanceDTO allocateLeave(LeaveBalanceDTO dto) {
        LeaveType type = leaveTypeRepo.findById(dto.getLeaveTypeId()).orElseThrow();
        LeaveBalance bal = LeaveBalance.builder()
                .employeeId(dto.getEmployeeId())
                .leaveType(type)
                .allocatedDays(dto.getAllocatedDays())
                .usedDays(0)
                .remainingDays(dto.getAllocatedDays())
                .build();
        bal = leaveBalanceRepo.save(bal);
        dto.setId(bal.getId());
        dto.setRemainingDays(bal.getRemainingDays());
        return dto;
    }

    @Override
    public LeaveBalanceDTO getBalance(Long employeeId, Long leaveTypeId) {
        LeaveBalance bal = leaveBalanceRepo.findByEmployeeIdAndLeaveType_Id(employeeId, leaveTypeId).orElseThrow();
        return LeaveBalanceDTO.fromEntity(bal);
    }

    // --- Helpers ---
    private LeaveRequestDTO toDTO(LeaveRequest req) {
        LeaveRequestDTO dto = new LeaveRequestDTO();
        dto.setId(req.getId());
        dto.setEmployeeId(req.getEmployeeId());
        dto.setLeaveTypeId(req.getLeaveType().getId());
        dto.setStartDate(req.getStartDate());
        dto.setEndDate(req.getEndDate());
        dto.setReason(req.getReason());
        dto.setStatus(req.getStatus());
        dto.setApproverId(req.getApproverId());
        return dto;
    }

    public List<LeaveRequest> getPendingRequestsForManager(Long managerId) {
        // Step 1: Get employees under manager
        List<EmployeeResponse> employees = employeeClient.getEmployeesByManager(managerId);

        List<Long> employeeIds = employees.stream()
                .map(EmployeeResponse::getId)
                .collect(Collectors.toList());

        // Step 2: Fetch leave requests by employeeIds with PENDING status
        return leaveRequestRepo.findByEmployeeIdInAndStatus(employeeIds, LeaveStatus.PENDING);
    }
}
