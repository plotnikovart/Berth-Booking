package app.config.exception;

import app.config.exception.impl.ServiceException;
import app.web.dto.response.ErrorResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResp> handleUnexpectedException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return response(ExceptionCode.INTERNAL_ERROR, ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(ServiceException.class)
    public final ResponseEntity<ErrorResp> handleServiceException(ServiceException ex, WebRequest request) {
        return response(ex.getCode(), ex.getMessage(), request.getDescription(false));
    }


    private ResponseEntity<ErrorResp> response(ExceptionCode code, String message, String details) {
        var body = new ErrorResp(code.getValue(), message, details);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }
}
