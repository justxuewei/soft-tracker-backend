package com.niuxuewei.lucius.core.exception;

public class PermissionDeniedException extends ForbiddenException {

    public PermissionDeniedException() {
        super();
    }

    public PermissionDeniedException(String message) {
        super(message);
    }
}
