package com.bbu.hrms.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeDocumentDTO {
    private Long id;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotBlank(message = "Document type is required")
    private String docType;

    @NotBlank(message = "File URL is required")
    private String fileUrl;

    private LocalDate issuedDate;
    private LocalDate expiryDate;
}
