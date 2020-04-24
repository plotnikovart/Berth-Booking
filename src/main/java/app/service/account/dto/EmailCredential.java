package app.service.account.dto;

import app.config.validation.ValidationUtils;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class EmailCredential {

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Email(message = ValidationUtils.EMAIL_MESSAGE)
    private String email;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String password;
}
