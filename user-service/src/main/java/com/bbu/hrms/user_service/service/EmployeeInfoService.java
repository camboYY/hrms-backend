package com.bbu.hrms.user_service.service;

import com.bbu.hrms.user_service.dto.*;
import com.bbu.hrms.user_service.exception.EmployeeCodeExistException;
import com.bbu.hrms.user_service.exception.EmployeeNotFoundException;
import com.bbu.hrms.user_service.model.EmployeeInfo;
import com.bbu.hrms.user_service.model.User;
import com.bbu.hrms.user_service.repository.EmployeeInfoRepository;
import com.bbu.hrms.user_service.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EmployeeInfoService {

    private final EmployeeInfoRepository employeeRepo;
    private final UserRepository userRepository;
    public EmployeeInfoService(EmployeeInfoRepository employeeRepo, UserRepository userRepository) {
        this.employeeRepo = employeeRepo;
        this.userRepository = userRepository;
    }

    public Page<EmployeeInfoDto> getAllEmployees(String employeeCode, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        if (employeeCode != null && !employeeCode.isEmpty()) {
            return employeeRepo.findByEmployeeCodeContainingIgnoreCase(employeeCode, pageable)
                    .map(EmployeeInfoDto::fromEntity);
        }

        return employeeRepo.findAll(pageable)
                .map(EmployeeInfoDto::fromEntity);
    }

    public EmployeeInfoDto getEmployeeById(Long id) {
        return employeeRepo.findById(id).map(EmployeeInfoDto::fromEntity).orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + id));
    }

   public List<String> getTotalDepartments() {
        return employeeRepo.findDistinctDepartments();
   }

   public long getTotalEmployees() {
      return  employeeRepo.count();
   }

    public List<EmployeeInfoDto> getSubordinates(Long managerId) {
        List<EmployeeInfo> employees = employeeRepo.findByManagerId(managerId);
        return employees.stream().map(EmployeeInfoDto::fromEntity).toList();
    }

    public EmployeeInfoDto createEmployee(EmployeeInfoRequest employee) {
        if (employeeRepo.existsByEmployeeCode(employee.getEmployeeCode())) {
            throw new EmployeeCodeExistException(employee.getEmployeeCode());
        }

        User user = userRepository.findByUsername(employee.getUsername())
                .orElseThrow(() -> new EmployeeNotFoundException("User not found"));
        // Optionally also load manager if present
        User manager = null;
        if (employee.getManagerId() != null) {
            manager = userRepository.findById(employee.getManagerId())
                    .orElseThrow(() -> new EmployeeNotFoundException("Manager not found"));
        }

        EmployeeInfo newEmployee = getEmployeeInfo(employee, manager, user);
        EmployeeInfo savedEmployee = employeeRepo.save(newEmployee);
        System.out.println("Employee created: " + savedEmployee);
        return EmployeeInfoDto.fromEntity(savedEmployee);
    }

    private static EmployeeInfo getEmployeeInfo(EmployeeInfoRequest employee, User manager, User user) {
        EmployeeInfo newEmployee = new EmployeeInfo();
        newEmployee.setFullName(employee.getFullName());
        newEmployee.setEmployeeCode(employee.getEmployeeCode());
        newEmployee.setDepartment(employee.getDepartment());
        newEmployee.setPosition(employee.getPosition());
        newEmployee.setEmail(employee.getEmail());
        newEmployee.setPhone(employee.getPhone());
        newEmployee.setJoinDate(employee.getJoinDate());
        newEmployee.setSalary(employee.getSalary());
        newEmployee.setManager(manager);
        newEmployee.setUser(user);
        return newEmployee;
    }

    public EmployeeInfoDto updateEmployee(Long id, EmployeeInfoUpdateRequest updated) {
        return employeeRepo.findById(id).map(emp -> {

            if (updated.getFullName() != null) {
                emp.setFullName(updated.getFullName());
            }

            if (updated.getDepartment() != null) {
                emp.setDepartment(updated.getDepartment());
            }

            if (updated.getPosition() != null) {
                emp.setPosition(updated.getPosition());
            }

            if (updated.getEmail() != null) {
                emp.setEmail(updated.getEmail());
            }

            if (updated.getPhone() != null) {
                emp.setPhone(updated.getPhone());
            }

            if (updated.getJoinDate() != null) {
                emp.setJoinDate(updated.getJoinDate());
            }

            if (updated.getSalary() != null) {
                emp.setSalary(updated.getSalary());
            }
        System.out.println(updated.getManagerId()+"getManagerId");
            if (updated.getManagerId() != null) {
                System.out.println(updated.getManagerId()+"InsidegetManagerId"+ userRepository.findById(updated.getManagerId()));

                emp.setManager(userRepository.findById(updated.getManagerId()).orElse(null));
            }

            // save changes
            employeeRepo.save(emp);

            return EmployeeInfoDto.fromEntity(emp);
        }).orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + id));
    }


    public void deleteEmployee(Long id) {
        employeeRepo.deleteById(id);
    }

    public List<EmployeeInfoDto> getManagerEmployees(String fullName) {
        List<EmployeeInfo> managers = employeeRepo
                .findByFullNameContainingIgnoreCase(fullName);

        return managers.stream()
                .map(e -> new EmployeeInfoDto(
                        e.getId(),
                        e.getEmployeeCode(),
                        e.getFullName(),
                        e.getDepartment(),
                        e.getPosition(),
                        e.getJoinDate(),
                        e.getSalary(),
                        e.getEmail(),
                        e.getPhone(),
                        EmployeeDto.fromEntity(e.getUser()),
                        e.getManager() != null ? ManagerDto.fromEntity(e.getManager()) : null
                ))
                .toList();
    }

}
