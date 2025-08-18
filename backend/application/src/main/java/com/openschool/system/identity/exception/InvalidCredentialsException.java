package com.openschool.system.identity.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Invalid Identity");
    }
}
