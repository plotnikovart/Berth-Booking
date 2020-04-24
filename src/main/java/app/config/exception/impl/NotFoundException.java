package app.config.exception.impl;

import app.config.exception.ExceptionCode;

public class NotFoundException extends ServiceException {

    @Override
    public ExceptionCode getCode() {
        return ExceptionCode.NOT_FOUND;
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
    }
}
