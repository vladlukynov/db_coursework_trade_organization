package com.coursework.app.exception;

public class NoUserByLoginException extends Throwable {
    public NoUserByLoginException(String message) {
        super(message);
    }
}
