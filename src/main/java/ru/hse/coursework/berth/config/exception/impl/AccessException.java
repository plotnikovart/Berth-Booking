package ru.hse.coursework.berth.config.exception.impl;

import ru.hse.coursework.berth.config.exception.ExceptionCode;

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
