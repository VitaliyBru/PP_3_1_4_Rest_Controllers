package ru.kata.spring.boot_security.demo.util;

public class UserErrorResponse {
    private final String message;

    public UserErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
