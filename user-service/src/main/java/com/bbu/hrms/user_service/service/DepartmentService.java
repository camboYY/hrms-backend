package com.bbu.hrms.user_service.service;

import com.bbu.hrms.user_service.dto.CreateDepartmentRequest;
import com.bbu.hrms.user_service.dto.DepartmentDTO;
import com.bbu.hrms.user_service.dto.PagedResponse;
import com.bbu.hrms.user_service.dto.UpdateDepartmentRequest;
import com.bbu.hrms.user_service.model.Department;
import com.bbu.hrms.user_service.repository.DepartmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService  {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }


    public List<Department> getDepartmentsByName(String name) {
        return departmentRepository.findByNameContaining(name);
    }


    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElseThrow(null);
    }


    public boolean departmentExists(String name) {
        return departmentRepository.findByNameContaining(name) != null;
    }


    public void createDepartment(CreateDepartmentRequest request) {
        Department departmentExists = this.departmentRepository.findByName(request.getName());
        Department parentDepartment = this.departmentRepository.findById(request.getParentId()).orElse(null);
        if (departmentExists != null) {
            throw new IllegalArgumentException("Department with name " + request.getName() + " already exists");
        }

        Department department = new Department();
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setParentDepartment(parentDepartment);
        departmentRepository.save(department);
    }


    public void updateDepartment(Long id, UpdateDepartmentRequest request) {
        Department departmentExists = this.departmentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Department not found"));

        if (departmentExists == null) {
            throw new IllegalArgumentException("Department with name " + request.getName() + " not exists");
        }

        if (request.getParentId() != null) {
            Department parentDepartment = this.departmentRepository.findById(request.getParentId()).orElse(null);
            departmentExists.setParentDepartment(parentDepartment);
        }
        if (request.getDescription() != null) {
            departmentExists.setDescription(request.getDescription());
        }
        if (request.getName() != null) {
            departmentExists.setName(request.getName());
        }

        departmentRepository.save(departmentExists);
    }


    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    public PagedResponse<DepartmentDTO> getAllDepartments(Pageable pageable) {
       Page<Department> departments = departmentRepository.findAll(pageable);
        return new PagedResponse<>(
                departments.getContent().stream().map(DepartmentDTO::fromEntity).toList(),
                departments.getNumber(),
                departments.getSize(),
                departments.getTotalElements(),
                departments.getTotalPages(),
                departments.isLast()
                );
    }
}
