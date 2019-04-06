package app.controller;

import app.common.ServiceException;
import lombok.Data;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    private final static Logger LOGGER = Logger.getRootLogger();

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleUnexpectedException(Exception ex, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ServiceException.class)
    public final ResponseEntity<ErrorDetails> handleServiceException(ServiceException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.OK);
    }

    @Data
    private static class ErrorDetails {

        private LocalDateTime dateTime;
        private String message;
        private String details;

        ErrorDetails(String message, String details) {
            this.dateTime = LocalDateTime.now();
            this.message = message;
            this.details = details;
        }
    }
}
