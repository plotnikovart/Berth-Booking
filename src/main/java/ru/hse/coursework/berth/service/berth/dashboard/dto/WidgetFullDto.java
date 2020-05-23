package ru.hse.coursework.berth.service.berth.dashboard.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WidgetFullDto {

    @ApiModelProperty(required = true, position = 1)
    private String code;

    @ApiModelProperty(position = 2)
    private Object data;

    @ApiModelProperty(required = true, position = 3)
    private SettingsDto settings;
}
