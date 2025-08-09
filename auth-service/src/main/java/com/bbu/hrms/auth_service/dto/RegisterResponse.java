package com.bbu.hrms.auth_service.dto;

public class RegisterResponse {
    private String message;
    private Long id;
    public RegisterResponse(String message, Long id) {
        this.message = message;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public Long getId() {
        return id;
    }
}
