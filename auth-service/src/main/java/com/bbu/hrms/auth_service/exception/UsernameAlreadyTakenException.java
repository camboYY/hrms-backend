package com.bbu.hrms.auth_service.exception;

public class UsernameAlreadyTakenException extends RuntimeException {
    public UsernameAlreadyTakenException(String username) {
        super("Username '" + username + "' is already taken");
    }
}
