package ru.kata.spring.boot_security.demo.util;

public class UserInvalidFieldsException extends RuntimeException {
    public UserInvalidFieldsException(String message) {
        super(message);
    }
}
