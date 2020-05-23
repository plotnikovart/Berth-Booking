package ru.hse.coursework.berth.service.berth.dashboard.widget.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReservedPercentDto {

    @ApiModelProperty(required = true, position = 1)
    private Integer reservedPlaceNum;

    @ApiModelProperty(required = true, position = 2)
    private Integer totalPlaceNum;
}
