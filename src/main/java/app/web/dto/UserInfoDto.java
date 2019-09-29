package app.web.dto;

import app.common.ValidationUtils;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserInfoDto {

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String firstName;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String lastName;

    private String phCode;

    private String phNumber;

    private String photo;

    @Data
    public static class Req extends UserInfoDto {
    }

    @Data
    public static class Resp extends UserInfoDto {

        private Long accountId;
        private String email;
    }
}
