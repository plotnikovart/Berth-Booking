package app.config.exception.impl;

import app.config.exception.ExceptionCode;
import lombok.Setter;

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
