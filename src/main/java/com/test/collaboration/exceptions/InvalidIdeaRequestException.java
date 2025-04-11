package com.test.collaboration.exceptions;

public class InvalidIdeaRequestException extends RuntimeException {
    public InvalidIdeaRequestException(String message) {
        super(message);
    }
}
