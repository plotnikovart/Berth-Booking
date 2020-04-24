package app.service.account.dto;

import app.config.validation.ValidationUtils;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GoogleCredential {

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String code;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String redirectUri;
}
