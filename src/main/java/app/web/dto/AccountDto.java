package app.web.dto;

import app.common.ValidationUtils;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class AccountDto {

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String email;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String password;
}
