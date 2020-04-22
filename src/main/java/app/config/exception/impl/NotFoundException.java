package app.config.exception.impl;

public class NotFoundException extends ServiceException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
    }
}
