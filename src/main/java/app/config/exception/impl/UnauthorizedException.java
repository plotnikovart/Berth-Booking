package app.config.exception.impl;

public class UnauthorizedException extends ServiceException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException() {
    }
}
