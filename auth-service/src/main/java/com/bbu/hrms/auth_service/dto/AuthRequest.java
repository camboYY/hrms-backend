package com.bbu.hrms.auth_service.dto;

import lombok.Data;

@Data
public class AuthRequest {
    public String username;
    public String password;
}
