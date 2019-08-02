package app.common.exception;

public class UnauthorizedException extends ServiceException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException() {
    }
}
