package ru.hse.coursework.berth.service.chat.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.config.validation.ValidationUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class OffsetDto {

    @ApiModelProperty(required = true, position = 1)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Min(message = ValidationUtils.MIN_MESSAGE, value = 0)
    private Long offsetStart;

    @ApiModelProperty(required = true, position = 2)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Long offsetEnd;

    public Long getTargetNum() {
        return offsetEnd - offsetStart + 1;
    }
}
