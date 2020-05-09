package ru.hse.coursework.berth.service.berth.dto;

import lombok.Data;
import ru.hse.coursework.berth.config.validation.ValidationUtils;
import ru.hse.coursework.berth.database.entity.DictAmenity;

import javax.validation.constraints.NotNull;

@Data
public class DictAmenityDto {

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String key;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String value;

    public static DictAmenityDto of(DictAmenity e) {
        return new DictAmenityDto()
                .setKey(e.getKey())
                .setValue(e.getValue());
    }
}
