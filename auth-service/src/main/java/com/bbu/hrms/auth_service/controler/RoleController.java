package com.bbu.hrms.auth_service.controler;


import com.bbu.hrms.auth_service.dto.RolePermissionRequest;
import com.bbu.hrms.auth_service.dto.RoleRequest;
import com.bbu.hrms.auth_service.dto.RoleResponse;
import com.bbu.hrms.auth_service.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "Role", description = "Role operations")
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "Assign permission to role")
    @PostMapping("/{roleId}/permissions")
    public ResponseEntity<RoleResponse> assignPermission(
            @PathVariable Long roleId,
            @RequestBody RolePermissionRequest request) {
        return ResponseEntity.ok(roleService.assignPermissionToRole(roleId, request.permissionId()));
    }

    @Operation(summary = "Assign multiple permissions to role")
    @PostMapping("/{roleId}/permissions/batch")
    public ResponseEntity<List<RoleResponse>> assignPermissions(
            @PathVariable Long roleId,
            @RequestBody RolePermissionRequest[] requests) {
        return ResponseEntity.ok(roleService.assignPermissionsToRole(roleId, requests));
    }

    @Operation(summary = "Remove role")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        roleService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove permission from role")
    @DeleteMapping("/{roleId}/permissions/{permId}")
    public ResponseEntity<RoleResponse> removePermission(
            @PathVariable Long roleId,
            @PathVariable Long permId) {
        return ResponseEntity.ok(roleService.removePermissionFromRole(roleId, permId));
    }

    @Operation(summary = "Create a new role")
    @PostMapping
    public ResponseEntity<RoleResponse> create(@RequestBody RoleRequest req) {
        return ResponseEntity.ok(roleService.create(req));
    }

    @Operation(summary = "Get all roles")
    @GetMapping
    public ResponseEntity<List<RoleResponse>> all() {
        return ResponseEntity.ok(roleService.findAll());
    }

}
