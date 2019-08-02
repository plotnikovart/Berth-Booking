package app.web.dto;

import app.common.ValidationUtils;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class UserDto {

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String firstName;
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String lastName;

    private String phCode;
    private String phNumber;

    private String photoName;
}
