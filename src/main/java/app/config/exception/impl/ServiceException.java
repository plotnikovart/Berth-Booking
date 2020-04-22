package app.config.exception.impl;

public class ServiceException extends RuntimeException {

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
