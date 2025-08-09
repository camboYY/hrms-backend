package com.bbu.hrms.auth_service.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    private String username;
    private String newPassword;

    public String getUsername() {
        return username;
    }
    public String getNewPassword() {
        return newPassword;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
