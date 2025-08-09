package com.bbu.hrms.user_service.service.impl;

import com.bbu.hrms.user_service.dto.*;
import com.bbu.hrms.user_service.exception.RoleNotFoundException;
import com.bbu.hrms.user_service.exception.UserNotFoundException;
import com.bbu.hrms.user_service.model.*;
import com.bbu.hrms.user_service.repository.*;
import com.bbu.hrms.user_service.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDto createUser(CreateUserRequest request) {
        User user = new User();
        user.setAuthUserId(request.getAuthUserId());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
       user.setFirstName(request.getFirstName());
       user.setLastName(request.getLastName());
       user.setPhoneNumber(request.getPhoneNumber());
       user.setAuthUserId(request.getAuthUserId());
       user.setStatus("ACTIVE");

        userRepository.save(user);
        return toDTO(user);
    }

    @Override
    public UserDto getUserById(Long id) {
        return userRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public PagedResponse<UserDto> getAllUsers(Pageable pageable) {
        Page<UserDto> pageResult = userRepository.findAll(pageable).map(this::toDTO);
        return new PagedResponse<>(
                pageResult.getContent(),
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isLast()
        );
    }

    @Override
    public UserDto assignRole(Long authUserId, AssignRoleRequest request) {
        User user = userRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Role role = roleRepository.findByName(request.getRoleName())
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        user.getRoles().add(role);
        userRepository.save(user);

        return toDTO(user);
    }

    public UserDto unassignRole(Long authUserId, AssignRoleRequest roleName) {
        User user = userRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Role role = roleRepository.findByName(roleName.getRoleName())
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        user.getRoles().remove(role);
        userRepository.save(user);
        return toDTO(user);
    }

    @Override
    public UserDto getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return toDTO(user);
    }

    private UserDto toDTO(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setStatus(user.getStatus());
        dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        dto.setAuthUserId(user.getAuthUserId());
        return dto;
    }
}
