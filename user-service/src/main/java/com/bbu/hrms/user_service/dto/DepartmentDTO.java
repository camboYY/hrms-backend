package com.bbu.hrms.user_service.dto;

// DepartmentDTO.java

import com.bbu.hrms.user_service.model.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {
    private Long id;
    private String name;
    private String description;
    private String parentDepartmentName;

    public static DepartmentDTO fromEntity(Department department) {
        return DepartmentDTO.builder().
                id(department.getId()).
                name(department.getName()).
                description(department.getDescription()).
                parentDepartmentName(department.getParentDepartment() != null ? department.getParentDepartment().getName() : null).
                build();
    }
}
