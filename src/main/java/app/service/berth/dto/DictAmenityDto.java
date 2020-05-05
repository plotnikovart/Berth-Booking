package app.service.berth.dto;

import app.config.validation.ValidationUtils;
import app.database.entity.DictAmenity;
import lombok.Data;

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
