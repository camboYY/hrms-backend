package com.bbu.hrms.user_service.service;

import com.bbu.hrms.user_service.dto.CreateUserRequest;
import com.bbu.hrms.user_service.dto.PagedResponse;
import com.bbu.hrms.user_service.dto.UserDto;
import com.bbu.hrms.user_service.dto.AssignRoleRequest;
import org.springframework.data.domain.Pageable;


public interface UserService {
    UserDto createUser(CreateUserRequest request);
    UserDto getUserById(Long id);
    PagedResponse<UserDto> getAllUsers(Pageable pageable);
    UserDto assignRole(Long userId, AssignRoleRequest request);
    UserDto getCurrentUser(String username);
    UserDto unassignRole(Long id, AssignRoleRequest roleName);
}
