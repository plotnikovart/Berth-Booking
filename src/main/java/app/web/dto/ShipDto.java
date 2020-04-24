package app.web.dto;

import app.config.validation.ValidationUtils;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class ShipDto {

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Length(max = 40, message = ValidationUtils.LENGTH_MESSAGE)
    private String name;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(message = ValidationUtils.RANGE_MESSAGE)
    private Double length;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(message = ValidationUtils.RANGE_MESSAGE)
    private Double draft;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(message = ValidationUtils.RANGE_MESSAGE)
    private Double width;

    @Data
    public static class Req extends ShipDto {

        private List<String> photoList = new ArrayList<>();
    }

    @Data
    public static class Resp extends ShipDto {

        private Long id;
        private List<String> photoList = new ArrayList<>();
    }
}
