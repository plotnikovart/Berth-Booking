package ru.hse.coursework.berth.service.account.recovery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.config.validation.ValidationUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class PasswordRecoveryCheckReq {

    @ApiModelProperty(required = true, position = 1)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Email(message = ValidationUtils.EMAIL_MESSAGE)
    private String email;

    @ApiModelProperty(required = true, position = 2)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String recoveryCode;
}
