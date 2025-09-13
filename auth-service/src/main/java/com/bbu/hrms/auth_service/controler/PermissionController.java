package com.bbu.hrms.auth_service.controler;

import com.bbu.hrms.auth_service.dto.PermissionRequest;
import com.bbu.hrms.auth_service.dto.PermissionResponse;
import com.bbu.hrms.auth_service.mapper.UserMapper;
import com.bbu.hrms.auth_service.model.Permission;
import com.bbu.hrms.auth_service.repository.PermissionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@Tag(name = "Permissions", description = "Permissions")
public class PermissionController {
    private final PermissionRepository repo;

    @Operation(summary = "Get all permissions")
    @GetMapping
    public ResponseEntity<List<PermissionResponse>> all() {
        return ResponseEntity.ok(repo.findAll().stream().map(UserMapper::toResponse).toList());
    }

    @Operation(summary = "Create a new permission")
    @PostMapping
    public ResponseEntity<PermissionResponse> create(@RequestBody PermissionRequest req) {
        Permission p = new Permission();
        p.setName(req.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toResponse(repo.save(p)));
    }
}
