package com.bbu.hrms.user_service.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateDepartmentRequest {
    @Size(max = 100, message = "Name must not exceed 100 characters")
    @NotNull(message = "Name is required")
    private String name;
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    private Long parentId;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public @Size(max = 100, message = "Name must not exceed 100 characters") @NotNull(message = "Name is required") String getName() {
        return name;
    }

    public void setName(@Size(max = 100, message = "Name must not exceed 100 characters") @NotNull(message = "Name is required") String name) {
        this.name = name;
    }

    public @Size(max = 500, message = "Description must not exceed 500 characters") String getDescription() {
        return description;
    }

    public void setDescription(@Size(max = 500, message = "Description must not exceed 500 characters") String description) {
        this.description = description;
    }
}
