package com.movem.backend.Exception;

public class EmailNotVerifiedException extends RuntimeException {
    private final String email;

    public EmailNotVerifiedException(String email) {
        super("Please verify your email before logging in.");
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}