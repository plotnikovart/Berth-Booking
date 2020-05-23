package ru.hse.coursework.berth.service.berth.dashboard.widget.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.Month;

@Data
public class MonthRevenueDto {

    @ApiModelProperty(required = true, position = 1)
    private Integer year;

    @ApiModelProperty(required = true, position = 2)
    private Month month;

    @ApiModelProperty(required = true, position = 3)
    private Double revenue;
}
