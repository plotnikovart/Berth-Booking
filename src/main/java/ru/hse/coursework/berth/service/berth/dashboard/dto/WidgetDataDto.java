package ru.hse.coursework.berth.service.berth.dashboard.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WidgetDataDto {

    @ApiModelProperty(required = true, position = 1)
    private String code;

    @ApiModelProperty(required = true, position = 2)
    private Object data;
}
