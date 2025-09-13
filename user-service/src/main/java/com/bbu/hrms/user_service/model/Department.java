package com.bbu.hrms.user_service.model;

import com.bbu.hrms.user_service.dto.DepartmentResponse;
import jakarta.persistence.*;

@Table(name = "departments")
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "parent_department_id")
    private Department parentDepartment;

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

    public Department getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(Department parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    public DepartmentResponse toResponse() {
        return new DepartmentResponse(
                this.getId(),
                this.getName(),
                this.getDescription(),
                this.getParentDepartment() != null ? this.getParentDepartment().getId() : null);
    }
}
