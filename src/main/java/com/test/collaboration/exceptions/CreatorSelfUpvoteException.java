package com.test.collaboration.exceptions;

public class CreatorSelfUpvoteException extends RuntimeException {
    public CreatorSelfUpvoteException(String message) {
        super(message);
    }
}
