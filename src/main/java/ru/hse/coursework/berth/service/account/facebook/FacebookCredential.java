package ru.hse.coursework.berth.service.account.facebook;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.config.validation.ValidationUtils;

import javax.validation.constraints.NotNull;

@Data
public class FacebookCredential {

    @ApiModelProperty(required = true)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String code;

    @ApiModelProperty(required = true)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String redirectUri;
}
