package ru.hse.coursework.berth.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

import static ru.hse.coursework.berth.config.validation.ValidationUtils.NOT_NULL_MESSAGE;

@Data
public class PageableFilter {

    @ApiModelProperty(required = true, position = 10)
    @NotNull(message = NOT_NULL_MESSAGE)
    private Integer pageNum;

    @ApiModelProperty(required = true, position = 11)
    @NotNull(message = NOT_NULL_MESSAGE)
    private Integer pageSize;
}
