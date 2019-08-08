package app.web.dto;

import app.common.ValidationUtils;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ConvenienceDto {

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Integer id;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String code;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String name;
}
