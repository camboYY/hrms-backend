package com.bbu.hrms.leave_request.service;

import com.bbu.hrms.leave_request.dto.LeaveBalanceDTO;
import com.bbu.hrms.leave_request.exception.LeaveBalanceNotFoundException;
import com.bbu.hrms.leave_request.model.LeaveBalance;
import com.bbu.hrms.leave_request.model.LeaveType;
import com.bbu.hrms.leave_request.repository.LeaveBalanceRepository;
import com.bbu.hrms.leave_request.repository.LeaveTypeSettingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class LeaveBalanceService {

    private final LeaveBalanceRepository repository;
    private final LeaveTypeSettingRepository leaveTypeSettingRepository;

    public LeaveBalanceService(LeaveBalanceRepository repository, LeaveTypeSettingRepository leaveTypeSettingRepository) {
        this.repository = repository;
        this.leaveTypeSettingRepository = leaveTypeSettingRepository;
    }

    public Page<LeaveBalanceDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable).map(this::toResponse);
    }

    public LeaveBalanceDTO getById(Long id) {
        LeaveBalance entity = repository.findById(id)
                .orElseThrow(() -> new LeaveBalanceNotFoundException("Leave balance not found"));
        return toResponse(entity);
    }

    public LeaveBalanceDTO create(LeaveBalanceDTO request) {
        LeaveType leaveTypeSetting = this.leaveTypeSettingRepository.findById(request.getLeaveTypeId()).orElseThrow(()-> new NoSuchElementException("Leave type not found"));
        LeaveBalance entity = toEntity(request,leaveTypeSetting);
        LeaveBalance saved = repository.save(entity);
        return toResponse(saved);
    }

    public LeaveBalanceDTO update(Long id, LeaveBalanceDTO request) {
        LeaveBalance saved  = repository.findById(id).map(existing -> {
            if (request.getEmployeeId() != null) {
                existing.setEmployeeId(request.getEmployeeId());
            }
            if (request.getLeaveTypeId() != null) {
                existing.setLeaveType(this.leaveTypeSettingRepository.findById(request.getLeaveTypeId()).orElseThrow(()-> new NoSuchElementException("Leave type not found")));
            }
            existing.setRemainingDays(request.getRemainingDays());

            return repository.save(existing);
        }).orElseThrow(() -> new LeaveBalanceNotFoundException("LeaveBalance not found"));

        return toResponse(saved);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new LeaveBalanceNotFoundException("Leave balance not found");
        }
        repository.deleteById(id);
    }

    private LeaveBalanceDTO toResponse(LeaveBalance entity) {
        return LeaveBalanceDTO.fromEntity(entity);
    }

    private LeaveBalance toEntity(LeaveBalanceDTO req, LeaveType leaveTypeSetting) {
        LeaveBalance entity = new LeaveBalance();
        entity.setEmployeeId(req.getEmployeeId());
        entity.setLeaveType(leaveTypeSetting);
        entity.setRemainingDays(req.getRemainingDays());
        return entity;
    }
}
