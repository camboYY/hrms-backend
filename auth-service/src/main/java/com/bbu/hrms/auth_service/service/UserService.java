package com.bbu.hrms.auth_service.service;

import com.bbu.hrms.auth_service.dto.UserRequest;
import com.bbu.hrms.auth_service.dto.UserResponse;
import com.bbu.hrms.auth_service.mapper.UserMapper;
import com.bbu.hrms.auth_service.model.Role;
import com.bbu.hrms.auth_service.model.User;
import com.bbu.hrms.auth_service.repository.RoleRepository;
import com.bbu.hrms.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = Logger.getLogger(UserService.class.getName());

    public Page<UserResponse> all(Pageable pageable) {
        return repo.findAll(pageable).map(UserMapper::toResponse);
    }

    public UserResponse get(Long id) {
        return repo.findById(id)
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserResponse create(UserRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        logger.info("User created12:  " + request);

        User formedUser = UserMapper.toEntity(request);
        logger.info("User created: " + formedUser);
        return UserMapper.toResponse(repo.save(formedUser));
    }

    public UserResponse update(Long id, UserRequest request) {
        User existing = repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        // Encode password only if it is provided
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            request.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        logger.info("User updated: " + request);
        UserMapper.updateEntity(existing, request);
        return UserMapper.toResponse(repo.save(existing));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public UserResponse assignRole(Long userId, Long roleId) {
        User u = repo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Role r = roleRepo.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        u.getRoles().add(r);
        return UserMapper.toResponse(repo.save(u));
    }

    public UserResponse assignRoleToUser(Long userId, Long roleId) {
        User user = repo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepo.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(role);
        return UserMapper.toResponse(repo.save(user));
    }

    public UserResponse removeRoleFromUser(Long userId, Long roleId) {
        User user = repo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepo.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().remove(role);
        return UserMapper.toResponse(repo.save(user));
    }
}
