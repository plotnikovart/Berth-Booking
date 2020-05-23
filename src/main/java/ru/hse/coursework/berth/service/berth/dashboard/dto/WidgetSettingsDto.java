package ru.hse.coursework.berth.service.berth.dashboard.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.config.validation.ValidationUtils;

import javax.validation.constraints.NotNull;

@Data
public class WidgetSettingsDto {

    @ApiModelProperty(required = true, position = 1)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String code;

    @ApiModelProperty(required = true, position = 3)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private SettingsDto settings;
}
