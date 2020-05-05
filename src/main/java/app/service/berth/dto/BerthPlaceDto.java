package app.service.berth.dto;

import app.config.validation.ValidationUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class BerthPlaceDto {

    private Long id;

    @ApiModelProperty(required = true, position = 1)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String name;

    @ApiModelProperty(required = true, position = 2)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(message = ValidationUtils.RANGE_MESSAGE)
    private Double length;

    @ApiModelProperty(required = true, position = 3)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(message = ValidationUtils.RANGE_MESSAGE)
    private Double draft;

    @ApiModelProperty(required = true, position = 4)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(message = ValidationUtils.RANGE_MESSAGE)
    private Double width;

    @ApiModelProperty(required = true, position = 5)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(message = ValidationUtils.RANGE_MESSAGE)
    private Double price;

    @ApiModelProperty(required = true, position = 6)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Double xCoord;

    @ApiModelProperty(required = true, position = 7)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Double yCoord;

    @ApiModelProperty(required = true, position = 8)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Double rotate;

    @ApiModelProperty(required = true, position = 9)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String color;
}
