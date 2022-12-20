package com.coursework.app.exception;

public class NoTransactionByIdException extends Throwable {
    public NoTransactionByIdException(String message) {
        super(message);
    }
}
