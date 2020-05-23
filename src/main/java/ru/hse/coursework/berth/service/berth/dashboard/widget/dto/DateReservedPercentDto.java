package ru.hse.coursework.berth.service.berth.dashboard.widget.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DateReservedPercentDto {

    @ApiModelProperty(required = true, position = 1)
    private LocalDate date;

    @ApiModelProperty(required = true, position = 2)
    private ReservedPercentDto percent;
}
