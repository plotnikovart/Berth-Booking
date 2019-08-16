package app.web.dto;

import app.common.ValidationUtils;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BerthDto {

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Length(max = 64, message = ValidationUtils.LENGTH_MESSAGE)
    private String name;

    @Length(max = 2048, message = ValidationUtils.LENGTH_MESSAGE)
    private String description;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(min = -90, max = 90, message = ValidationUtils.RANGE_MESSAGE)
    private Double lat;     // широта

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(min = 0, max = 180, message = ValidationUtils.RANGE_MESSAGE)
    private Double lng;     // долгота

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(message = ValidationUtils.RANGE_MESSAGE)
    private Double standardPrice;

    private List<String> photoList;

    private List<BerthPlaceDto.WithId> placeList;

    private List<ConvenienceDto> convenienceList;

    private Double distance;
    private Double minPrice;
    private Double rating;

    @Data
    public static class WithId extends BerthDto {

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        private Long id;
    }
}
