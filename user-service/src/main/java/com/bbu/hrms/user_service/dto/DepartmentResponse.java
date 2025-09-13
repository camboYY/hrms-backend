package com.bbu.hrms.user_service.dto;

import com.bbu.hrms.user_service.model.Department;

public class DepartmentResponse {
    private Long id;
    private String name;
    private String description;
    private Long parentId;

    public DepartmentResponse(Long id, String name, String description, Long parentId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.parentId = parentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public static DepartmentResponse fromEntity(Department department) {
        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getDescription(),
                department.getParentDepartment() != null ? department.getParentDepartment().getId() : null
        );
    }
}
