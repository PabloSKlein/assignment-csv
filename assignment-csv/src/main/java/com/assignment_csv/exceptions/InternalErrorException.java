package com.assignment_csv.exceptions;

public class InternalErrorException extends RuntimeException {
    public InternalErrorException(String message, Exception cause) {
        super(message, cause);
    }
}
