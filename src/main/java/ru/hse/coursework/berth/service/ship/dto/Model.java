package ru.hse.coursework.berth.service.ship.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class Model implements Serializable {

    @ApiModelProperty(position = 1)
    private String producer;

    @ApiModelProperty(position = 2)
    private String model;

    @ApiModelProperty(position = 3)
    private Integer year;
}
