package com.test.collaboration.exceptions;

public class DuplicateCollaborationRequest extends RuntimeException {
    public DuplicateCollaborationRequest(String message) {
        super(message);
    }
}
