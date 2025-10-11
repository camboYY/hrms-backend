package com.bbu.hrms.user_service.service;


import com.bbu.hrms.user_service.client.LeaveFeignClient;
import com.bbu.hrms.user_service.dto.*;
import com.bbu.hrms.user_service.model.Employee;
import com.bbu.hrms.user_service.model.EmployeeContact;
import com.bbu.hrms.user_service.model.EmployeeDocument;
import com.bbu.hrms.user_service.model.EmployeeStatus;
import com.bbu.hrms.user_service.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.LogManager.getLogger;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl  implements EmployeeServiceInterface {

    private final EmployeeRepository employeeRepo;
    private final DepartmentRepository departmentRepo;
    private final PositionRepository positionRepo;
    private final EmployeeContactRepository contactRepo;
    private final EmployeeDocumentRepository documentRepo;
    private final LeaveFeignClient leaveClient;
    private final Logger logger = getLogger(EmployeeServiceImpl.class);

    // --- mapping helpers (simple, no MapStruct for brevity)
    private EmployeeDTO toDTO(Employee e) {
        EmployeeDTO d = new EmployeeDTO();
        d.setId(e.getId());
        d.setUserId(e.getUserId());
        d.setEmployeeCode(e.getEmployeeCode());
        d.setFirstName(e.getFirstName());
        d.setLastName(e.getLastName());
        d.setGender(e.getGender());
        d.setDob(e.getDob());
        d.setMaritalStatus(e.getMaritalStatus());
        d.setHireDate(e.getHireDate());
        d.setJobTitle(e.getJobTitle());
        d.setDepartmentId(e.getDepartment() != null ? e.getDepartment().getId() : null);
        d.setPositionId(e.getPosition() != null ? e.getPosition().getId() : null);
        d.setManagerId(e.getManager() != null ? e.getManager().getId() : null);
        d.setStatus(e.getStatus());
        d.setDepartmentName(e.getDepartment() != null ? e.getDepartment().getName() : null);
        d.setPositionName(e.getPosition() != null ? e.getPosition().getName() : null);
        d.setManagerName(e.getManager() != null ? e.getManager().getFirstName() + " " + e.getManager().getLastName() : null);
        return d;
    }

    private Employee toEntity(EmployeeDTO d, Employee existing) {
        Employee e = existing != null ? existing : new Employee();
        e.setUserId(d.getUserId());
        e.setEmployeeCode(d.getEmployeeCode());
        e.setFirstName(d.getFirstName());
        e.setLastName(d.getLastName());
        e.setGender(d.getGender());
        e.setDob(d.getDob());
        e.setMaritalStatus(d.getMaritalStatus());
        e.setHireDate(d.getHireDate());
        e.setJobTitle(d.getJobTitle());
        e.setStatus(d.getStatus());

        e.setDepartment(d.getDepartmentId() == null ? null :
                departmentRepo.findById(d.getDepartmentId()).orElseThrow());
        e.setPosition(d.getPositionId() == null ? null :
                positionRepo.findById(d.getPositionId()).orElseThrow());
        e.setManager(d.getManagerId() == null ? null :
                employeeRepo.findById(d.getManagerId()).orElseThrow());
        return e;
    }

    private EmployeeContactDTO toDTO(EmployeeContact c) {
        EmployeeContactDTO d = new EmployeeContactDTO();
        d.setId(c.getId());
        d.setEmployeeId(c.getEmployee().getId());
        d.setPhone(c.getPhone());
        d.setEmail(c.getEmail());
        d.setAddress(c.getAddress());
        d.setEmergencyContact(c.getEmergencyContact());
        return d;
    }

    private EmployeeDocumentDTO toDTO(EmployeeDocument c) {
        EmployeeDocumentDTO d = new EmployeeDocumentDTO();
        d.setId(c.getId());
        d.setEmployeeId(c.getEmployee().getId());
        d.setDocType(c.getDocType());
        d.setFileUrl(c.getFileUrl());
        d.setIssuedDate(c.getIssuedDate());
        d.setExpiryDate(c.getExpiryDate());
        return d;
    }

    @Override
    public EmployeeDTO create(EmployeeDTO dto) {
        if (employeeRepo.existsByEmployeeCode(dto.getEmployeeCode())) {
            throw new IllegalArgumentException("Employee code already exists");
        }
        Employee saved = employeeRepo.save(toEntity(dto, null));
        return toDTO(saved);
    }

    @Override
    public EmployeeDTO update(Long id, EmployeeDTO dto) {
        Employee existing = employeeRepo.findById(id).orElseThrow();
        // keep unique code unless explicitly changed (and validated)
        if (!existing.getEmployeeCode().equals(dto.getEmployeeCode())
                && employeeRepo.existsByEmployeeCode(dto.getEmployeeCode())) {
            throw new IllegalArgumentException("Employee code already exists");
        }
        Employee saved = employeeRepo.save(toEntity(dto, existing));
        return toDTO(saved);
    }

    @Override @Transactional(readOnly = true)
    public EmployeeDTO get(Long id) {
        return employeeRepo.findById(id).map(this::toDTO).orElseThrow();
    }

    @Override
    public void delete(Long id) {
        employeeRepo.deleteById(id);
    }

    @Override
    public List<LeaveRequestDTO> getEmployeesByManager(Long managerId) {
        List<Employee> employees = employeeRepo.findByManagerUserId(managerId);
        var ids = employees.stream().map(Employee::getUserId).collect(Collectors.toList());
        if (ids.isEmpty()) return List.of();
        logger.info("Retrieving leave requests for employees {} for manager {}",ids, managerId);
        return leaveClient.getLeavesByEmployeeIdsAndStatus(ids, "PENDING");
    }

    @Override @Transactional(readOnly = true)
    public Page<EmployeeDTO> list(EmployeeStatus status, Pageable pageable) {
        Page<Employee> page = (status == null)
                ? employeeRepo.findAll(pageable)
                : employeeRepo.findByStatus(status, pageable);
        return page.map(this::toDTO);
    }

    @Override @Transactional(readOnly = true)
    public List<EmployeeContactDTO> listContacts(Long employeeId) {
        return contactRepo.findByEmployee_Id(employeeId).stream().map(this::toDTO).toList();
    }

    @Override
    public EmployeeContactDTO addContact(EmployeeContactDTO dto) {
        Employee emp = employeeRepo.findById(dto.getEmployeeId()).orElseThrow();
        EmployeeContact c = EmployeeContact.builder()
                .employee(emp).phone(dto.getPhone()).email(dto.getEmail())
                .address(dto.getAddress()).emergencyContact(dto.getEmergencyContact())
                .build();
        return toDTO(contactRepo.save(c));
    }

    @Override
    public List<EmployeeDTO> getEmployeesByPosition(Long positionId) {
        List<Employee> employees = employeeRepo.findByPositionId(positionId);
        return employees.stream().map(this::toDTO).toList();
    }

    @Override
    public List<EmployeeDTO> searchByNameOrPosition(String query) {
        return employeeRepo.searchByNameOrPosition(query).stream().map(this::toDTO).toList();
    }

    @Override
    public void deleteContact(Long id) { contactRepo.deleteById(id); }

    @Override @Transactional(readOnly = true)
    public List<EmployeeDocumentDTO> listDocuments(Long employeeId) {
        return documentRepo.findByEmployee_Id(employeeId).stream().map(this::toDTO).toList();
    }

    @Override
    public EmployeeDocumentDTO addDocument(EmployeeDocumentDTO dto) {
        Employee emp = employeeRepo.findById(dto.getEmployeeId()).orElseThrow();
        EmployeeDocument c = EmployeeDocument.builder()
                .employee(emp).docType(dto.getDocType()).fileUrl(dto.getFileUrl())
                .issuedDate(dto.getIssuedDate()).expiryDate(dto.getExpiryDate())
                .build();
        return toDTO(documentRepo.save(c));
    }

    @Override
    public void deleteDocument(Long id) { documentRepo.deleteById(id); }

    @Override
    public Long count() {
        return employeeRepo.countByStatus(EmployeeStatus.ACTIVE);
    }
}
