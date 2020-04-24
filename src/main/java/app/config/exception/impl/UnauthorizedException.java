package app.config.exception.impl;

import app.config.exception.ExceptionCode;

public class UnauthorizedException extends ServiceException {

    @Override
    public ExceptionCode getCode() {
        return ExceptionCode.UNAUTHORIZED;
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException() {
    }
}
