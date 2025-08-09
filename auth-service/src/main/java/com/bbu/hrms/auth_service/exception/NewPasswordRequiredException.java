package com.bbu.hrms.auth_service.exception;

public class NewPasswordRequiredException extends RuntimeException {
    public NewPasswordRequiredException(String message) {
        super(message);
    }
}
