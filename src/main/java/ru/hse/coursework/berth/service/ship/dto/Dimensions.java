package ru.hse.coursework.berth.service.ship.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import ru.hse.coursework.berth.config.validation.ValidationUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class Dimensions implements Serializable {

    @ApiModelProperty(required = true, position = 1)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(message = ValidationUtils.RANGE_MESSAGE)
    private Double length;

    @ApiModelProperty(required = true, position = 2)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(message = ValidationUtils.RANGE_MESSAGE)
    private Double draft;

    @ApiModelProperty(required = true, position = 3)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(message = ValidationUtils.RANGE_MESSAGE)
    private Double width;
}
