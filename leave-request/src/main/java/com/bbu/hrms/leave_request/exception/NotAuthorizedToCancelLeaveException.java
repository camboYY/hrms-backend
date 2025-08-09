package com.bbu.hrms.leave_request.exception;

public class NotAuthorizedToCancelLeaveException extends RuntimeException {
    public NotAuthorizedToCancelLeaveException(String message) {
        super(message);
    }
}
