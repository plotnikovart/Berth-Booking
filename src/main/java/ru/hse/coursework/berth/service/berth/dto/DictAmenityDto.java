package ru.hse.coursework.berth.service.berth.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.config.validation.ValidationUtils;
import ru.hse.coursework.berth.database.entity.DictAmenity;

import javax.validation.constraints.NotNull;

@Data
public class DictAmenityDto {

    @ApiModelProperty(required = true, position = 1)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String key;

    @ApiModelProperty(required = true, position = 2)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String value;

    public static DictAmenityDto of(DictAmenity e) {
        return new DictAmenityDto()
                .setKey(e.getKey())
                .setValue(e.getValue());
    }
}
