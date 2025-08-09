package com.bbu.hrms.auth_service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class RegisterRequest {
    public String username;
    public String password;
    public String roles;
    public String email;
}