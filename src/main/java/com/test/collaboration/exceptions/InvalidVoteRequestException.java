package com.test.collaboration.exceptions;

public class InvalidVoteRequestException extends RuntimeException {
    public InvalidVoteRequestException(String message) {
        super(message);
    }
}
