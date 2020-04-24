package app.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ErrorResp extends Response {

    private final Error error;

    public ErrorResp(int code, String message, String details) {
        super(false);
        this.error = new Error(code, message, details);
    }


    @Getter
    @AllArgsConstructor
    public static class Error {

        private final int code;
        private final String message;
        private final String details;
    }
}
