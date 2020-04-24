package app.web.dto;

import app.config.validation.ValidationUtils;
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
    @Range(min = -180, max = 180, message = ValidationUtils.RANGE_MESSAGE)
    private Double lng;     // долгота

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(message = ValidationUtils.RANGE_MESSAGE)
    private Double standardPrice;

    private List<ConvenienceDto> convenienceList;

    private List<BerthPlaceDto> placeList;

    @Data
    public static class Req extends BerthDto {

        private List<String> photoList;
    }

    @Data
    public static class Resp extends BerthDto {

        private Long id;
        private Integer rating;
        private List<String> photoList;     // ссылки


        @Data
        public static class Search extends Resp {

            private Double distance;
            private Double minPrice;
        }
    }
}
