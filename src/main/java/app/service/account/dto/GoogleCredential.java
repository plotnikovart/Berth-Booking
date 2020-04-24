package app.service.account.dto;

import app.config.validation.ValidationUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GoogleCredential {

    @ApiModelProperty(required = true)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String code;

    @ApiModelProperty(required = true)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String redirectUri;
}
