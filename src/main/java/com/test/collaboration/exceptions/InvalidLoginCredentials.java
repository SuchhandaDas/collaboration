package com.test.collaboration.exceptions;

public class InvalidLoginCredentials extends RuntimeException {
    public InvalidLoginCredentials(String message) {
        super(message);
    }
}
