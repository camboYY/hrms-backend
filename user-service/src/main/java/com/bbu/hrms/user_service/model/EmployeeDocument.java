package com.bbu.hrms.user_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

// EmployeeDocument.java
@Entity
@Table(name = "employee_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private String docType;
    private String fileUrl;
    private LocalDate issuedDate;
    private LocalDate expiryDate;
}
