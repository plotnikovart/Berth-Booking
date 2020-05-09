package ru.hse.coursework.berth.config.exception.impl;

import ru.hse.coursework.berth.config.exception.ExceptionCode;

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
