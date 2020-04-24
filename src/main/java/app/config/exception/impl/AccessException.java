package app.config.exception.impl;

import app.config.exception.ExceptionCode;

public class AccessException extends ServiceException {

    @Override
    public ExceptionCode getCode() {
        return ExceptionCode.FORBIDDEN;
    }

    public AccessException(String message) {
        super(message);
    }

    public AccessException() {
    }
}
