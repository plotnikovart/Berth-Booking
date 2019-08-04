package app.web.dto;

import app.common.ValidationUtils;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class ShipDto {

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Length(max = 40, message = ValidationUtils.LENGTH_MESSAGE)
    private String name;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Double length;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Double draft;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Double width;

    private List<String> fileNames = new ArrayList<>();

    @Data
    public static class WithId extends ShipDto {

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        private Long id;
    }
}
