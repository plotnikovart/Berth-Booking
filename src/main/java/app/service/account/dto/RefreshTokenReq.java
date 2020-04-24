package app.service.account.dto;

import app.config.validation.ValidationUtils;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RefreshTokenReq {

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String refreshToken;
}
