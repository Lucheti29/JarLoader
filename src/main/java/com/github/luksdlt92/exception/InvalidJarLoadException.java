package com.github.luksdlt92.exception;

public class InvalidJarLoadException extends Exception {

    private final String mMessage;

    public InvalidJarLoadException(String message) {
        super();
        mMessage = message;
    }

    @Override
    public final String getMessage() {
        return InvalidJarLoadException.class.getSimpleName() + mMessage;
    }
}
