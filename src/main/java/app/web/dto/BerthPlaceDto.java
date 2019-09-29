package app.web.dto;

import app.common.ValidationUtils;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class BerthPlaceDto {

    private Long id;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(message = ValidationUtils.RANGE_MESSAGE)
    private Double length;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(message = ValidationUtils.RANGE_MESSAGE)
    private Double draft;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(message = ValidationUtils.RANGE_MESSAGE)
    private Double width;

    @Range(message = ValidationUtils.RANGE_MESSAGE)
    private Double price;
}
