package app.common;

public class ServiceException extends RuntimeException {

    private String userMessage;

    public ServiceException(String message, Throwable throwable) {
        super(message, throwable);
        userMessage = message;
    }

    public ServiceException(String message) {
        super(message);
        userMessage = message;
    }

    public ServiceException() {}

    @Override
    public String getMessage() {
        return userMessage;
    }
}
