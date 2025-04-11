package com.test.collaboration.exceptions;

public class IdeaNotFoundException extends RuntimeException {
    public IdeaNotFoundException(String message) {
        super(message);
    }
}
