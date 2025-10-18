package com.bbu.hrms.leave_request.service.impl;


import com.bbu.hrms.common.events.LeaveApprovedEvent;
import com.bbu.hrms.leave_request.dto.*;
import com.bbu.hrms.leave_request.model.LeaveBalance;
import com.bbu.hrms.leave_request.model.LeaveRequest;
import com.bbu.hrms.leave_request.model.LeaveStatus;
import com.bbu.hrms.leave_request.model.LeaveType;
import com.bbu.hrms.leave_request.repository.LeaveBalanceRepository;
import com.bbu.hrms.leave_request.repository.LeaveRequestRepository;
import com.bbu.hrms.leave_request.repository.LeaveTypeRepository;
import com.bbu.hrms.leave_request.service.LeaveApprovalPublisher;
import com.bbu.hrms.leave_request.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.apache.logging.log4j.LogManager.getLogger;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveServiceImpl implements LeaveService {

    private final LeaveTypeRepository leaveTypeRepo;
    private final LeaveRequestRepository leaveRequestRepo;
    private final LeaveBalanceRepository leaveBalanceRepo;
    private final LeaveApprovalPublisher leaveEventPublisher;
    private final Logger logger = getLogger(LeaveServiceImpl.class);

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
        // End update balance

        // Publish event
        LeaveApprovedEvent event = new LeaveApprovedEvent();
        event.setEmployeeId(req.getEmployeeId());
        event.setApproverId(approverId);
        event.setStartDate(req.getStartDate());
        event.setEndDate(req.getEndDate());
        event.setLeaveType(req.getLeaveType().getName());
        event.setStatus(req.getStatus().name());
        // End publish event
        logger.info("Publishing leave approved event: " + event);
        leaveEventPublisher.publishLeaveApprovedEvent(event);
        logger.info("Publishing leave approved event finished: " + event);

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

    public List<LeaveRequestDTO> getByEmployeeIdsAndStatus(List<Long> employeeIds, String status) {
        logger.info("Getting leave requests for employeeIds: {}, status: {}", employeeIds, status);
        if (employeeIds == null || employeeIds.isEmpty()) return List.of();
        LeaveStatus leaveStatus = LeaveStatus.valueOf(status.toUpperCase()); // "PENDING" -> LeaveStatus.PENDING

        List<LeaveRequest> leaves = leaveRequestRepo.findByEmployeeIdsAndStatus(employeeIds, leaveStatus);

        // Map entities to DTOs
        return leaves.stream()
                .map(l -> {
                    LeaveRequestDTO dto = new LeaveRequestDTO();
                    dto.setId(l.getId());
                    dto.setEmployeeId(l.getEmployeeId()); // only basic info
                    dto.setLeaveTypeId(l.getLeaveType().getId());
                    dto.setStartDate(l.getStartDate());
                    dto.setEndDate(l.getEndDate());
                    dto.setReason(l.getReason());
                    dto.setStatus(l.getStatus());
                    dto.setApproverId(l.getApproverId() != null ? l.getApproverId() : null);
                    return dto;
                }).toList();
    }
}
