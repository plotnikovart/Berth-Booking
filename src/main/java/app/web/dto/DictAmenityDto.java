package app.web.dto;

import app.config.validation.ValidationUtils;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DictAmenityDto {

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String key;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String value;
}
