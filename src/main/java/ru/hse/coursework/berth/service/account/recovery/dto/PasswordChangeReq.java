package ru.hse.coursework.berth.service.account.recovery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import ru.hse.coursework.berth.config.validation.ValidationUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import static ru.hse.coursework.berth.service.account.recovery.PasswordRecoveryService.RECOVERY_CODE_LENGTH;

@Data
public class PasswordChangeReq {

    @ApiModelProperty(required = true, position = 1)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Email(message = ValidationUtils.EMAIL_MESSAGE)
    private String email;

    @ApiModelProperty(required = true, position = 2)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String newPassword;

    @ApiModelProperty(required = true, position = 3)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(message = ValidationUtils.RANGE_MESSAGE, max = RECOVERY_CODE_LENGTH)
    private String recoveryCode;
}
