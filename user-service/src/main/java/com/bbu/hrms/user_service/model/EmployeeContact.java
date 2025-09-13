package com.bbu.hrms.user_service.model;

import jakarta.persistence.*;
import lombok.*;

// EmployeeContact.java
@Entity
@Table(name = "employee_contacts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeContact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private String phone;
    private String email;
    private String address;
    private String emergencyContact;
}

