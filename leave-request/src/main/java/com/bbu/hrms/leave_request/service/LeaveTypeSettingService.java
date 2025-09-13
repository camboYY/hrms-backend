package com.bbu.hrms.leave_request.service;

import com.bbu.hrms.leave_request.dto.LeaveBalanceDTO;
import com.bbu.hrms.leave_request.dto.LeaveTypeDTO;
import com.bbu.hrms.leave_request.exception.LeaveTypeSettingNotFoundException;
import com.bbu.hrms.leave_request.exception.LeaveTypeSettingExistsException;
import com.bbu.hrms.leave_request.model.LeaveType;
import com.bbu.hrms.leave_request.repository.LeaveTypeSettingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LeaveTypeSettingService {

    private final LeaveTypeSettingRepository repository;

    public LeaveTypeSettingService(LeaveTypeSettingRepository repository) {
        this.repository = repository;
    }

    public Page<LeaveTypeDTO> getAllByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByNameContaining(name, pageable).map(LeaveTypeDTO::fromEntity);
    }


    public LeaveTypeDTO getById(Long id) {
        LeaveType setting = repository.findById(id)
                .orElseThrow(() -> new LeaveTypeSettingNotFoundException("LeaveTypeSetting not found with id " + id));
        return LeaveTypeDTO.fromEntity(setting);
    }

    public LeaveTypeDTO create(LeaveTypeDTO request) {
        if (repository.findByName(request.getName()).isPresent()) {
            throw new LeaveTypeSettingExistsException("LeaveType already exists");
        }
        LeaveType entity = LeaveTypeDTO.toEntity(request);
        LeaveType saved = repository.save(entity);
        return LeaveTypeDTO.fromEntity(saved);
    }

    public LeaveTypeDTO update(Long id, LeaveTypeDTO request) {
        LeaveType existing = repository.findById(id)
                .orElseThrow(() -> new LeaveTypeSettingNotFoundException("LeaveTypeSetting not found with id " + id));
        existing.setMaxDaysPerYear(request.getMaxDaysPerYear());
        existing.setCarryForwardAllowed(request.getCarryForwardAllowed());
        existing.setRequiresApproval(request.getRequiresApproval());
        // leaveType not updated here to avoid conflict
        LeaveType saved = repository.save(existing);
        return LeaveTypeDTO.fromEntity(saved);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new LeaveTypeSettingNotFoundException("LeaveTypeSetting not found with id " + id);
        }
        repository.deleteById(id);
    }

}
