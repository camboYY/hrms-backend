package com.bbu.hrms.auth_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class MyAuthResponse {
    private String token;
    private String username;
    private List<String> roles;

    // getters and setters
    public MyAuthResponse(String token, String username, List<String> roles) {
        this.token = token;
        this.username = username;
        this.roles = roles;
    }
}
