package com.bbu.hrms.auth_service.service;


import com.bbu.hrms.auth_service.dto.RoleRequest;
import com.bbu.hrms.auth_service.dto.RoleResponse;
import com.bbu.hrms.auth_service.mapper.UserMapper;
import com.bbu.hrms.auth_service.model.Permission;
import com.bbu.hrms.auth_service.model.Role;
import com.bbu.hrms.auth_service.repository.PermissionRepository;
import com.bbu.hrms.auth_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepo;
    private final PermissionRepository permRepo;

    public RoleResponse assignPermissionToRole(Long roleId, Long permId) {
        Role role = roleRepo.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        Permission perm = permRepo.findById(permId).orElseThrow(() -> new RuntimeException("Permission not found"));
        role.getPermissions().add(perm);
        return UserMapper.toResponse(roleRepo.save(role));
    }

    public RoleResponse removePermissionFromRole(Long roleId, Long permId) {
        Role role = roleRepo.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        Permission perm = permRepo.findById(permId).orElseThrow(() -> new RuntimeException("Permission not found"));
        role.getPermissions().remove(perm);
        return UserMapper.toResponse(roleRepo.save(role));
    }

    public RoleResponse create(RoleRequest req) {
        Role role = new Role();
        role.setName(req.name());
        return UserMapper.toResponse(roleRepo.save(role));
    }

    public List<RoleResponse> findAll() {
        return roleRepo.findAll().stream()
                .map(UserMapper::toResponse).toList();
    }
}
