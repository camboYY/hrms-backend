package com.bbu.hrms.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PositionDTO {
    private Long id;

    @NotBlank(message = "Position title is required")
    private String title;

    private String description;

    @NotNull(message = "Department is required")
    private Long departmentId;

    private String departmentName;
}

