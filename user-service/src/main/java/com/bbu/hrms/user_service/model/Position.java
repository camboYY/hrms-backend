package com.bbu.hrms.user_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "positions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String name; // name of the position and must be unique value
    private String description;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
