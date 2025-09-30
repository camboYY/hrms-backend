package com.bbu.hrms.user_service.controller;

import com.bbu.hrms.user_service.dto.PositionDTO;
import com.bbu.hrms.user_service.model.Department;
import com.bbu.hrms.user_service.model.Position;
import com.bbu.hrms.user_service.repository.DepartmentRepository;
import com.bbu.hrms.user_service.repository.PositionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/positions")
@RequiredArgsConstructor
@Tag(name = "Positions", description = "Manage positions")
public class PositionController {

    private final PositionRepository repo;
    private final DepartmentRepository departmentRepo;

    private com.bbu.hrms.user_service.dto.PositionDTO toDTO(Position p) {
        PositionDTO dto = new PositionDTO();
        dto.setId(p.getId());
        dto.setTitle(p.getName());
        dto.setDescription(p.getDescription());
        dto.setDepartmentId(p.getDepartment().getId());
        dto.setDepartmentName(p.getDepartment().getName());
        return dto;
    }

    @Operation(summary = "List positions")
    @GetMapping
    public ResponseEntity<List<PositionDTO>> list() {
        return ResponseEntity.ok(repo.findAll().stream().map(this::toDTO).toList());
    }

    @Operation(summary = "Create position")
    @PostMapping
    public ResponseEntity<PositionDTO> create(@Valid @RequestBody PositionDTO dto) {
        Department dept = departmentRepo.findById(dto.getDepartmentId()).orElseThrow();
        Position p = Position.builder()
                .name(dto.getTitle()).description(dto.getDescription())
                .department(dept).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(repo.save(p)));
    }
}

