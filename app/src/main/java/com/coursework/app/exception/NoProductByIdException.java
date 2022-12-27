package com.coursework.app.exception;

public class NoProductByIdException extends Throwable {
    public NoProductByIdException(String message) {
        super(message);
    }
}
