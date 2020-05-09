package ru.hse.coursework.berth.config.exception.impl;

import lombok.Setter;
import ru.hse.coursework.berth.config.exception.ExceptionCode;

@Setter
public class ServiceException extends RuntimeException {

    private ExceptionCode code;

    public ExceptionCode getCode() {
        return code == null ? ExceptionCode.WARNING : code;
    }

    public ServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ServiceException(Throwable throwable) {
        super(throwable.getMessage(), throwable);
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException() {
        super();
    }
}
