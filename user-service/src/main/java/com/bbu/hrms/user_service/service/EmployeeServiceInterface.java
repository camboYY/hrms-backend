package com.bbu.hrms.user_service.service;


import com.bbu.hrms.user_service.dto.EmployeeContactDTO;
import com.bbu.hrms.user_service.dto.EmployeeDTO;
import com.bbu.hrms.user_service.dto.EmployeeDocumentDTO;
import com.bbu.hrms.user_service.dto.EmployeeResponse;
import com.bbu.hrms.user_service.model.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeServiceInterface {
    EmployeeDTO create(EmployeeDTO dto);
    EmployeeDTO update(Long id, EmployeeDTO dto);
    EmployeeDTO get(Long id);
    void delete(Long id);
    List<EmployeeResponse> getEmployeesByManager(Long managerId);

    Page<EmployeeDTO> list(EmployeeStatus status, Pageable pageable);

    // Contacts
    List<EmployeeContactDTO> listContacts(Long employeeId);
    EmployeeContactDTO addContact(EmployeeContactDTO dto);
    void deleteContact(Long id);

    // Documents
    List<EmployeeDocumentDTO> listDocuments(Long employeeId);
    EmployeeDocumentDTO addDocument(EmployeeDocumentDTO dto);
    void deleteDocument(Long id);
}
